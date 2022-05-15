package vn.aptech.sem4prj.be;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.aptech.sem4prj.config.CustomUserDetails;
import vn.aptech.sem4prj.dto.AdminDto;
import vn.aptech.sem4prj.entity.AdminRole;
import vn.aptech.sem4prj.entity.Administrator;
import vn.aptech.sem4prj.entity.Role;
import vn.aptech.sem4prj.entity.Users;
import vn.aptech.sem4prj.service.AdminRoleService;
import vn.aptech.sem4prj.service.AdminService;
import vn.aptech.sem4prj.service.RoleService;
import vn.aptech.sem4prj.service.impl.UserDetailsServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

@Controller
public class AdminController {
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private AdminService service;
    @Autowired
    private RoleService roleService;
    @Autowired
    private AdminRoleService adminRoleService;

    @GetMapping("/login")
    public String loginPage(Model model) {
        return "be/admin/login";
    }

    @GetMapping("/403")
    public String error(Model model) {
        return "be/admin/403";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/";
    }

    @GetMapping("/be/user/profile")
    public String accountLogged(){//get account logged
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication.getName());
        Administrator accLogged = service.findByUsername(authentication.getName());
        return "redirect:/be/account/profile/" + accLogged.getId();
    }

    @GetMapping(value = "/be/admin")
    public String list(Model model) {
        model.addAttribute("list", service.findAll());
        return "be/admin/index";
    }

    @GetMapping("/be/admin/search")
    public String search(Model model, @RequestParam("search") String search) {
        model.addAttribute("list", service.findByFullnameLike(search));
        return "be/admin/index";
    }

    @GetMapping(value = "/be/account/profile/{id}")
    public String get(@PathVariable int id, Model model) {

        service.findById(id).ifPresent(a -> {
            model.addAttribute("get", a);
        });
        adminRoleService.findByAdminId(id).ifPresent(r -> {
            model.addAttribute("role", r);
        });
        return "be/admin/profile";
    }


    @GetMapping(value = "/be/admin/create")
    public String create(Model model) {
        model.addAttribute("admin", new AdminDto());
        return "be/admin/create";
    }

    @PostMapping(value = "/be/admin/save")
    public String save(Model model,
                       @ModelAttribute("admin") AdminDto admin,
                       @RequestParam("images") MultipartFile image, // part hình
                       BindingResult result,
                       final RedirectAttributes redirectAttributes
    ) throws ParseException {
        try {
            byte[] bytes = image.getBytes();    // copy dữ liệu hình vào biến bytes
            File uploadFolder = ResourceUtils.getFile("classpath:static/images");   // tạo đối tượng File trỏ đến thư mục static/images
            Path imagePath = Paths.get(uploadFolder.getPath(), image.getOriginalFilename());    // kết hợp path của thư mục images với filename để tạo thành đường dẫn của file cần upload
            Files.write(imagePath, bytes);  // ghi mảng bytes vào file
            admin.setImage(image.getOriginalFilename());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //check Id of new Admin
        List<Administrator> checkId = service.findAll();
        for (Administrator a : checkId) {
            if (a.getId() == admin.getId()) {
                redirectAttributes.addFlashAttribute("imessage", "Failed: ID already exists!!!");
                return "redirect:/be/admin/create";
            }
        }

        //check username of new Admin
        List<Administrator> checkUsername = service.findAll();
        for (Administrator a : checkUsername) {
            if (a.getUsername().equals(admin.getUsername())) {
                redirectAttributes.addFlashAttribute("umessage", "Failed: User name already exists!!!");
                return "redirect:/be/admin/create";
            }
        }

        // lưu admin\
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Administrator a = new Administrator();
        a.setId(admin.getId());
        a.setUsername(admin.getUsername());
        a.setPassword(passwordEncoder.encode(admin.getPassword()));
        a.setFullname(admin.getFullname());
        a.setBirthday(df.parse(admin.getBirthday()));
        a.setGender(admin.isGender());
        a.setImage(admin.getImage());
        a.setEmail(admin.getEmail());
        a.setStatus(admin.isStatus());
        a.setCreated_at(Calendar.getInstance().getTime());
        a.setModifiedAt(Calendar.getInstance().getTime());
        service.save(a);

        //save role
        Administrator adminId = service.findById(admin.getId()).get();
        Role roleId = roleService.findById(admin.getRoles()).get();
        AdminRole saveRole = new AdminRole();
        saveRole.setAdminId(adminId);
        saveRole.setRoleId(roleId);
        adminRoleService.save(saveRole);
        return "redirect:/be/admin";
    }

    @GetMapping(value = "/be/account/update/{id}")
    public String update(Model model, @PathVariable("id") int id) {
        AdminRole ar = adminRoleService.findByAdminId(id).get();

        Administrator admin = service.findById(id).get();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        AdminDto dto = new AdminDto(admin.getId(), admin.getUsername(), admin.getPassword(), null, ar.getRoleId().getId(),
                admin.getFullname(), df.format(admin.getBirthday()), admin.getGender(), admin.getImage(), admin.getEmail(), admin.getStatus());
        model.addAttribute("admin", dto);
        return "be/admin/update";
    }

    @PostMapping(value = "/be/account/save-put/{id}")
    public String savePut(@PathVariable int id, @ModelAttribute("admin") AdminDto admin, @RequestParam("images") MultipartFile image) throws ParseException {
        if (!image.isEmpty()) {
            try {
                byte[] bytes = image.getBytes();    // copy dữ liệu hình vào biến bytes
                File uploadFolder = ResourceUtils.getFile("classpath:static/images");   // tạo đối tượng File trỏ đến thư mục static/images
                Path imagePath = Paths.get(uploadFolder.getPath(), image.getOriginalFilename());    // kết hợp path của thư mục images với filename để tạo thành đường dẫn của file cần upload
                Files.write(imagePath, bytes);  // ghi mảng bytes vào file
                admin.setImage(image.getOriginalFilename());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            service.findById(admin.getId()).ifPresent(a -> {
                admin.setImage(a.getImage());

            });
        }
        // lưu admin\
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Administrator a = service.findById(id).get();
        a.setId(a.getId());
        a.setUsername(a.getUsername());
        a.setPassword(a.getPassword());
        a.setFullname(admin.getFullname());
        a.setBirthday(df.parse(admin.getBirthday()));
        a.setGender(admin.isGender());
        a.setImage(admin.getImage());
        a.setEmail(admin.getEmail());
        a.setStatus(admin.isStatus());
        a.setCreated_at(a.getCreatedAt());
        a.setModifiedAt(Calendar.getInstance().getTime());
        service.update(id, a);

        //update role
        Administrator adminId = service.findById(admin.getId()).get();
        Role roleId = roleService.findById(admin.getRoles()).get();
        AdminRole saveRole = adminRoleService.findByAdminId(admin.getId()).get();
        saveRole.setAdminId(adminId);
        saveRole.setRoleId(roleId);
        adminRoleService.save(saveRole);
        if(admin.getRoles() == 2){
            return "redirect:/be/account/profile/" + admin.getId();
        }
        return "redirect:/be/admin";
    }

    @GetMapping(value = "/be/account/changepassword/{id}")
    public String changePassword(Model model, @PathVariable("id") int id) throws ParseException {
        Administrator admin = service.findById(id).get();
        AdminRole ar = adminRoleService.findByAdminId(id).get();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        AdminDto dto = new AdminDto(admin.getId(), admin.getUsername(), admin.getPassword(), null, ar.getRoleId().getId(),
                admin.getFullname(), df.format(admin.getBirthday()), admin.getGender(), admin.getImage(), admin.getEmail(), admin.getStatus());
        model.addAttribute("admin", dto);
        return "be/admin/changepassword";
    }

    @PostMapping(value = "/be/account/save-pass/{id}")
    public String savePassword(@ModelAttribute AdminDto admin, @PathVariable("id") int id, final RedirectAttributes redirectAttributes) {
        Administrator a = service.findById(id).get();
        if (passwordEncoder.matches(admin.getPassword(), a.getPassword())) {
            a.setPassword(admin.getNewPassword());
            service.update(a.getId(), a);
            if(admin.getRoles() == 2){
                return "redirect:/be/account/profile/" + admin.getId();
            }
            return "redirect:/be/admin";
        } else {
            redirectAttributes.addFlashAttribute("message", "Failed: Password not match");
            return "redirect:/be/account/changepassword/{id}";
        }
    }

    @GetMapping(value = "/be/admin/delete/{id}")
    public String delete(@PathVariable int id) {
        service.deleteById(id);
        adminRoleService.deleteByAdminId(id);
        return "redirect:/be/admin";
    }

}
