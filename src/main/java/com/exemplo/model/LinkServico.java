package com.exemplo.model;

import java.util.UUID;

public class LinkServico {
    private String id;
    private String nome;
    private String url;
    private String descricao;
    private String icone;
    private String imagemPersonalizada;

    // Construtor padrão necessário para deserialização JSON
    public LinkServico() {
        this.id = UUID.randomUUID().toString();
    }

    public LinkServico(String nome, String url, String descricao, String icone, String imagemPersonalizada) {
        this.nome = nome;
        this.url = url;
        this.descricao = descricao;
        this.icone = icone;
        this.imagemPersonalizada = imagemPersonalizada;
    }

    // Getters e Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getIcone() { return icone; }
    public void setIcone(String icone) { this.icone = icone; }

    public String getImagemPersonalizada() {
        return imagemPersonalizada;
    }

    public void setImagemPersonalizada(String imagemPersonalizada) {
        this.imagemPersonalizada = imagemPersonalizada;
    }
} 