package vn.aptech.sem4prj.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.aptech.sem4prj.dto.AdminDto;
import vn.aptech.sem4prj.entity.Administrator;
import vn.aptech.sem4prj.repository.AdminRepository;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;


public interface AdminService {

    List<Administrator> findAll();

    Optional<Administrator> findById(int id);

    Administrator save(Administrator admin);

    Administrator update(int id,Administrator admin);

    void deleteById(int id);

    List<Administrator> findByFullnameLike(String fullname);

    Administrator findByUsername(String username);
}
