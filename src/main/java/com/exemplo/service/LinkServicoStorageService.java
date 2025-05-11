package com.exemplo.service;

import com.exemplo.model.LinkServico;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class LinkServicoStorageService {
    private static final Logger logger = LoggerFactory.getLogger(LinkServicoStorageService.class);
    private static final String ARQUIVO_LINKS = "links.json";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private List<LinkServico> links = new ArrayList<>();

    @PostConstruct
    public void init() {
        carregarLinks();
    }

    public List<LinkServico> getLinks() {
        return new ArrayList<>(links);
    }

    public void adicionarLink(LinkServico link) {
        links.add(link);
        salvarLinks();
    }

    public void removerLink(String id) {
        links.removeIf(link -> link.getId().equals(id));
        salvarLinks();
    }

    public void atualizarLink(String id, LinkServico linkAtualizado) {
        for (int i = 0; i < links.size(); i++) {
            if (links.get(i).getId().equals(id)) {
                linkAtualizado.setId(id);
                links.set(i, linkAtualizado);
                salvarLinks();
                return;
            }
        }
    }

    private void carregarLinks() {
        try {
            File arquivo = new File(ARQUIVO_LINKS);
            if (arquivo.exists()) {
                links = objectMapper.readValue(arquivo, 
                    objectMapper.getTypeFactory().constructCollectionType(List.class, LinkServico.class));
                logger.info("Links carregados com sucesso. Total: {}", links.size());
            } else {
                logger.info("Arquivo de links não encontrado. Criando novo arquivo.");
                links = new ArrayList<>();
                salvarLinks();
            }
        } catch (IOException e) {
            logger.error("Erro ao carregar links do arquivo", e);
            links = new ArrayList<>();
        }
    }

    private void salvarLinks() {
        try {
            objectMapper.writeValue(new File(ARQUIVO_LINKS), links);
            logger.info("Links salvos com sucesso. Total: {}", links.size());
        } catch (IOException e) {
            logger.error("Erro ao salvar links no arquivo", e);
        }
    }
} 