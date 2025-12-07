package com.sweet_temptation.api.controller;

import com.sweet_temptation.api.dto.LoginRequestDTO;
import com.sweet_temptation.api.dto.LoginResponseDTO;
import com.sweet_temptation.api.dto.password.ForgotPasswordRequestDTO;
import com.sweet_temptation.api.dto.password.ResetPasswordRequestDTO;
import com.sweet_temptation.api.model.Usuario;
import com.sweet_temptation.api.security.CustomUserDetailsService;
import com.sweet_temptation.api.security.JwtService;
import com.sweet_temptation.api.servicios.PasswordResetService;
import jakarta.mail.MessagingException;
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

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    private final PasswordResetService passwordResetService;

    public AuthController(AuthenticationManager authenticationManager, 
                          JwtService jwtService, 
                          CustomUserDetailsService userDetailsService,
                          PasswordResetService passwordResetService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.passwordResetService = passwordResetService;
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

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequestDTO request) {
        try {
            passwordResetService.solicitarRecuperacion(request.getCorreo());
            return ResponseEntity.ok("Se ha enviado un correo con las instrucciones para restablecer tu contraseña.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al enviar el correo. Intenta nuevamente.");
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequestDTO request) {
        try {
            passwordResetService.validarYCambiarContrasena(request.getToken(), request.getNuevaContrasena());
            return ResponseEntity.ok("Contraseña actualizada correctamente.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}

