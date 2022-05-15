package vn.aptech.sem4prj.service;

import vn.aptech.sem4prj.dto.ProductDto;
import vn.aptech.sem4prj.entity.Product;

import java.util.List;
import java.util.Optional;


public interface ProductService {
    List<Product> findAll();

    Optional<Product> findById(String id);

    Product save(Product product);

    Product update(String id, Product product);

    void deleteById(String id);

    List<ProductDto> findByCateName(String cateName);
}
