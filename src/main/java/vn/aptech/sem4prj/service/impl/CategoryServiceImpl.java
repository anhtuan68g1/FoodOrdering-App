package vn.aptech.sem4prj.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.aptech.sem4prj.entity.Category;
import vn.aptech.sem4prj.repository.CategoryRepository;
import vn.aptech.sem4prj.service.CategoryService;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository repo;
    @Override
    public List<Category> findAll() {
        return repo.findAll();
    }

    @Override
    public Optional<Category> findById(int id) {
        return repo.findById(id);
    }

    @Override
    public Category save(Category category) {
        return repo.save(category);
    }

    @Override
    public void deleteById(int id) {
        repo.deleteById(id);
    }

    @Override
    public List<Category> findByNameLike(String name) {
        return repo.findByNameLike(name);
    }

    @Override
    public Category update(int id, Category category) {
        Category getOne = repo.getById(id);
        getOne.setName(category.getName());
        return repo.save(getOne);
    }
}
