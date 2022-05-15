package vn.aptech.sem4prj.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.aptech.sem4prj.dto.ProductDto;
import vn.aptech.sem4prj.entity.Product;
import vn.aptech.sem4prj.repository.CategoryRepository;
import vn.aptech.sem4prj.repository.ProductRepository;
import vn.aptech.sem4prj.service.ProductService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository repo;
    @Autowired
    private CategoryRepository cRepo;

    @Override
    public List<Product> findAll() {
        return repo.findAll();
    }

    @Override
    public Optional<Product> findById(String id) {
        return repo.findById(id);
    }

    @Override
    public Product save(Product product) {

        return repo.save(product);
    }

    @Override
    public List<ProductDto> findByCateName(String cateName) {
        List<Product> products = repo.findAll();
        List<ProductDto> dto = products.stream().map(p ->
                        new ProductDto(p.getId(),
                                p.getName(), p.getImage(), p.getPrice(),
                                p.getCategoryId().getName(),
                                p.getDescription(), p.getCreatedAt(), p.getModifiedAt()))
                .collect(Collectors.toList());
        List<ProductDto> result = new ArrayList<>();
        for (ProductDto p : dto) {
            if (p.getCategory_name().toLowerCase().contains(cateName.toLowerCase())) {
                result.add(p);
            }
        }
        return result;
    }

    @Override
    public Product update(String id, Product product) {
        repo.findById(id).ifPresent(u -> {
            u.setId(product.getId());
            u.setName(product.getName());
            u.setImage(product.getImage());
            u.setPrice(product.getPrice());
            u.setCategoryId(product.getCategoryId());
            u.setDescription(product.getDescription());
            u.setCreatedAt(u.getCreatedAt());
            u.setModifiedAt(product.getModifiedAt());
            repo.save(u);
        });
        return product;
    }

    @Override
    public void deleteById(String id) {
        repo.deleteById(id);
    }

}
