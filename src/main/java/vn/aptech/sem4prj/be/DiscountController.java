package vn.aptech.sem4prj.be;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.aptech.sem4prj.dto.DiscountDto;
import vn.aptech.sem4prj.entity.Discount;
import vn.aptech.sem4prj.service.DiscountService;

import java.text.ParseException;

@Controller
public class DiscountController {
    @Autowired
    private DiscountService service;

    @GetMapping(value = "/be/discount")
    public String list(Model model) {
        model.addAttribute("list", service.findAll());
        return "be/discount/index";
    }

    @GetMapping(value = "/be/discount/create")
    public String create(Model model) {
        model.addAttribute("discount", new Discount());
        return "be/discount/create";
    }

    @PostMapping(value = "/be/discount/save")
    public String save(Model model, @ModelAttribute("discount") DiscountDto discount) throws ParseException {
        service.save(discount);
        return "redirect:/be/discount";
    }


    @GetMapping(value = "/be/discount/update/{id}")
    public String update(Model model, @PathVariable("id") int id) {
        service.findById(id).ifPresent(a -> {
            model.addAttribute("discount", a);
        });
        return "be/discount/update";
    }

    @PostMapping(value = "/be/discount/save-put/{id}")
    public String savePut(@PathVariable int id, @ModelAttribute("discount") DiscountDto discount) throws ParseException {
        service.update(id, discount);
        return "redirect:/be/discount";
    }


    @GetMapping(value = "/be/discount/delete/{id}")
    public String delete(@PathVariable int id) {
        service.deleteById(id);
        return "redirect:/be/discount";
    }
}
