package vn.aptech.sem4prj.be;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.aptech.sem4prj.dto.AdminDto;
import vn.aptech.sem4prj.dto.UserDto;
import vn.aptech.sem4prj.entity.Users;
import vn.aptech.sem4prj.service.UserService;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.List;

@Controller
public class UserController {
    @Autowired
    private UserService service;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping(value = "/be/user")
    public String list(Model model) {
        model.addAttribute("list", service.findAll());
        return "be/user/index";
    }

    @GetMapping("/be/user/search")
    public String search(Model model, @RequestParam("search") String search) {
        model.addAttribute("list", service.findByFullnameLike(search));
        return "be/user/index";
    }

    @GetMapping(value = "/be/user/profile/{id}")
    public String get(@PathVariable int id, Model model) {
        service.findById(id).ifPresent(a -> {
            model.addAttribute("get", a);
        });
        return "be/user/profile";
    }

    @GetMapping(value = "/be/user/create")
    public String create(Model model) {
        model.addAttribute("user", new Users());
        return "be/user/create";
    }

    @PostMapping(value = "/be/user/save")
    public String save(Model model,
                       @ModelAttribute("user") Users users,
                       @RequestParam("images") MultipartFile image, // part hình
                       BindingResult result,
                       final RedirectAttributes redirectAttributes
    ) {
        try {
            byte[] bytes = image.getBytes();    // copy dữ liệu hình vào biến bytes
            File uploadFolder = ResourceUtils.getFile("classpath:static/images");   // tạo đối tượng File trỏ đến thư mục static/images
            Path imagePath = Paths.get(uploadFolder.getPath(), image.getOriginalFilename());    // kết hợp path của thư mục images với filename để tạo thành đường dẫn của file cần upload
            Files.write(imagePath, bytes);  // ghi mảng bytes vào file
            users.setImage(image.getOriginalFilename());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        // lưu product
        users.setCreatedAt(Calendar.getInstance().getTime());
        users.setModifiedAt(Calendar.getInstance().getTime());
        List<Users> checkPhone = service.findAll();
        for (Users c : checkPhone){
            if(c.getPhone().equals(users.getPhone())){
                redirectAttributes.addFlashAttribute("message", "Phone number has already exist!!");
                return "redirect:/be/user/create";
            }
        }
        service.save(users);
        return "redirect:/be/user";
    }


    @GetMapping(value = "/be/user/update/{id}")
    public String update(Model model, @PathVariable("id") int id) {
        service.findById(id).ifPresent(a -> {
            model.addAttribute("user", a);
        });
        return "be/user/update";
    }

    @PostMapping(value = "/be/user/save-put/{id}")
    public String savePut(@PathVariable int id, @ModelAttribute("user") Users users, @RequestParam("images") MultipartFile image) {
        if (!image.isEmpty()) {
            try {
                byte[] bytes = image.getBytes();    // copy dữ liệu hình vào biến bytes
                File uploadFolder = ResourceUtils.getFile("classpath:static/images");   // tạo đối tượng File trỏ đến thư mục static/images
                Path imagePath = Paths.get(uploadFolder.getPath(), image.getOriginalFilename());    // kết hợp path của thư mục images với filename để tạo thành đường dẫn của file cần upload
                Files.write(imagePath, bytes);  // ghi mảng bytes vào file
                users.setImage(image.getOriginalFilename());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            service.findById(users.getId()).ifPresent(a -> {
                users.setImage(a.getImage());

            });
        }

        users.setCreatedAt(users.getCreatedAt());
        users.setModifiedAt(Calendar.getInstance().getTime());
        service.update(id, users);
        return "redirect:/be/user";
    }

    @GetMapping(value = "/be/user/changepassword/{id}")
    public String changePassword(Model model, @PathVariable("id") int id) {
        Users users = service.findById(id).get();
        UserDto dto = new UserDto(users.getId(), users.getPhone(), users.getPassword(), null,
                users.getFullName(), users.getBirthday(), users.getGender(), users.getEmail(), users.getImage(), users.isEnabled());
        model.addAttribute("user", dto);
        return "be/user/changepassword";
    }

    @PostMapping(value = "/be/user/save-pass/{id}")
    public String savePassword(@ModelAttribute UserDto user, @PathVariable("id") int id, final RedirectAttributes redirectAttributes) {
        Users a = service.findById(id).get();
        if (passwordEncoder.matches(user.getPassword(), a.getPassword())) {
            a.setPassword(user.getNewPassword());
            service.update(a.getId(), a);
            return "redirect:/be/user";
        } else {
            redirectAttributes.addFlashAttribute("message", "Failed: Password not match");
            return "redirect:/be/user/changepassword/{id}";
        }
    }

    @GetMapping(value = "/be/user/delete/{id}")
    public String delete(@PathVariable int id) {
        service.deleteById(id);
        return "redirect:/be/user";
    }

}
