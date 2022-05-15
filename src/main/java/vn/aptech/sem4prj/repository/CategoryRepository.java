package vn.aptech.sem4prj.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.aptech.sem4prj.entity.Category;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    @Query("SELECT a FROM Category a WHERE a.name LIKE %?1%")
    List<Category> findByNameLike(String name);
}
