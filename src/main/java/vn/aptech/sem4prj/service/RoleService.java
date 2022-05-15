package vn.aptech.sem4prj.service;

import vn.aptech.sem4prj.entity.AdminRole;
import vn.aptech.sem4prj.entity.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService{
    List<Role> findAll();

    Optional<Role> findById(int id);

    Role save(Role admin);
}
