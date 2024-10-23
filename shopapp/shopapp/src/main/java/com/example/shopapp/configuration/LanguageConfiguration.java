package com.example.shopapp.configuration;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean; // Thêm @Bean
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
public class LanguageConfiguration {

    @Bean // Thêm @Bean để đăng ký MessageSource trong ngữ cảnh Spring
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("i18n/messages"); // Đường dẫn đến các tệp tài nguyên
        messageSource.setDefaultEncoding("UTF-8"); // Đặt mã hóa ký tự
        return messageSource;
    }
}
