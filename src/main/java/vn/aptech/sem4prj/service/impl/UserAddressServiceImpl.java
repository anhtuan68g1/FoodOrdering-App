package vn.aptech.sem4prj.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import vn.aptech.sem4prj.entity.UserAddress;
import vn.aptech.sem4prj.repository.AdminRepository;
import vn.aptech.sem4prj.repository.UserAddressRepository;
import vn.aptech.sem4prj.service.UserAddressService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserAddressServiceImpl implements UserAddressService {
    @Autowired
    private UserAddressRepository repo;
    @Override
    public List<UserAddress> findAll() {
        return repo.findAll();
    }

    @Override
    public Optional<UserAddress> findById(int id) {
        return repo.findById(id);
    }

    @Override
    public UserAddress save(UserAddress userAddress) {
        return repo.save(userAddress);
    }

    @Override
    public UserAddress update(int id,UserAddress userAddress) {
        UserAddress getOne = repo.getById(id);
        getOne.setUserId(getOne.getUserId());
        getOne.setReceiver(userAddress.getReceiver());
        getOne.setAddress(userAddress.getAddress());
        getOne.setPhone(userAddress.getPhone());
        return repo.save(getOne);
    }

    @Override
    public void deleteById(int id) {
        repo.deleteById(id);
    }

    @Override
    public List<UserAddress> findByUserId(int id) {
        List<UserAddress> list = repo.findAll();
        List<UserAddress> result = new ArrayList<>();
        for(UserAddress ua : list){
            if(ua.getUserId().getId() == id){
                result.add(ua);
            }
        }

        return result;
    }

}
