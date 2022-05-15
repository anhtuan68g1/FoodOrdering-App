package vn.aptech.sem4prj.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.aptech.sem4prj.entity.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {

}
