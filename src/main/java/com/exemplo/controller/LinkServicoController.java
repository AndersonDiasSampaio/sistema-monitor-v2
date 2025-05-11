package com.exemplo.controller;

import com.exemplo.model.LinkServico;
import com.exemplo.model.SenhaConfig;
import com.exemplo.service.LinkServicoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/links")
public class LinkServicoController {
    private static final Logger logger = LoggerFactory.getLogger(LinkServicoController.class);

    @Autowired
    private LinkServicoService linkService;

    @Autowired
    private SenhaConfig senhaConfig;

    @GetMapping
    public ResponseEntity<List<LinkServico>> getLinks() {
        logger.info("Recebida requisição para listar todos os links");
        try {
            List<LinkServico> links = linkService.getLinks();
            logger.info("Retornando {} links", links.size());
            return ResponseEntity.ok(links);
        } catch (Exception e) {
            logger.error("Erro ao listar links", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<LinkServico> buscarLink(@PathVariable String id, @RequestHeader("X-Admin-Password") String senha) {
        logger.info("Recebida requisição para buscar link com ID: {}", id);
        
        if (!senhaConfig.validarSenha(senha)) {
            logger.warn("Senha de administrador inválida");
            return ResponseEntity.status(401).build();
        }

        try {
            List<LinkServico> links = linkService.getLinks();
            LinkServico link = links.stream()
                .filter(l -> l.getId().equals(id))
                .findFirst()
                .orElse(null);
            
            if (link != null) {
                logger.info("Link encontrado: {}", id);
                return ResponseEntity.ok(link);
            } else {
                logger.warn("Link não encontrado: {}", id);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Erro ao buscar link", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<LinkServico> adicionarLink(@RequestBody LinkServico link, @RequestHeader("X-Admin-Password") String senha) {
        logger.info("Recebida requisição para adicionar novo link: {}", link.getNome());
        
        if (!senhaConfig.validarSenha(senha)) {
            logger.warn("Senha de administrador inválida");
            return ResponseEntity.status(401).build();
        }

        try {
            LinkServico novoLink = linkService.adicionarLink(link);
            logger.info("Link adicionado com sucesso: {}", novoLink.getId());
            return ResponseEntity.ok(novoLink);
        } catch (Exception e) {
            logger.error("Erro ao adicionar link", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerLink(@PathVariable String id, @RequestHeader("X-Admin-Password") String senha) {
        logger.info("Recebida requisição para remover link com ID: {}", id);
        
        if (!senhaConfig.validarSenha(senha)) {
            logger.warn("Senha de administrador inválida");
            return ResponseEntity.status(401).build();
        }

        try {
            if (linkService.removerLink(id)) {
                logger.info("Link removido com sucesso: {}", id);
                return ResponseEntity.ok().build();
            } else {
                logger.warn("Link não encontrado para remoção: {}", id);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Erro ao remover link", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<LinkServico> atualizarLink(@PathVariable String id, @RequestBody LinkServico link, @RequestHeader("X-Admin-Password") String senha) {
        logger.info("Recebida requisição para atualizar link com ID: {}", id);
        
        if (!senhaConfig.validarSenha(senha)) {
            logger.warn("Senha de administrador inválida");
            return ResponseEntity.status(401).build();
        }

        try {
            LinkServico linkAtualizado = linkService.atualizarLink(id, link);
            if (linkAtualizado != null) {
                logger.info("Link atualizado com sucesso: {}", id);
                return ResponseEntity.ok(linkAtualizado);
            } else {
                logger.warn("Link não encontrado para atualização: {}", id);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Erro ao atualizar link", e);
            return ResponseEntity.internalServerError().build();
        }
    }
} 