package vn.aptech.sem4prj.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.aptech.sem4prj.entity.Product;

public interface ProductRepository extends JpaRepository<Product, String> {

}
