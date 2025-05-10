package com.exemplo.service;

import com.exemplo.model.LinkServico;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class LinkServicoStorageService {
    private static final String ARQUIVO_LINKS = "links.json";
    private final ObjectMapper objectMapper;
    private final AtomicLong idCounter;
    private final File arquivoLinks;

    public LinkServicoStorageService() throws IOException {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.idCounter = new AtomicLong(1);
        
        // Criar diretório de dados se não existir
        String diretorioDados = "dados";
        Path diretorioPath = Paths.get(diretorioDados);
        if (!Files.exists(diretorioPath)) {
            Files.createDirectory(diretorioPath);
        }
        
        this.arquivoLinks = new File(diretorioDados, ARQUIVO_LINKS);
        carregarLinks();
    }

    private void carregarLinks() {
        try {
            if (arquivoLinks.exists()) {
                List<LinkServico> links = objectMapper.readValue(arquivoLinks, 
                    objectMapper.getTypeFactory().constructCollectionType(List.class, LinkServico.class));
                if (!links.isEmpty()) {
                    long maxId = links.stream()
                        .mapToLong(LinkServico::getId)
                        .max()
                        .orElse(0);
                    idCounter.set(maxId + 1);
                }
            } else {
                // Criar arquivo vazio se não existir
                salvarLinks(new ArrayList<>());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void salvarLinks(List<LinkServico> links) {
        try {
            objectMapper.writeValue(arquivoLinks, links);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<LinkServico> getLinks() {
        try {
            if (arquivoLinks.exists()) {
                return objectMapper.readValue(arquivoLinks, 
                    objectMapper.getTypeFactory().constructCollectionType(List.class, LinkServico.class));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public LinkServico adicionarLink(LinkServico link) {
        List<LinkServico> links = getLinks();
        link.setId(idCounter.getAndIncrement());
        links.add(link);
        salvarLinks(links);
        return link;
    }

    public void removerLink(Long id) {
        List<LinkServico> links = getLinks();
        links.removeIf(link -> link.getId().equals(id));
        salvarLinks(links);
    }

    public LinkServico atualizarLink(Long id, LinkServico linkAtualizado) {
        List<LinkServico> links = getLinks();
        for (int i = 0; i < links.size(); i++) {
            if (links.get(i).getId().equals(id)) {
                linkAtualizado.setId(id);
                links.set(i, linkAtualizado);
                salvarLinks(links);
                return linkAtualizado;
            }
        }
        return null;
    }
} 