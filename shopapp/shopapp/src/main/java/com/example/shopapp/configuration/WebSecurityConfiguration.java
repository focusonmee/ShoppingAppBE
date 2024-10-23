package com.example.shopapp.configuration;

import com.example.shopapp.filter.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfiguration {

    // Định nghĩa tiền tố API
    private final String apiPrefix = "/api/v1";
    private final JwtTokenFilter jwtTokenFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable) // Tắt CSRF
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class) // Thêm bộ lọc JWT
                .authorizeHttpRequests(requests -> {
                    requests
                            .requestMatchers(
                                    String.format("%s/users/register", apiPrefix), // Cho phép yêu cầu đăng ký
                                    String.format("%s/users/login", apiPrefix),  // Cho phép yêu cầu đăng nhập
                                    String.format("%s/roles",apiPrefix),
                                    String.format("%s/orders",apiPrefix)
//                                    String.format("%s/products/images/*",apiPrefix)
                            ).permitAll()
                            // Các yêu cầu khác
                            .requestMatchers(GET, String.format("%s/users/details/", apiPrefix)).hasAnyRole("ADMIN","USER")

                            .requestMatchers(GET, String.format("%s/categories/**", apiPrefix)).permitAll()
                            .requestMatchers(POST, String.format("%s/categories/**", apiPrefix)).hasRole("ADMIN")
                            .requestMatchers(PUT, String.format("%s/categories/**", apiPrefix)).hasRole("ADMIN")
                            .requestMatchers(DELETE, String.format("%s/categories/**", apiPrefix)).hasRole("ADMIN")


                            .requestMatchers(GET, String.format("%s/products/get-products-by-keyword/", apiPrefix)).hasRole("ADMIN")
                            .requestMatchers(GET, String.format("%s/products/**", apiPrefix)).permitAll()
                            .requestMatchers(GET, String.format("%s/products**", apiPrefix)).permitAll()
                            .requestMatchers(GET, String.format("%s/products/images/*",apiPrefix)).permitAll()
                            .requestMatchers(POST, String.format("%s/products/**", apiPrefix)).hasRole("ADMIN")
                            .requestMatchers(PUT, String.format("%s/products/**", apiPrefix)).hasRole("ADMIN")
                            .requestMatchers(DELETE, String.format("%s/products/**", apiPrefix)).hasRole("ADMIN")


                            .requestMatchers(GET, String.format("%s/order_details/**", apiPrefix)).hasAnyRole("USER", "ADMIN")
                            .requestMatchers(POST, String.format("%s/order_details/**", apiPrefix)).hasRole("ADMIN")
                            .requestMatchers(PUT, String.format("%s/order_details/**", apiPrefix)).hasRole("ADMIN")
                            .requestMatchers(DELETE, String.format("%s/order_details/**", apiPrefix)).hasRole("ADMIN")

                            .requestMatchers(GET, String.format("%s/orders/get-orders-by-keyword/", apiPrefix)).hasRole("ADMIN")
                            .requestMatchers(POST, String.format("%s/orders/**", apiPrefix)).hasRole("USER")
                            .requestMatchers(PUT, String.format("%s/orders/**", apiPrefix)).permitAll()
                            .requestMatchers(DELETE, String.format("%s/orders/**", apiPrefix)).hasRole("ADMIN")
                            .requestMatchers(GET, String.format("%s/orders/**", apiPrefix)).permitAll()

                            .requestMatchers(GET, String.format("%s/payment/get-all/**", apiPrefix)).hasRole("ADMIN")
                            .requestMatchers(GET, String.format("%s/payment/me/**", apiPrefix)).hasRole("USER")
                            .requestMatchers(GET, String.format("%s/payment/**", apiPrefix)).hasAnyRole("ADMIN", "USER")
                            .requestMatchers(POST, "/api/v1/payment").permitAll() // Ví dụ chỉ cho phép role USER truy cập





                            .anyRequest().authenticated(); // Chỉ cần xác thực cho các yêu cầu khác
                });

        // Cấu hình CORS
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200")); // Chỉ cho phép nguồn từ ứng dụng Angular
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Các phương thức được phép
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Auth-Token")); // Chú ý chữ cái hoa
        configuration.setExposedHeaders(Arrays.asList("X-Auth-Token"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Đăng ký cấu hình CORS cho tất cả các endpoint

        http.cors(cors -> cors.configurationSource(source)); // Đăng ký cấu hình CORS vào HttpSecurity

        return http.build(); // Trả về cấu hình bảo mật
    }


    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        return authenticationManagerBuilder.build(); // Trả về AuthenticationManager
    }
}
