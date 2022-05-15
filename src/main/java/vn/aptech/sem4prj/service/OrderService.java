package vn.aptech.sem4prj.service;


import vn.aptech.sem4prj.entity.Order;


import java.util.List;
import java.util.Optional;

public interface OrderService {

    List<Order> findAll();

    Optional<Order> findById(int id);

    Order save(Order order);

    Order update(int id, Order order);

    void deleteById(int id);

    List<Order> findByUserId(int id);

    Double getTotalPrice(int orderId);
}
