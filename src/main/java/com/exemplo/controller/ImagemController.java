package com.exemplo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/api/imagens")
public class ImagemController {

    private static final Logger logger = LoggerFactory.getLogger(ImagemController.class);

    @Value("${app.upload.dir}")
    private String uploadDir;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImagem(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                logger.error("Arquivo vazio recebido");
                return ResponseEntity.badRequest().body("Arquivo vazio");
            }

            // Remover espaços em branco do caminho do diretório
            String cleanUploadDir = uploadDir.trim();
            logger.info("Diretório de upload original: '{}'", uploadDir);
            logger.info("Diretório de upload limpo: '{}'", cleanUploadDir);

            // Verificar se o diretório existe
            File directory = new File(cleanUploadDir);
            if (!directory.exists()) {
                logger.info("Diretório não existe, tentando criar: {}", cleanUploadDir);
                boolean created = directory.mkdirs();
                if (!created) {
                    logger.error("Falha ao criar diretório: {}", cleanUploadDir);
                    return ResponseEntity.internalServerError().body("Não foi possível criar o diretório de upload");
                }
                logger.info("Diretório criado com sucesso");
            }

            // Verificar permissões de escrita
            if (!directory.canWrite()) {
                logger.error("Sem permissão de escrita no diretório: {}", cleanUploadDir);
                return ResponseEntity.internalServerError().body("Sem permissão de escrita no diretório de upload");
            }

            // Validar extensão do arquivo
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || !originalFilename.contains(".")) {
                logger.error("Nome de arquivo inválido: {}", originalFilename);
                return ResponseEntity.badRequest().body("Nome de arquivo inválido");
            }

            String extension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
            if (!extension.matches("\\.(jpg|jpeg|png|gif)$")) {
                logger.error("Extensão de arquivo não permitida: {}", extension);
                return ResponseEntity.badRequest().body("Tipo de arquivo não permitido. Use apenas imagens (jpg, jpeg, png, gif)");
            }

            // Gerar nome único para o arquivo
            String filename = UUID.randomUUID().toString() + extension;
            logger.info("Nome do arquivo gerado: {}", filename);

            // Salvar o arquivo
            Path destinationPath = Paths.get(cleanUploadDir, filename);
            logger.info("Caminho completo do arquivo: {}", destinationPath);
            
            Files.write(destinationPath, file.getBytes());
            logger.info("Arquivo salvo com sucesso em: {}", destinationPath);

            // Retornar o caminho relativo para acesso via URL
            String relativePath = "/uploads/" + filename;
            logger.info("Caminho relativo para acesso: {}", relativePath);
            
            return ResponseEntity.ok(relativePath);

        } catch (Exception e) {
            logger.error("Erro ao fazer upload da imagem", e);
            return ResponseEntity.internalServerError().body("Erro ao fazer upload da imagem: " + e.getMessage());
        }
    }
} 