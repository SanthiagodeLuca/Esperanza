package com.example.esperanzaapk.ui.login;

public class LoginResponse {
    private boolean success;
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String token;
    private String error;

    public String getToken() {
        return token;
    }

    public String getError() {
        return error;
    }

}
