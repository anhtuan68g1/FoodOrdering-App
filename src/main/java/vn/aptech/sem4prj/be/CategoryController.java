package vn.aptech.sem4prj.be;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.aptech.sem4prj.entity.Category;
import vn.aptech.sem4prj.service.CategoryService;

import java.util.List;

@Controller
public class CategoryController {
    @Autowired
    private CategoryService service;

    @GetMapping(value = "/be/cate")
    public String list(Model model) {
        model.addAttribute("list", service.findAll());
        return "be/category/index";
    }

    @GetMapping("/be/cate/search")
    public String search(Model model, @RequestParam("search") String search) {
        model.addAttribute("list", service.findByNameLike(search));
        return "be/category/index";
    }

    @GetMapping(value = "/be/cate/create")
    public String create(Model model) {
        model.addAttribute("category", new Category());
        return "be/category/create";
    }

    @PostMapping(value = "/be/cate/save")
    public String save(Model model, @ModelAttribute("category") Category category, RedirectAttributes redirectAttributes) {
        List<Category> checkName = service.findAll();
        for(Category c : checkName){
            if(c.getName().equals(category.getName())){
                redirectAttributes.addFlashAttribute("message", "Failed: Category name already exists!!");
                return "redirect:/be/cate/create";
            }
        }
        service.save(category);
        return "redirect:/be/cate";
    }


    @GetMapping(value = "/be/cate/update/{id}")
    public String update(Model model, @PathVariable("id") int id) {
        service.findById(id).ifPresent(a -> {
            model.addAttribute("category", a);
        });
        return "be/category/update";
    }

    @PostMapping(value = "/be/cate/save-put/{id}")
    public String savePut(@PathVariable int id, @ModelAttribute("category") Category category) {
        service.update(id, category);
        return "redirect:/be/cate";
    }


    @GetMapping(value = "/be/cate/delete/{id}")
    public String delete(@PathVariable int id) {
        service.deleteById(id);
        return "redirect:/be/cate";
    }
}
