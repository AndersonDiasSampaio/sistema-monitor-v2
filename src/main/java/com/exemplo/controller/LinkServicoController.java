package com.exemplo.controller;

import com.exemplo.model.LinkServico;
import com.exemplo.service.LinkServicoService;
import com.exemplo.model.SenhaConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/links")
public class LinkServicoController {

    @Autowired
    private LinkServicoService linkServicoService;

    @Autowired
    private SenhaConfig senhaConfig;

    @GetMapping
    public List<LinkServico> getLinks() {
        return linkServicoService.getLinks();
    }

    @PostMapping
    public ResponseEntity<?> adicionarLink(@RequestBody LinkServico link, @RequestHeader("X-Admin-Password") String senha) {
        if (!senhaConfig.validarSenha(senha)) {
            return ResponseEntity.status(401).body("Senha de administrador inválida");
        }
        return ResponseEntity.ok(linkServicoService.adicionarLink(link));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removerLink(@PathVariable Long id, @RequestHeader("X-Admin-Password") String senha) {
        if (!senhaConfig.validarSenha(senha)) {
            return ResponseEntity.status(401).body("Senha de administrador inválida");
        }
        linkServicoService.removerLink(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarLink(@PathVariable Long id, @RequestBody LinkServico link, @RequestHeader("X-Admin-Password") String senha) {
        if (!senhaConfig.validarSenha(senha)) {
            return ResponseEntity.status(401).body("Senha de administrador inválida");
        }
        LinkServico linkAtualizado = linkServicoService.atualizarLink(id, link);
        return linkAtualizado != null ? ResponseEntity.ok(linkAtualizado) : ResponseEntity.notFound().build();
    }
} 