package com.exemplo.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SenhaConfig {
    private static final Logger logger = LoggerFactory.getLogger(SenhaConfig.class);
    private static final String SENHA_PADRAO = "admin123";
    private String senhaAtual = SENHA_PADRAO;

    public boolean validarSenha(String senha) {
        logger.info("Validando senha. Senha fornecida: {}", senha);
        logger.info("Senha atual: {}", senhaAtual);
        boolean valida = senhaAtual.equals(senha);
        logger.info("Resultado da validação: {}", valida);
        return valida;
    }

    public void alterarSenha(String senhaAntiga, String senhaNova) {
        if (validarSenha(senhaAntiga)) {
            senhaAtual = senhaNova;
            logger.info("Senha alterada com sucesso");
        } else {
            logger.warn("Tentativa de alterar senha com senha antiga inválida");
            throw new IllegalArgumentException("Senha atual inválida");
        }
    }
} 