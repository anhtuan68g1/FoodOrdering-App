package vn.aptech.sem4prj.service;


import vn.aptech.sem4prj.entity.UserAddress;

import java.util.List;
import java.util.Optional;


public interface UserAddressService {
    List<UserAddress> findAll();

    Optional<UserAddress> findById(int id);

    UserAddress save(UserAddress userAddress);

    UserAddress update(int id,UserAddress userAddress);

    void deleteById(int id);

    List<UserAddress> findByUserId(int id);

}
