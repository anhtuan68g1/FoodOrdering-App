package vn.aptech.sem4prj.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import vn.aptech.sem4prj.dto.DiscountDto;
import vn.aptech.sem4prj.entity.Discount;
import vn.aptech.sem4prj.repository.AdminRepository;
import vn.aptech.sem4prj.repository.DiscountRepository;
import vn.aptech.sem4prj.service.DiscountService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DiscountServiceImpl implements DiscountService {
    @Autowired
    private DiscountRepository repo;
    @Override
    public List<Discount> findAll() {
        return repo.findAll();
    }

    @Override
    public Optional<Discount> findById(int id) {
        return repo.findById(id);
    }

    @Override
    public Discount save(DiscountDto discount) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Discount getOne = new Discount();
        getOne.setName(discount.getName());
        getOne.setValue(discount.getValue());
        getOne.setStart(df.parse(discount.getStart()));
        getOne.setEnd(df.parse(discount.getEnd()));
        return repo.save(getOne);
    }

    @Override
    public Discount update(int id, DiscountDto discount) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Discount getOne = repo.getById(id);
        getOne.setName(discount.getName());
        getOne.setValue(discount.getValue());
        getOne.setStart(df.parse(discount.getStart()));
        getOne.setEnd(df.parse(discount.getEnd()));
        return repo.save(getOne);
    }

    @Override
    public void deleteById(int id) {
        repo.deleteById(id);
    }

//    @Override
//    public List<Discount> findByDate(Date start, Date end) {
//        List<Discount> list = repo.findAll();
//        List<Discount> result = new ArrayList<>();
//        for(Discount d : list){
//            if(start != null && end != null) {
//
//            }
//        }
//    }

}
