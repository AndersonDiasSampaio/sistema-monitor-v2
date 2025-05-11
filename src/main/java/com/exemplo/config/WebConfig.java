package com.exemplo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(WebConfig.class);

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        try {
            // Remover espaços em branco do caminho do diretório
            String cleanUploadDir = uploadDir.trim();
            logger.info("Diretório de upload original: '{}'", uploadDir);
            logger.info("Diretório de upload limpo: '{}'", cleanUploadDir);
            
            // Criar o diretório se não existir
            File directory = new File(cleanUploadDir);
            if (!directory.exists()) {
                logger.info("Diretório não existe, tentando criar: {}", cleanUploadDir);
                boolean created = directory.mkdirs();
                if (!created) {
                    logger.error("Falha ao criar diretório: {}", cleanUploadDir);
                } else {
                    logger.info("Diretório criado com sucesso: {}", cleanUploadDir);
                }
            } else {
                logger.info("Diretório já existe: {}", cleanUploadDir);
            }

            // Registrar o diretório de upload como um recurso estático
            String location = "file:" + cleanUploadDir + "/";
            logger.info("Registrando recurso estático: /uploads/** -> {}", location);
            
            registry.addResourceHandler("/uploads/**")
                    .addResourceLocations(location);
                    
        } catch (Exception e) {
            logger.error("Erro ao configurar recursos estáticos", e);
        }
    }
} 