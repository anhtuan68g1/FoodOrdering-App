package vn.aptech.sem4prj.service;

import javassist.NotFoundException;
import vn.aptech.sem4prj.entity.Users;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;


public interface UserService {
    List<Users> findAll();

    Optional<Users> findById(int id);

    Users save(Users users);

    Users update(int id, Users users);

    void deleteById(int id);

    boolean checkLogin(String email, String pwd);

    List<Users> findByFullnameLike(String fullname);

    Users findByEmail(String email);

    //sender email forgot password
    void updateResetPasswordToken(String token, String email) throws NotFoundException;

    Users getByResetPasswordToken(String token);

    void updatePassword(Users user, String newPassword);

    //email confirm register
    void register(Users user, String siteURL) throws Exception;

    void sendVerificationEmail(Users user, String siteURL) throws MessagingException, UnsupportedEncodingException;

    boolean verify(String verificationCode);
}
