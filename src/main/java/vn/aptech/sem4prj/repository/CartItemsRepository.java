package vn.aptech.sem4prj.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.aptech.sem4prj.entity.CartItems;

public interface CartItemsRepository extends JpaRepository<CartItems, Integer> {


}
