package vn.aptech.sem4prj.service;

import vn.aptech.sem4prj.entity.AdminRole;

import java.util.List;
import java.util.Optional;

public interface AdminRoleService {
    List<AdminRole> findAll();

    Optional<AdminRole> findById(int id);

    Optional<AdminRole> findByAdminId(int adminId);
    AdminRole save(AdminRole admin);

    void deleteByAdminId(int id);
}
