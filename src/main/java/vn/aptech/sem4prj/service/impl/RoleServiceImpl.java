package vn.aptech.sem4prj.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.aptech.sem4prj.entity.Role;
import vn.aptech.sem4prj.repository.RoleRepository;
import vn.aptech.sem4prj.service.RoleService;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository repo;

    @Override
    public List<Role> findAll() {
        return repo.findAll();
    }

    @Override
    public Optional<Role> findById(int id) {
        return repo.findById(id);
    }

    @Override
    public Role save(Role admin) {
        return repo.save(admin);
    }
}
