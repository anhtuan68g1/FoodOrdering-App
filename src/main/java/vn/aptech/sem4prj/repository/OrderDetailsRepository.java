package vn.aptech.sem4prj.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.aptech.sem4prj.entity.OrderDetails;

import java.util.List;

public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Integer> {
    //List<OrderDetails> getListOrderId(int oid);
}
