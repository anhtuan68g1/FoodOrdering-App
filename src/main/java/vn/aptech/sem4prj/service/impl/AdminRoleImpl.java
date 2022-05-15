package vn.aptech.sem4prj.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.aptech.sem4prj.entity.AdminRole;
import vn.aptech.sem4prj.entity.Administrator;
import vn.aptech.sem4prj.repository.AdminRoleRepository;
import vn.aptech.sem4prj.service.AdminRoleService;
import vn.aptech.sem4prj.service.AdminService;

import java.util.List;
import java.util.Optional;

@Service
public class AdminRoleImpl implements AdminRoleService {

    @Autowired
    private AdminRoleRepository repo;

    @Override
    public List<AdminRole> findAll() {
        return repo.findAll();
    }

    @Override
    public Optional<AdminRole> findById(int id) {
        return repo.findById(id);
    }

    @Override
    public Optional<AdminRole> findByAdminId(int adminId) {
        for (AdminRole a : repo.findAll()){
            if(a.getAdminId().getId() == adminId){
                return Optional.of(a);
            }
        }
        return null;
    }

    @Override
    public AdminRole save(AdminRole admin) {
        return repo.save(admin);
    }

    @Override
    public void deleteByAdminId(int adminId) {
        for (AdminRole a : repo.findAll()){
            if(a.getAdminId().getId() == adminId){
                repo.deleteById(a.getId());
            }
        }
    }
}
