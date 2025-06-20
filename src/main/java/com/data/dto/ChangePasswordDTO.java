package com.data.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class ChangePasswordDTO {
    @NotBlank(message = "Không được để trống trường này")
    private String oldPassword;

    @NotBlank(message = "Không được để trống trường này")
    private String newPassword;

    @NotBlank(message = "Không được để trống trường này")
    private String confirmNewPassword;
}