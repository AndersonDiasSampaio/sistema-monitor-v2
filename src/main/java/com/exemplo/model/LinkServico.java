package com.exemplo.model;

public class LinkServico {
    private Long id;
    private String nome;
    private String url;
    private String descricao;
    private String icone;

    // Construtor padrão necessário para deserialização JSON
    public LinkServico() {
    }

    public LinkServico(String nome, String url, String descricao, String icone) {
        this.nome = nome;
        this.url = url;
        this.descricao = descricao;
        this.icone = icone;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getIcone() { return icone; }
    public void setIcone(String icone) { this.icone = icone; }
} 