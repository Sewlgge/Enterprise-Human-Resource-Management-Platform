package com.company.management.aspect;

import com.company.management.annotation.OperateLog;
import com.company.management.entity.Log;
import com.company.management.mapper.LogMapper;
import com.company.management.utils.ThreadLocalUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Parameter;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Aspect
@Component
@Slf4j
public class OperateLogAspect {

    private static final int PARAMS_MAX_LENGTH = 2000;
    private static final String MASK = "******";

    @Autowired
    private LogMapper logMapper;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @Around("@annotation(operateLog)")
    public Object around(ProceedingJoinPoint joinPoint, OperateLog operateLog) throws Throwable {
        Object result = joinPoint.proceed();
        try {
            saveLog(joinPoint, operateLog);
        } catch (Exception e) {
            log.error("写入操作日志失败", e);
        }
        return result;
    }

    private void saveLog(ProceedingJoinPoint joinPoint, OperateLog operateLog) throws Exception {
        HttpServletRequest request = currentRequest();
        if (request == null) {
            return;
        }

        Log sysLog = new Log();
        sysLog.setOperation(operateLog.value());
        sysLog.setMethod(request.getMethod() + " " + request.getRequestURI());
        sysLog.setIp(resolveClientIp(request));
        sysLog.setCreateTime(LocalDateTime.now());

        Map<String, Object> claims = ThreadLocalUtil.get();
        if (claims != null && claims.get("id") != null) {
            sysLog.setUserId((Integer) claims.get("id"));
        }

        if (operateLog.recordParams()) {
            sysLog.setParams(buildParams(joinPoint, request));
        } else {
            sysLog.setParams("{}");
        }

        logMapper.insert(sysLog);
    }

    private String buildParams(ProceedingJoinPoint joinPoint, HttpServletRequest request) throws Exception {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Parameter[] parameters = signature.getMethod().getParameters();
        String[] paramNames = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();

        Map<String, Object> paramMap = new LinkedHashMap<>();
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            if (shouldSkipArg(arg)) {
                continue;
            }
            String name = resolveParamName(parameters, i, paramNames);
            paramMap.put(name, maskIfSensitive(name, arg));
        }

        if (paramMap.isEmpty()) {
            request.getParameterMap().forEach((key, values) -> {
                if (values != null && values.length > 0) {
                    Object value = values.length == 1 ? values[0] : values;
                    paramMap.put(key, maskIfSensitive(key, value));
                }
            });
        }

        String json = objectMapper.writeValueAsString(paramMap);
        if (json.length() > PARAMS_MAX_LENGTH) {
            return json.substring(0, PARAMS_MAX_LENGTH);
        }
        return json;
    }

    private String resolveParamName(Parameter[] parameters, int index, String[] paramNames) {
        if (index < parameters.length) {
            Parameter parameter = parameters[index];
            RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
            if (requestParam != null) {
                String name = firstNonBlank(requestParam.value(), requestParam.name());
                if (name != null) {
                    return name;
                }
            }
            PathVariable pathVariable = parameter.getAnnotation(PathVariable.class);
            if (pathVariable != null) {
                String name = firstNonBlank(pathVariable.value(), pathVariable.name());
                if (name != null) {
                    return name;
                }
            }
            if (parameter.isAnnotationPresent(RequestBody.class)) {
                return "body";
            }
        }
        if (paramNames != null && index < paramNames.length && paramNames[index] != null) {
            return paramNames[index];
        }
        return "arg" + index;
    }

    private String firstNonBlank(String... values) {
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return null;
    }

    private boolean shouldSkipArg(Object arg) {
        return arg instanceof HttpServletRequest
                || arg instanceof HttpServletResponse
                || arg instanceof MultipartFile;
    }

    private Object maskIfSensitive(String name, Object value) {
        if (value == null) {
            return null;
        }
        String lower = name.toLowerCase();
        if (lower.contains("password")) {
            return MASK;
        }
        return value;
    }

    private HttpServletRequest currentRequest() {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return null;
        }
        return attributes.getRequest();
    }

    private String resolveClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isBlank() && !"unknown".equalsIgnoreCase(ip)) {
            return normalizeLoopback(ip.split(",")[0].trim());
        }
        ip = request.getHeader("X-Real-IP");
        if (ip != null && !ip.isBlank() && !"unknown".equalsIgnoreCase(ip)) {
            return normalizeLoopback(ip);
        }
        return normalizeLoopback(request.getRemoteAddr());
    }

    /** 将 IPv6 本机地址统一显示为 127.0.0.1 */
    private String normalizeLoopback(String ip) {
        if (ip == null || ip.isBlank()) {
            return ip;
        }
        if ("0:0:0:0:0:0:0:1".equals(ip) || "::1".equals(ip)) {
            return "127.0.0.1";
        }
        return ip;
    }
}
