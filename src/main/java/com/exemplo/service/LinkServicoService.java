package com.exemplo.service;

import com.exemplo.model.LinkServico;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class LinkServicoService {
    private static final Logger logger = LoggerFactory.getLogger(LinkServicoService.class);
    
    @Autowired
    private LinkServicoStorageService storageService;

    public List<LinkServico> getLinks() {
        logger.info("Retornando links do armazenamento");
        return storageService.getLinks();
    }

    public LinkServico adicionarLink(LinkServico link) {
        logger.info("Adicionando novo link: {}", link.getNome());
        if (link.getId() == null) {
            link.setId(UUID.randomUUID().toString());
        }
        storageService.adicionarLink(link);
        logger.info("Link adicionado com sucesso. ID: {}", link.getId());
        return link;
    }

    public boolean removerLink(String id) {
        logger.info("Removendo link com ID: {}", id);
        try {
            storageService.removerLink(id);
            logger.info("Link removido com sucesso. ID: {}", id);
            return true;
        } catch (Exception e) {
            logger.error("Erro ao remover link. ID: {}", id, e);
            return false;
        }
    }

    public LinkServico atualizarLink(String id, LinkServico linkAtualizado) {
        logger.info("Atualizando link com ID: {}", id);
        try {
            storageService.atualizarLink(id, linkAtualizado);
            logger.info("Link atualizado com sucesso. ID: {}", id);
            return linkAtualizado;
        } catch (Exception e) {
            logger.error("Erro ao atualizar link. ID: {}", id, e);
            return null;
        }
    }
} 