package vn.aptech.sem4prj.be;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.aptech.sem4prj.service.FavouriteProductService;

@Controller
public class FavouriteProductController {
    @Autowired
    private FavouriteProductService service;

    @GetMapping(value = "/be/fap")
    public String list(Model model) {
        model.addAttribute("list", service.findAll());
        return "be/favourite_product/index";
    }

    @GetMapping("/be/fap/search")
    public String search(Model model, @RequestParam(value="search", required = false) Integer search) {
        if(search != null){
            model.addAttribute("list", service.findByUserId(search));
            return "be/favourite_product/index";
        }
        model.addAttribute("list", service.findAll());
        return "be/favourite_product/index";
    }

}
