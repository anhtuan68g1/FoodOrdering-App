package vn.aptech.sem4prj.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.aptech.sem4prj.entity.Users;

import java.util.List;

public interface UserRepository extends JpaRepository<Users, Integer> {
    @Query("SELECT a FROM Users a WHERE a.fullName LIKE %?1%")
    List<Users> findByFullnameLike(String fullname);
    @Query("SELECT a FROM Users a WHERE a.email LIKE %?1% ")
    Users checkLogin(String email);
    @Query("SELECT u FROM Users u WHERE u.email = ?1")
    Users findByEmail(String email);

    Users findByResetPasswordToken(String token);

    @Query("SELECT u FROM Users u WHERE u.verificationCode = ?1")
    Users findByVerificationCode(String code);

}
