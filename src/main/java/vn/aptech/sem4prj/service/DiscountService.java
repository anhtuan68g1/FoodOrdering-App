package vn.aptech.sem4prj.service;

import org.springframework.stereotype.Service;
import vn.aptech.sem4prj.dto.DiscountDto;
import vn.aptech.sem4prj.entity.Discount;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;


public interface DiscountService {
    List<Discount> findAll();

    Optional<Discount> findById(int id);

    Discount save(DiscountDto discount) throws ParseException;

    Discount update(int id, DiscountDto discount) throws ParseException;

    void deleteById(int id);

//    List<Discount> findByDate(Date start, Date end);
}
