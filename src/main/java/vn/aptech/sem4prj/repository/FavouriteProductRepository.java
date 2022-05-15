package vn.aptech.sem4prj.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.aptech.sem4prj.entity.FavouriteProduct;

public interface FavouriteProductRepository extends JpaRepository<FavouriteProduct, Integer> {
}
