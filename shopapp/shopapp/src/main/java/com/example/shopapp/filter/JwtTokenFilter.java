    package com.example.shopapp.filter;

    import com.example.shopapp.component.JwtTokenUtils;
    import com.example.shopapp.model.User;
    import jakarta.annotation.Nonnull;
    import jakarta.servlet.FilterChain;
    import jakarta.servlet.ServletException;
    import jakarta.servlet.http.HttpServletRequest;
    import jakarta.servlet.http.HttpServletResponse;
    import lombok.NonNull;
    import lombok.RequiredArgsConstructor;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.security.core.userdetails.UserDetailsService;
    import org.springframework.stereotype.Component;
    import org.springframework.web.filter.OncePerRequestFilter;
    import org.springframework.data.util.Pair;
    import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

    import java.io.IOException;
    import java.util.Arrays;
    import java.util.List;

    @RequiredArgsConstructor
    @Component
    public class JwtTokenFilter extends OncePerRequestFilter {

        // Hardcode apiPrefix
        private final String apiPrefix = "/api/v1";

        private final UserDetailsService userDetailsService;
        @Autowired
        private final JwtTokenUtils jwtTokenUtil;

        @Override
        protected void doFilterInternal(@NonNull HttpServletRequest request,
                                        @Nonnull HttpServletResponse response,
                                        @NonNull FilterChain filterChain)
                throws ServletException, IOException {

            // Kiểm tra nếu request cần được bypass
            if (isByPassToken(request)) {
                filterChain.doFilter(request, response); // Bypass và tiếp tục chuỗi lọc
                return; // Dừng lại sau khi bypass
            }

            final String authHeader = request.getHeader("Authorization");

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                final String token = authHeader.substring(7);
                try {
                    final String phoneNumber = jwtTokenUtil.extractPhoneNumber(token);
                    if (phoneNumber != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        User userDetails = (User) userDetailsService.loadUserByUsername(phoneNumber);
                        if (jwtTokenUtil.validateToken(token, userDetails)) {
                            UsernamePasswordAuthenticationToken authenticationToken =
                                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                        }
                    }
                } catch (Exception e) {
                    // Gửi lỗi Unauthorized nếu token không hợp lệ
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                    return; // Dừng lại nếu gặp lỗi
                }
            }

            filterChain.doFilter(request, response); // Tiếp tục chuỗi lọc
        }

        // Kiểm tra nếu request nằm trong danh sách bypass
        private boolean isByPassToken(@Nonnull HttpServletRequest request) {
            final List<Pair<String, String>> bypassTokens = Arrays.asList(
                    Pair.of(apiPrefix + "/roles", "GET"),
                    Pair.of(apiPrefix + "/products", "GET"),
                    Pair.of(apiPrefix + "/categories", "GET"),
                    Pair.of(apiPrefix + "/orders", "GET"),
                    Pair.of(apiPrefix + "/users/register", "POST"),
                    Pair.of(apiPrefix + "/users/login", "POST"),
                    Pair.of(apiPrefix + "/products/get-products-by-keyword", "GET")
            );

            String requestPath = request.getServletPath();
            String requestMethod = request.getMethod();

            if(requestPath.equals(String.format("%s/orders", apiPrefix))&& requestMethod.equals("GET")){
                return true;
            }

            for (Pair<String, String> bypassToken : bypassTokens) {
                if (request.getServletPath().contains(bypassToken.getFirst()) &&
                        request.getMethod().equalsIgnoreCase(bypassToken.getSecond())) {
                    return true; // Đã tìm thấy token bypass
                }
            }
            return false; // Không tìm thấy token bypass
        }
    }
