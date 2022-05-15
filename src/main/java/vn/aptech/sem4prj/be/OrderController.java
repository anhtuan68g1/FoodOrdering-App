package vn.aptech.sem4prj.be;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.aptech.sem4prj.entity.Order;
import vn.aptech.sem4prj.service.AdminService;
import vn.aptech.sem4prj.service.OrderService;

import java.util.List;

@Controller
public class OrderController {
    @Autowired
    private OrderService service;

    @GetMapping(value = "/be/order")
    public String list(Model model) {
        List<Order> list = service.findAll();
//        for(Order r : list) {
//            r.setTotal_price(service.getTotalPrice(r.getUserId().getId()));
//            service.save(r);
//        }

        model.addAttribute("list", list);
        return "be/order/index";
    }

    @GetMapping("/be/order/search")
    public String search(Model model, @RequestParam(value="search", required = false) Integer search) {
        if(search != null){
            model.addAttribute("list", service.findByUserId(search));
            return "be/order/index";
        }
        model.addAttribute("list", service.findAll());
        return "be/order/index";
    }

    @GetMapping(value = "/be/order/update/{id}")
    public String update(Model model, @PathVariable("id") int id) {
        service.findById(id).ifPresent(a -> {
            model.addAttribute("order", a);
        });
        return "be/order/update";
    }

    @PostMapping(value = "/be/order/save-put/{id}")
    public String savePut(@PathVariable int id, @ModelAttribute("order") Order order){
        service.update(id, order);
        return "redirect:/be/order";
    }
}
