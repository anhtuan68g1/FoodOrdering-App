package vn.aptech.sem4prj.service;

import vn.aptech.sem4prj.entity.OrderDetails;

import java.util.List;


public interface OrderDetailsService {

    List<OrderDetails> findAll();

    List<OrderDetails> getListByOrderId(int oid);

    OrderDetails save(OrderDetails orderDetails);

    void deleteById(int id);

}
