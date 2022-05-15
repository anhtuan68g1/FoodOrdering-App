package vn.aptech.sem4prj.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.aptech.sem4prj.entity.Discount;

import java.util.Date;
import java.util.List;

public interface DiscountRepository extends JpaRepository<Discount, Integer> { ;
}
