package vn.aptech.sem4prj.service;

import vn.aptech.sem4prj.entity.CartItems;

import java.util.List;
import java.util.Optional;


public interface CartItemsService {
    List<CartItems> findAll();

    Optional<CartItems> findById(int id);

    CartItems save(CartItems cart);

    CartItems update(int id, CartItems items);

    void deleteById(int id);

    List<CartItems> findByUserId(Integer id);
}
