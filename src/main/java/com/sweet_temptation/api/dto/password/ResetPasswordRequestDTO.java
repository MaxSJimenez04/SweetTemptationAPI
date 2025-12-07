package com.sweet_temptation.api.dto.password;

public class ResetPasswordRequestDTO {
    private String token;
    private String nuevaContrasena;

    public ResetPasswordRequestDTO() {}

    public ResetPasswordRequestDTO(String token, String nuevaContrasena) {
        this.token = token;
        this.nuevaContrasena = nuevaContrasena;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNuevaContrasena() {
        return nuevaContrasena;
    }

    public void setNuevaContrasena(String nuevaContrasena) {
        this.nuevaContrasena = nuevaContrasena;
    }
}



