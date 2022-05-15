package vn.aptech.sem4prj.service;

import org.springframework.stereotype.Service;
import vn.aptech.sem4prj.entity.Category;

import java.util.List;
import java.util.Optional;


public interface CategoryService {
    List<Category> findAll();

    Optional<Category> findById(int id);

    Category save(Category category);

    Category update(int id,Category category);

    void deleteById(int id);

    List<Category> findByNameLike(String name);
}
