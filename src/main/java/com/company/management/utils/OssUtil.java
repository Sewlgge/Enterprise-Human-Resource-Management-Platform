package com.company.management.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.CopyObjectRequest;
import com.aliyun.oss.model.ObjectMetadata;
import com.company.management.config.AliyunOssConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Slf4j
@Component
public class OssUtil {

    @Autowired
    private AliyunOssConfig ossConfig;

    /**
     * 上传文件到 OSS
     *
     * @param file 文件
     * @param objectName 对象名称（路径）
     * @return 文件访问 URL
     */
    public String upload(MultipartFile file, String objectName) {
        OSS ossClient = null;
        try {
            // 创建 OSS 客户端
            ossClient = new OSSClientBuilder().build(
                    ossConfig.getEndpoint(),
                    ossConfig.getAccessKeyId(),
                    ossConfig.getAccessKeySecret()
            );

            // 获取文件输入流
            InputStream inputStream = file.getInputStream();

            // 设置元数据
            ObjectMetadata metadata = new ObjectMetadata();
            
            // 根据文件扩展名确定 Content-Type，避免 file.getContentType() 不准确
            String fileName = objectName.substring(objectName.lastIndexOf("/") + 1);
            String contentType = getContentType(fileName);
            metadata.setContentType(contentType);
            
            metadata.setContentLength(file.getSize());
            
            // 设置为 inline，让浏览器预览而不是下载
            metadata.setHeader("Content-Disposition", "inline");
            
            log.info("上传文件 - 文件名: {}, Content-Type: {}", fileName, contentType);

            // 上传文件
            ossClient.putObject(ossConfig.getBucketName(), objectName, inputStream, metadata);

            // 返回文件访问 URL
            String url = ossConfig.getUrlPrefix() + "/" + objectName;
            log.info("文件上传成功: {}", url);
            return url;

        } catch (IOException e) {
            log.error("文件上传失败", e);
            throw new RuntimeException("文件上传失败: " + e.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    /**
     * 生成对象名称（文件路径）
     * 格式: avatar/{userId}/{timestamp}_{random}.{ext}
     *
     * @param userId 用户ID
     * @param originalFilename 原始文件名
     * @return 对象名称
     */
    public String generateObjectName(Integer userId, String originalFilename) {
        // 获取文件扩展名
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        // 生成唯一文件名
        String timestamp = String.valueOf(System.currentTimeMillis());
        String random = UUID.randomUUID().toString().replace("-", "").substring(0, 8);

        return String.format("avatar/%d/%s_%s%s", userId, timestamp, random, extension);
    }

    /**
     * 更新已存在对象的元数据（设置为可预览）
     *
     * @param objectName 对象名称（不包含 bucket 名称）
     */
    public void updateObjectMetadata(String objectName) {
        OSS ossClient = null;
        try {
            ossClient = new OSSClientBuilder().build(
                    ossConfig.getEndpoint(),
                    ossConfig.getAccessKeyId(),
                    ossConfig.getAccessKeySecret()
            );

            // 通过 copyObject 方式更新元数据（源和目标相同）
            CopyObjectRequest copyObjectRequest = new CopyObjectRequest(
                    ossConfig.getBucketName(), objectName,
                    ossConfig.getBucketName(), objectName
            );

            ObjectMetadata metadata = new ObjectMetadata();
            // 设置 Content-Type 为图片类型
            String fileName = objectName.substring(objectName.lastIndexOf("/") + 1);
            String contentType = getContentType(fileName);
            metadata.setContentType(contentType);
            // 设置为 inline，让浏览器预览而不是下载
            metadata.setHeader("Content-Disposition", "inline");
            
            copyObjectRequest.setNewObjectMetadata(metadata);
            ossClient.copyObject(copyObjectRequest);
            
            log.info("更新对象元数据成功: {}", objectName);

        } catch (Exception e) {
            log.error("更新对象元数据失败: {}", objectName, e);
            throw new RuntimeException("更新对象元数据失败: " + e.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    /**
     * 根据文件扩展名获取 Content-Type
     */
    private String getContentType(String fileName) {
        if (fileName == null) {
            return "application/octet-stream";
        }
        String lowerCase = fileName.toLowerCase();
        if (lowerCase.endsWith(".jpg") || lowerCase.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (lowerCase.endsWith(".png")) {
            return "image/png";
        } else if (lowerCase.endsWith(".gif")) {
            return "image/gif";
        } else if (lowerCase.endsWith(".webp")) {
            return "image/webp";
        }
        return "application/octet-stream";
    }
}
