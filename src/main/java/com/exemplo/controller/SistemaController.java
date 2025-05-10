package com.exemplo.controller;

import com.exemplo.model.SistemaInfo;
import com.exemplo.service.SistemaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SistemaController {

    @Autowired
    private SistemaService sistemaService;

    @GetMapping("/api/sistema")
    public SistemaInfo getSistemaInfo() {
        return sistemaService.getSistemaInfo();
    }
} 