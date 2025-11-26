package com.sweet_temptation.api.controller;

import com.sweet_temptation.api.dto.LoginRequestDTO;
import com.sweet_temptation.api.dto.LoginResponseDTO;
import com.sweet_temptation.api.model.Usuario;
import com.sweet_temptation.api.security.CustomUserDetailsService;
import com.sweet_temptation.api.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService, CustomUserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsuario(),
                            loginRequest.getContrasena()
                    )
            );

            String username = loginRequest.getUsuario();
            Usuario usuarioCompleto = userDetailsService.getUsuarioCompleto(username);
            String rol = userDetailsService.getRolByUsuario(username);
            String token = jwtService.generateToken(username, rol);

            LoginResponseDTO response = new LoginResponseDTO(
                    token,
                    usuarioCompleto.getId(),
                    usuarioCompleto.getNombre(),
                    usuarioCompleto.getCorreo(),
                    rol
            );
            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Credenciales inválidas");
        }
    }
}

