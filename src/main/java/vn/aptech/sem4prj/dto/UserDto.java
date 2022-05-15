package vn.aptech.sem4prj.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private int id;
    private String phone;
    private String password;
    private String newPassword;
    private String fullName;
    private String birthday;
    private boolean gender;
    private String email;
    private String image;
    private boolean status;
}