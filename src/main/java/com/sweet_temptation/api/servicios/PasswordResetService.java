package com.sweet_temptation.api.servicios;

import com.sweet_temptation.api.model.PasswordHistory;
import com.sweet_temptation.api.model.PasswordResetToken;
import com.sweet_temptation.api.model.Usuario;
import com.sweet_temptation.api.repository.PasswordHistoryRepository;
import com.sweet_temptation.api.repository.PasswordResetTokenRepository;
import com.sweet_temptation.api.repository.UsuarioRepository;
import jakarta.mail.MessagingException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class PasswordResetService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordHistoryRepository passwordHistoryRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    private static final int EXPIRATION_MINUTES = 15;
    private static final int MAX_PASSWORD_HISTORY = 5;

    public PasswordResetService(UsuarioRepository usuarioRepository,
                                 PasswordResetTokenRepository tokenRepository,
                                 PasswordHistoryRepository passwordHistoryRepository,
                                 EmailService emailService,
                                 PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.tokenRepository = tokenRepository;
        this.passwordHistoryRepository = passwordHistoryRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void solicitarRecuperacion(String correo) throws MessagingException {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new NoSuchElementException("No existe un usuario con ese correo."));

        tokenRepository.findByUsuario(usuario).ifPresent(tokenRepository::delete);

        String token = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        LocalDateTime expiracion = LocalDateTime.now().plusMinutes(EXPIRATION_MINUTES);

        PasswordResetToken resetToken = new PasswordResetToken(token, usuario, expiracion);
        tokenRepository.save(resetToken);

        emailService.sendPasswordResetEmail(correo, token, usuario.getNombre());
    }

    @Transactional
    public void validarYCambiarContrasena(String token, String nuevaContrasena) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new NoSuchElementException("Token inválido o no encontrado."));

        if (resetToken.isExpired()) {
            tokenRepository.delete(resetToken);
            throw new IllegalStateException("El token ha expirado. Solicita uno nuevo.");
        }

        Usuario usuario = resetToken.getUsuario();

        if (isPasswordUsedBefore(usuario, nuevaContrasena)) {
            throw new IllegalArgumentException("No puedes usar una contraseña que ya hayas utilizado anteriormente.");
        }

        guardarContrasenaEnHistorial(usuario);

        usuario.setContrasena(passwordEncoder.encode(nuevaContrasena));
        usuario.setFechaModificacion(LocalDateTime.now());
        usuarioRepository.save(usuario);

        tokenRepository.delete(resetToken);
    }

    private boolean isPasswordUsedBefore(Usuario usuario, String nuevaContrasena) {
        String nuevaContrasenaEncoded = passwordEncoder.encode(nuevaContrasena);
        
        if (nuevaContrasenaEncoded.equals(usuario.getContrasena())) {
            return true;
        }

        List<PasswordHistory> historial = passwordHistoryRepository.findByUsuarioOrderByFechaCreacionDesc(usuario);
        for (PasswordHistory ph : historial) {
            if (nuevaContrasenaEncoded.equals(ph.getContrasenaHash())) {
                return true;
            }
        }
        return false;
    }

    private void guardarContrasenaEnHistorial(Usuario usuario) {
        PasswordHistory history = new PasswordHistory(usuario, usuario.getContrasena());
        passwordHistoryRepository.save(history);

        List<PasswordHistory> historial = passwordHistoryRepository.findByUsuarioOrderByFechaCreacionDesc(usuario);
        if (historial.size() > MAX_PASSWORD_HISTORY) {
            for (int i = MAX_PASSWORD_HISTORY; i < historial.size(); i++) {
                passwordHistoryRepository.delete(historial.get(i));
            }
        }
    }
}



