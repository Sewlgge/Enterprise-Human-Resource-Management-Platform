package com.company.management.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "aliyun.oss")
public class AliyunOssConfig {
    
    /**
     * OSS Endpoint
     */
    private String endpoint;
    
    /**
     * AccessKey ID
     */
    private String accessKeyId;
    
    /**
     * AccessKey Secret
     */
    private String accessKeySecret;
    
    /**
     * Bucket 名称
     */
    private String bucketName;
    
    /**
     * URL 前缀
     */
    private String urlPrefix;
}
