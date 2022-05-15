package vn.aptech.sem4prj.be;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.aptech.sem4prj.entity.UserAddress;
import vn.aptech.sem4prj.service.UserAddressService;

@Controller
public class UserAddressController {
    @Autowired
    private UserAddressService service;

    @GetMapping(value = "/be/uas")
    public String list(Model model) {
        model.addAttribute("list", service.findAll());
        return "be/user_address/index";
    }

    @GetMapping("/be/uas/search")
    public String search(Model model, @RequestParam(name = "search", required = false) Integer search) {
        if (search != null) {
            model.addAttribute("list", service.findByUserId(search));
            return "be/user_address/index";
        }
        model.addAttribute("list", service.findAll());
        return "be/user_address/index";
    }

    @GetMapping(value = "/be/uas/create")
    public String create(Model model) {
        model.addAttribute("uas", new UserAddress());
        return "be/user_address/create";
    }

    @PostMapping(value = "/be/uas/save")
    public String save(Model model, @ModelAttribute("uas") UserAddress uas, RedirectAttributes redirectAttributes) {
        service.save(uas);
        return "redirect:/be/uas";
    }


    @GetMapping(value = "/be/uas/update/{id}")
    public String update(Model model, @PathVariable("id") int id) {
        service.findById(id).ifPresent(a -> {
            model.addAttribute("uas", a);
        });
        return "be/user_address/update";
    }

    @PostMapping(value = "/be/uas/save-put/{id}")
    public String savePut(@PathVariable int id, @ModelAttribute("uas") UserAddress uas) {
        service.update(id, uas);
        return "redirect:/be/uas";
    }


    @GetMapping(value = "/be/uas/delete/{id}")
    public String delete(@PathVariable int id) {
        service.deleteById(id);
        return "redirect:/be/uas";
    }
}
