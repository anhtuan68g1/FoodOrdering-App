package vn.aptech.sem4prj.fe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.aptech.sem4prj.entity.Users;
import vn.aptech.sem4prj.service.UserService;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;

@Controller
public class ResgisterController {
    @Autowired
    private UserService service;


    @GetMapping(value = "/register")
    public String create(Model model) {
        model.addAttribute("user", new Users());
        return "fe/register";
    }

    @PostMapping("/process_register")
    public String processRegister(@ModelAttribute("user") Users user,
                                  HttpServletRequest request,
                                  @RequestParam("images") MultipartFile image,
                                  final RedirectAttributes redirectAttributes)
            throws Exception {
        try {
            byte[] bytes = image.getBytes();    // copy dữ liệu hình vào biến bytes
            File uploadFolder = ResourceUtils.getFile("classpath:static/images");   // tạo đối tượng File trỏ đến thư mục static/images
            Path imagePath = Paths.get(uploadFolder.getPath(), image.getOriginalFilename());    // kết hợp path của thư mục images với filename để tạo thành đường dẫn của file cần upload
            Files.write(imagePath, bytes);  // ghi mảng bytes vào file
            user.setImage(image.getOriginalFilename());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        // lưu product
        user.setCreatedAt(Calendar.getInstance().getTime());
        user.setModifiedAt(Calendar.getInstance().getTime());
        try {
            service.register(user, getSiteURL(request));
            return "fe/register_success";
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("message","Email already exists!!!");
            return "redirect:/register";
        }

    }

    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }

    @GetMapping("/verify")
    public String verifyUser(@Param("code") String code) {
        if (service.verify(code)) {
            return "fe/verify_success";
        } else {
            return "fe/verify_fail";
        }
    }
}
