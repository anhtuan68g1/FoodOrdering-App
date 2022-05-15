package vn.aptech.sem4prj.be;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import vn.aptech.sem4prj.entity.CartItems;
import vn.aptech.sem4prj.service.CartItemsService;

import java.util.List;

@Controller
public class CartItemsController {
    @Autowired
    private CartItemsService service;

    @GetMapping(value = "/be/items")
    public String list(Model model) {
        List<CartItems> list = service.findAll();
        for(CartItems r : list) {
            r.setTotal(r.getProductId().getPrice() * r.getQuantity());
            service.save(r);
        }
        model.addAttribute("list", list);
        return "be/cart_items/index";
    }

    @GetMapping("/be/items/search")
    public String search(Model model, @RequestParam(value="search", required = false) Integer search) {
        if(search != null){
            model.addAttribute("list", service.findByUserId(search));
            return "be/cart_items/index";
        }
        model.addAttribute("list", service.findAll());
        return "be/cart_items/index";
    }

}
