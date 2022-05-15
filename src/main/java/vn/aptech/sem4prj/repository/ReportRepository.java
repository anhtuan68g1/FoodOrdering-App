package vn.aptech.sem4prj.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.aptech.sem4prj.dto.ReportDto;
import vn.aptech.sem4prj.entity.Order;

import java.util.Date;
import java.util.List;

public interface ReportRepository extends JpaRepository<Order, Integer> {
    @Query("SELECT e FROM Order e WHERE e.orderDate BETWEEN ?1 AND ?2")
    List<Order> findByOrderDate(Date from, Date to);
}
