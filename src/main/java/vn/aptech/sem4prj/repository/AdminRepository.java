package vn.aptech.sem4prj.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.aptech.sem4prj.entity.Administrator;

import java.util.List;

public interface AdminRepository extends JpaRepository<Administrator, Integer> {
    @Query("SELECT a FROM Administrator a WHERE a.fullname LIKE %?1%")
    List<Administrator> findByFullnameLike(String fullname);
    @Query("SELECT a FROM Administrator a WHERE a.username LIKE %?1%")
    Administrator findByUsername(String username);
}
