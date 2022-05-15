package vn.aptech.sem4prj.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.aptech.sem4prj.entity.Order;
import vn.aptech.sem4prj.entity.OrderDetails;
import vn.aptech.sem4prj.repository.OrderRepository;
import vn.aptech.sem4prj.repository.UserRepository;
import vn.aptech.sem4prj.service.OrderDetailsService;
import vn.aptech.sem4prj.service.OrderService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository repo;
    @Autowired
    private OrderDetailsService oRepo;

    @Override
    public List<Order> findAll() {
        return repo.findAll();
    }


    @Override
    public Optional<Order> findById(int id) {
        return repo.findById(id);
    }


    @Override
    public Order save(Order order){
        order.setOrderDate(Calendar.getInstance().getTime());
        return repo.save(order);
    }

    @Override
    public Order update(int id, Order order){
        Order get = repo.getById(id);
        get.setStatus(order.getStatus());
        return repo.save(get);
    }

    @Override
    public void deleteById(int id) {
        repo.deleteById(id);
    }

    @Override
    public List<Order> findByUserId(int userId) {
        List<Order> list = repo.findAll();
        List<Order> result = new ArrayList<>();
        for(Order c : list){
            if(c.getUserId().getId() == userId){
                result.add(c);
            }
        }
        return result;
    }

    @Override
    public Double getTotalPrice(int orderId) {
        Double a = 0.0;
        List<OrderDetails> oList = oRepo.findAll();
        for(OrderDetails o : oList){
            if(o.getOrderId().getId() == orderId){
                a += o.getTotal();
            }
        }
        return a;
    }

}
