package be.url_backend.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewInterceptor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final EntityManagerFactory entityManagerFactory;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        OpenEntityManagerInViewInterceptor openEntityManagerInViewInterceptor = new OpenEntityManagerInViewInterceptor();
        openEntityManagerInViewInterceptor.setEntityManagerFactory(entityManagerFactory);

        registry.addWebRequestInterceptor(openEntityManagerInViewInterceptor)
                .addPathPatterns("/api/admin/**");
    }
} 