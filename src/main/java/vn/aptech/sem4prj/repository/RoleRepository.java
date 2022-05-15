package vn.aptech.sem4prj.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.aptech.sem4prj.entity.Role;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    @Query("SELECT o.roleId FROM AdminRole o WHERE o.adminId.username=:username")
    List<Role> findRoleByUsername(@Param("username") String username);
}
