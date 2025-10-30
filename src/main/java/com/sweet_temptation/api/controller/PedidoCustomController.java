package com.sweet_temptation.api.controller;

import com.sweet_temptation.api.servicios.PedidoCustomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/pedidocustom")
public class PedidoCustomController {
    @Autowired
    private PedidoCustomService pedidoCustomService;
}
