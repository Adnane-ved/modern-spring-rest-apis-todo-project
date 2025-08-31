package com.spring.todos.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class PasswordUpdateRequest {

    @NotEmpty(message = "old password is mandatory")
    @Size(min=5,max=30, message = "Old password must be at least 5 characters long")
    private String oldPassword;

    @NotEmpty(message = "New password is mandatory")
    @Size(min=5,max=30, message = "New password must be at least 5 characters long")
    private String NewPassword;

    @NotEmpty(message = "Confirmed password is mandatory")
    @Size(min=5,max=30, message = "Confirmed password must be at least 5 characters long")
    private String NewPassword2;

    public PasswordUpdateRequest(String oldPassword, String newPassword, String newPassword2) {
        this.oldPassword = oldPassword;
        NewPassword = newPassword;
        NewPassword2 = newPassword2;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return NewPassword;
    }

    public void setNewPassword(String newPassword) {
        NewPassword = newPassword;
    }

    public String getNewPassword2() {
        return NewPassword2;
    }

    public void setNewPassword2(String newPassword2) {
        NewPassword2 = newPassword2;
    }
}
