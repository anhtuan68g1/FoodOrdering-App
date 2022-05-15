package vn.aptech.sem4prj.be;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.aptech.sem4prj.dto.ProductDto;
import vn.aptech.sem4prj.entity.Product;
import vn.aptech.sem4prj.service.CategoryService;
import vn.aptech.sem4prj.service.ProductService;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ProductController {
    @Autowired
    private ProductService service;
    @Autowired
    private CategoryService cateService;

    @GetMapping(value = "/be/product")
    public String list(Model model) {
        List<Product> products = service.findAll();
        List<ProductDto> dto = products.stream().map(p ->
                        new ProductDto(p.getId(), p.getName(), p.getImage(), p.getPrice(), p.getCategoryId().getName(), p.getDescription(), p.getCreatedAt(), p.getModifiedAt()))
                .collect(Collectors.toList());
        model.addAttribute("list", dto);
        return "be/product/index";
    }

    @GetMapping("/be/product/search")
    public String search(Model model, @RequestParam("search") String search) {
        model.addAttribute("list", service.findByCateName(search));
        return "be/product/index";
    }


    @GetMapping(value = "/be/product/create")
    public String create(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("cateList", cateService.findAll());
        return "be/product/create";
    }

    @PostMapping(value = "/be/product/save")
    public String save(Model model,
                       @ModelAttribute("product") Product product,
                       @RequestParam("images") MultipartFile image, // part hình
                       BindingResult result,
                       final RedirectAttributes redirectAttributes
    ) {
        // kiểm tra xem có hình không?
        if (!image.isEmpty()) {
            try {
                byte[] bytes = image.getBytes();    // copy dữ liệu hình vào biến bytes
                File uploadFolder = ResourceUtils.getFile("classpath:static/images");   // tạo đối tượng File trỏ đến thư mục static/images
                Path imagePath = Paths.get(uploadFolder.getPath(), image.getOriginalFilename());    // kết hợp path của thư mục images với filename để tạo thành đường dẫn của file cần upload
                Files.write(imagePath, bytes);  // ghi mảng bytes vào file
                product.setImage(image.getOriginalFilename());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            // xử lý cho trường hợp update
            // cần đọc product cũ lên để copy image
            service.findById(product.getId()).ifPresent(p -> {
                product.setImage(p.getImage());

            });
        }

        // lưu product
        List<Product> checkId = service.findAll();
        for(Product p : checkId){
            if(p.getId().equals(product.getId())){
                redirectAttributes.addFlashAttribute("message", "Failed: ID already exists");
                return "redirect:/be/product/create";
            }
        }
        product.setCreatedAt(Calendar.getInstance().getTime());
        product.setModifiedAt(Calendar.getInstance().getTime());
        service.save(product);
        return "redirect:/be/product";
    }


    @GetMapping(value = "/be/product/update/{id}")
    public String update(Model model, @PathVariable("id") String id) {
        Product p = service.findById(id).get();
        service.findById(id).ifPresent(a -> {
            model.addAttribute("product", a);
        });
        model.addAttribute("cateList", cateService.findAll());
        return "be/product/update";
    }

    @PostMapping(value = "/be/product/save-put/{id}")
    public String savePut(@PathVariable String id, @ModelAttribute("product") Product product, @RequestParam("images") MultipartFile image) {
        if (!image.isEmpty()) {
            try {
                byte[] bytes = image.getBytes();    // copy dữ liệu hình vào biến bytes
                File uploadFolder = ResourceUtils.getFile("classpath:static/images");   // tạo đối tượng File trỏ đến thư mục static/images
                Path imagePath = Paths.get(uploadFolder.getPath(), image.getOriginalFilename());    // kết hợp path của thư mục images với filename để tạo thành đường dẫn của file cần upload
                Files.write(imagePath, bytes);  // ghi mảng bytes vào file
                product.setImage(image.getOriginalFilename());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            service.findById(product.getId()).ifPresent(a -> {
                product.setImage(a.getImage());

            });
        }
        product.setCreatedAt(product.getCreatedAt());
        product.setModifiedAt(Calendar.getInstance().getTime());
        service.update(id, product);
        return "redirect:/be/product";
    }


    @GetMapping(value = "/be/product/delete/{id}")
    public String delete(@PathVariable String id) {
        service.deleteById(id);
        return "redirect:/be/product";
    }


}
