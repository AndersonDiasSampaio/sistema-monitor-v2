package com.exemplo.model;

import org.springframework.stereotype.Component;

@Component
public class SenhaConfig {
    private String senhaAdmin = "admin123"; // Senha padrão

    public boolean validarSenha(String senha) {
        return senhaAdmin.equals(senha);
    }

    public void alterarSenha(String novaSenha) {
        this.senhaAdmin = novaSenha;
    }
} 