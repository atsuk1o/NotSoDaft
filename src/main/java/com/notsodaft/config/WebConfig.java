package com.notsodaft.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer{
    @Value("${app.upload.dir}")
    private String uploadDir;

    @Value("${app.upload.proof.dir}")
    private String proofUploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        String absolutePath = Paths.get(uploadDir).toAbsolutePath().toString();
        registry.addResourceHandler("/uploads/reviews/**")
                .addResourceLocations("file:" + absolutePath + "/");

        String proofPath = Paths.get(proofUploadDir).toAbsolutePath().toString();
        registry.addResourceHandler("/proof/**")
                .addResourceLocations("file:" + proofPath + "/");
    }
}