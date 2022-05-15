package vn.aptech.sem4prj.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.aptech.sem4prj.entity.OrderDetails;
import vn.aptech.sem4prj.repository.CartItemsRepository;
import vn.aptech.sem4prj.repository.OrderDetailsRepository;
import vn.aptech.sem4prj.repository.OrderRepository;
import vn.aptech.sem4prj.service.OrderDetailsService;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderDetailsServiceImpl implements OrderDetailsService {
    @Autowired
    private OrderDetailsRepository repo;
    @Autowired
    private OrderRepository oRepo;
    @Autowired
    CartItemsRepository cRepo;
    @Override
    public List<OrderDetails> findAll() {
        return repo.findAll();
    }

    @Override
    public List<OrderDetails> getListByOrderId(int oid) {
        List<OrderDetails> all = repo.findAll();
        List<OrderDetails> getList = new ArrayList<>();
        for (OrderDetails orderDetails : all) {
            if (orderDetails.getOrderId().getId() == oid) {
                getList.add(orderDetails);
            }
        }
        return getList;
    }

    @Override
    public OrderDetails save(OrderDetails orderDetails) {
        return repo.save(orderDetails);
    }

    @Override
    public void deleteById(int id) {
        repo.deleteById(id);
    }

}
