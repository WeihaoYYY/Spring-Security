package Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//要启用跨域的话记得把securityConfig里面的http.cors()打开

//@Configuration
//public class CorsConfig implements WebMvcConfigurer {
//
//    @Override
//    //重写spring提供的WebMvcConfigurer接口的addCorsMappings方法
//    public void addCorsMappings(CorsRegistry registry) {
//        // 设置允许跨域的路径
//        registry.addMapping("/**")
//                // 设置允许跨域请求的域名
//                .allowedOriginPatterns("*")
//                // 是否允许cookie
//                .allowCredentials(true)
//                // 设置允许的请求方式
//                .allowedMethods("GET", "POST", "DELETE", "PUT")
//                // 设置允许的header属性
//                .allowedHeaders("*")
//                // 跨域允许时间
//                .maxAge(3600);
//    }
//}


/*
@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")  // 允许所有路径
                        .allowedOrigins("http://localhost:3000")  // 允许指定的前端源
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // 允许的 HTTP 方法
                        .allowedHeaders("*")  // 允许的请求头
                        .allowCredentials(true);  // 允许发送 Cookie
            }
        };
    }
}*/
