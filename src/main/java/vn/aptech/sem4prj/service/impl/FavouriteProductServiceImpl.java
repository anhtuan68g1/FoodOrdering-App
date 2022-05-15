package vn.aptech.sem4prj.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import vn.aptech.sem4prj.entity.FavouriteProduct;
import vn.aptech.sem4prj.repository.AdminRepository;
import vn.aptech.sem4prj.repository.FavouriteProductRepository;
import vn.aptech.sem4prj.service.FavouriteProductService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FavouriteProductServiceImpl implements FavouriteProductService {
    @Autowired
    private FavouriteProductRepository repo;
    @Override
    public List<FavouriteProduct> findAll() {
        return repo.findAll();
    }

    @Override
    public Optional<FavouriteProduct> findById(int id) {
        return repo.findById(id);
    }

    @Override
    public FavouriteProduct save(FavouriteProduct favProduct) {
        return repo.save(favProduct);
    }

    @Override
    public void deleteById(int id) {
        repo.deleteById(id);
    }

    @Override
    public List<FavouriteProduct> findByUserId(int userId) {
        List<FavouriteProduct> result = repo.findAll();
        List<FavouriteProduct> rs = new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            if(result.get(i).getUserId().getId() == userId){
                rs.add(result.get(i));
            }
        }
        return rs;
    }

}
