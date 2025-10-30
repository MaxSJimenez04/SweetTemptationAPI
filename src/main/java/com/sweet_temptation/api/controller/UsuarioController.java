package com.sweet_temptation.api.controller;

import com.sweet_temptation.api.servicios.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/usuario")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;
}
