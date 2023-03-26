package studi.immo.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Value("${picture.upload.path}")
    private String pictureUploadPath;

    @Value("${type.of.file}")
    private String typeOfFile;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path photoUploadDir = Paths.get(pictureUploadPath);
        String photoUploadPath = photoUploadDir.toFile().getAbsolutePath();
        registry.addResourceHandler(typeOfFile+"**").addResourceLocations("file:"+ photoUploadPath + "/");
    }
}
