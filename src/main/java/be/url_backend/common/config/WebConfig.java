package be.url_backend.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.io.IOException;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173", "https://perfurl.com")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 정적 리소스 처리 - 높은 우선순위로 설정
        registry.addResourceHandler("/assets/**", "/static/**", "/*.ico", "/*.png", "/*.jpg", "/*.gif")
                .addResourceLocations("file:/app/static/", "classpath:/static/")
                .setCachePeriod(3600);

        // SPA를 위한 포워딩 설정 - API가 아닌 모든 요청을 index.html로 포워딩
        registry.addResourceHandler("/**")
                .addResourceLocations("file:/app/static/", "classpath:/static/")
                .resourceChain(true)
                .addResolver(new PathResourceResolver() {
                    @Override
                    protected Resource getResource(String resourcePath, Resource location) throws IOException {
                        Resource requestedResource = location.createRelative(resourcePath);
                        
                        // 요청된 파일이 존재하면 그대로 반환
                        if (requestedResource.exists() && requestedResource.isReadable()) {
                            return requestedResource;
                        }
                        
                        // API 요청이나 특정 확장자는 처리하지 않음
                        if (resourcePath.startsWith("api/") || 
                            resourcePath.startsWith("r/") ||
                            resourcePath.endsWith(".css") || 
                            resourcePath.endsWith(".js") || 
                            resourcePath.endsWith(".png") || 
                            resourcePath.endsWith(".jpg") || 
                            resourcePath.endsWith(".gif") || 
                            resourcePath.endsWith(".ico") ||
                            resourcePath.endsWith(".woff") ||
                            resourcePath.endsWith(".woff2") ||
                            resourcePath.endsWith(".ttf") ||
                            resourcePath.endsWith(".eot")) {
                            return null;
                        }
                        
                        // Vue 라우트의 경우 index.html을 반환 (SPA 라우팅 지원)
                        return new FileSystemResource("/app/static/index.html");
                    }
                });
    }
} 