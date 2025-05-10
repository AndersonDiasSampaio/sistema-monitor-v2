package com.exemplo.service;

import com.exemplo.model.LinkServico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LinkServicoService {
    
    @Autowired
    private LinkServicoStorageService storageService;

    public List<LinkServico> getLinks() {
        return storageService.getLinks();
    }

    public LinkServico adicionarLink(LinkServico link) {
        return storageService.adicionarLink(link);
    }

    public void removerLink(Long id) {
        storageService.removerLink(id);
    }

    public LinkServico atualizarLink(Long id, LinkServico linkAtualizado) {
        return storageService.atualizarLink(id, linkAtualizado);
    }
} 