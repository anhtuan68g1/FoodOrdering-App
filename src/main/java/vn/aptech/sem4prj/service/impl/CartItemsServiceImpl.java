package vn.aptech.sem4prj.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.aptech.sem4prj.entity.CartItems;
import vn.aptech.sem4prj.entity.Product;
import vn.aptech.sem4prj.entity.Users;
import vn.aptech.sem4prj.repository.CartItemsRepository;
import vn.aptech.sem4prj.repository.OrderRepository;
import vn.aptech.sem4prj.repository.ProductRepository;
import vn.aptech.sem4prj.repository.UserRepository;
import vn.aptech.sem4prj.service.CartItemsService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CartItemsServiceImpl implements CartItemsService {
    @Autowired
    private CartItemsRepository repo;
    @Autowired
    private ProductRepository pRepo;
    @Autowired
    private UserRepository uRepo;
    @Autowired
    private OrderRepository oRepo;
    @Override
    public List<CartItems> findAll() {
        return repo.findAll();
    }

    @Override
    public Optional<CartItems> findById(int id) {
        return repo.findById(id);
    }

    @Override
    public CartItems save(CartItems cart) {
        return repo.save(cart);
    }

    @Override
    public CartItems update(int id, CartItems items) {
        CartItems getOne = repo.getById(id);
        Users u = uRepo.getById(getOne.getUserId().getId());
        Product p = pRepo.getById(getOne.getProductId().getId());
        getOne.setUserId(u);
        getOne.setProductId(p);
        getOne.setQuantity(getOne.getQuantity());
        getOne.setTotal(getOne.getQuantity() * p.getPrice());
        return repo.save(getOne);
    }

    @Override
    public void deleteById(int id) {
        repo.deleteById(id);
    }

    @Override
    public List<CartItems> findByUserId(Integer id) {
        List<CartItems> list = repo.findAll();
        List<CartItems> result = new ArrayList<>();
        for(CartItems c : list){
            if(Objects.equals(c.getUserId().getId(), id)){
                result.add(c);
            }
        }
        return result;
    }

}
