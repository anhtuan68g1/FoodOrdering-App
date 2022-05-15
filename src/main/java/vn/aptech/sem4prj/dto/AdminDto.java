package vn.aptech.sem4prj.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminDto {
    private int id;
    private String username;
    private String password;
    private String newPassword;
    private Integer roles;
    private String fullname;
    private String birthday;
    private boolean gender;
    private String image;
    private String email;
    private boolean status;

    public AdminDto(int id, String username, String password, String newPassword, int roles, String fullname, String birthday, boolean gender, String image, String email, String status) {
    }
}


