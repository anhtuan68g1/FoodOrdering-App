package vn.aptech.sem4prj.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import vn.aptech.sem4prj.entity.Administrator;
import vn.aptech.sem4prj.repository.AdminRepository;
import vn.aptech.sem4prj.service.AdminService;

import java.util.List;
import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private AdminRepository repo;
    @Override
    public List<Administrator> findAll() {
        return repo.findAll();
    }

    @Override
    public Optional<Administrator> findById(int id) {
        return repo.findById(id);
    }

    @Override
    public Administrator update(int id, Administrator admin){
        Administrator getOne = repo.getById(id);
        getOne.setUsername(getOne.getUsername());
        getOne.setPassword(passwordEncoder.encode(getOne.getPassword()));
        getOne.setFullname(admin.getFullname());
        getOne.setBirthday(admin.getBirthday());
        getOne.setGender(admin.getGender());
        getOne.setImage(admin.getImage());
        getOne.setEmail(admin.getEmail());
        getOne.setStatus(admin.getStatus());
        getOne.setCreated_at(getOne.getCreatedAt());
        getOne.setModifiedAt(admin.getModifiedAt());
        return repo.save(getOne);
    }

    @Override
    public Administrator save(Administrator admin){
//        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        return repo.save(admin);
    }

    @Override
    public void deleteById(int id) {
        repo.deleteById(id);
    }

    @Override
    public  List<Administrator> findByFullnameLike(String fullname) {
        return repo.findByFullnameLike(fullname);
    }

    @Override
    public Administrator findByUsername(String username) {
        return repo.findByUsername(username);
    }


}
