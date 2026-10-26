package com.ecom.config;

import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadLocation = Paths.get(uploadDir).toAbsolutePath().normalize().toUri().toString();
        if (!uploadLocation.endsWith("/")) {
            uploadLocation += "/";
        }

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(uploadLocation);

        // /img/** ưu tiên tìm trong thư mục upload ngoài (ảnh do người dùng/admin tải lên),
        // sau đó mới tới ảnh mẫu đóng gói sẵn trong classpath:/static/img/.
        // Nhờ vậy các template vẫn dùng đường dẫn /img/product_img/... như cũ.
        registry.addResourceHandler("/img/**")
                .addResourceLocations(uploadLocation, "classpath:/static/img/");
    }
}
