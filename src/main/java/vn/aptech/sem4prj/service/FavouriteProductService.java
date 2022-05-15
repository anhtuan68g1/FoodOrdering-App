package vn.aptech.sem4prj.service;

import org.springframework.stereotype.Service;
import vn.aptech.sem4prj.entity.FavouriteProduct;

import java.util.List;
import java.util.Optional;


public interface FavouriteProductService {
    List<FavouriteProduct> findAll();

    Optional<FavouriteProduct> findById(int id);

    FavouriteProduct save(FavouriteProduct favouriteProduct);

    void deleteById(int id);

    List<FavouriteProduct> findByUserId(int userId);
}
