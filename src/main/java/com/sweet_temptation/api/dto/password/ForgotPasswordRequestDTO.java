package com.sweet_temptation.api.dto.password;

public class ForgotPasswordRequestDTO {
    private String correo;

    public ForgotPasswordRequestDTO() {}

    public ForgotPasswordRequestDTO(String correo) {
        this.correo = correo;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
}




