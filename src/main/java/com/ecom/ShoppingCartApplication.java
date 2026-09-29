package com.ecom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.Contact;

@OpenAPIDefinition(
	info = @Info(
		title = "Ecom Store API Documentation",
		version = "2.0",
		description = "Hệ thống API Nền tảng Thương mại Điện tử Ecom Store",
		contact = @Contact(name = "Ecom Store Support", email = "hotro@ecomstore.com")
	)
)
@SpringBootApplication
public class ShoppingCartApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShoppingCartApplication.class, args);
	}

}
