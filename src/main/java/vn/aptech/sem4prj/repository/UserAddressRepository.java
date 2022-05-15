package vn.aptech.sem4prj.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.aptech.sem4prj.entity.UserAddress;

import java.util.List;

public interface UserAddressRepository extends JpaRepository<UserAddress, Integer> {
}
