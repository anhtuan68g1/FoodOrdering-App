package vn.aptech.sem4prj.fe;

import javassist.NotFoundException;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.aptech.sem4prj.config.Utility;
import vn.aptech.sem4prj.dto.UserDto;
import vn.aptech.sem4prj.entity.CartItems;
import vn.aptech.sem4prj.entity.UserAddress;
import vn.aptech.sem4prj.entity.Users;
import vn.aptech.sem4prj.service.OrderService;
import vn.aptech.sem4prj.service.UserAddressService;
import vn.aptech.sem4prj.service.UserService;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.UnsupportedEncodingException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;


@Controller
public class LoginController {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserAddressService addressService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public Users checkCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        Users user = null;
        String email = "", password = "";
        if (cookies != null) {
            for (Cookie s : cookies) {
                if (s.getName().equals("email")) {
                    email = s.getValue();
                }
                if (s.getName().equals("pass")) {
                    password = s.getValue();
                }
            }
        }
        if (!email.isEmpty() && password != null) {
            Users u = userService.findByEmail(email);
            if (password.equals(u.getPassword())) {
                user = u;
            }
        }
        return user;
    }

    @GetMapping("/userlogin")
    public String login(Model model) {
        return "fe/account";
    }

    @PostMapping(value = "/checkLogin")
    public String checkLogin(HttpServletRequest request,
                             HttpServletResponse response,
                             RedirectAttributes redirectAttributes) {
        HttpSession session = request.getSession();
        Users user = checkCookie(request);
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String rememberMe = request.getParameter("rememberme");
        if (user == null) {
            if (userService.checkLogin(email, password)) {
                Users a = userService.findByEmail(email);
                if (rememberMe != null) {
                    Cookie email1 = new Cookie("email", a.getEmail());
                    email1.setMaxAge(60 * 60 * 24 * 15);
                    email1.setSecure(true);
                    email1.setHttpOnly(true);
                    email1.setPath("/");
                    response.addCookie(email1);
                    Cookie pass1 = new Cookie("pass", a.getPassword());
                    pass1.setMaxAge(60 * 60 * 24 * 15);
                    pass1.setSecure(true);
                    pass1.setHttpOnly(true);
                    pass1.setPath("/");
                    response.addCookie(pass1);
                }
                session.setAttribute("user", a);
                return "redirect:/index";
            } else {
                redirectAttributes.addFlashAttribute("message", "Failed: Password not match or your account deactive");
                return "redirect:/userlogin";
            }
        }
        session.setAttribute("user", user);
        return "redirect:/index";
    }

    @GetMapping(value = "/checkLogin")
    public String checkLogin(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Users user = checkCookie(request);
        if (user != null) {
            session.setAttribute("user", user);
        }
        return "redirect:/index";
    }

    @GetMapping(value = "/log-out")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        session.removeAttribute("user");
        Arrays.stream(request.getCookies()).forEach(c -> {
            if (c.getName().equals("email")) {
                c.setMaxAge(0);
                response.addCookie(c);
            }
            if (c.getName().equals("pass")) {
                c.setMaxAge(0);
                response.addCookie(c);
            }
        });
        return "redirect:/";
    }

    @GetMapping(value = "/account-info")
    public String accountInfo(Model model, HttpServletRequest request) {
        HomeController a = new HomeController();
        a.storeSession(model, request);
        Users user = (Users) request.getSession().getAttribute("user");
        model.addAttribute("user", user);
        System.out.println("pass " +  user.getPassword());
        model.addAttribute("address_list", addressService.findByUserId(user.getId()));
        return "fe/profile";
    }

    @GetMapping(value = "/edit-account")
    public String editAccount(Model model, HttpServletRequest request) {
        HomeController a = new HomeController();
        a.storeSession(model, request);
        Users users = (Users) request.getSession().getAttribute("user");
        model.addAttribute("getAccount", users);
        return "fe/edit_account";
    }

    @PostMapping(value = "/save-account")
    public String saveAccount(Model model, HttpServletRequest request, @ModelAttribute("getAccount") Users user, @RequestParam("images") MultipartFile image) {
        HomeController a = new HomeController();
        a.storeSession(model, request);
        if (!image.isEmpty()) {
            try {
                byte[] bytes = image.getBytes();    // copy dữ liệu hình vào biến bytes
                File uploadFolder = ResourceUtils.getFile("classpath:static/images");   // tạo đối tượng File trỏ đến thư mục static/images
                Path imagePath = Paths.get(uploadFolder.getPath(), image.getOriginalFilename());    // kết hợp path của thư mục images với filename để tạo thành đường dẫn của file cần upload
                Files.write(imagePath, bytes);  // ghi mảng bytes vào file
                user.setImage(image.getOriginalFilename());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            userService.findById(user.getId()).ifPresent(b -> {
                user.setImage(b.getImage());

            });
        }
        user.setModifiedAt(Calendar.getInstance().getTime());
        userService.update(user.getId(), user);
        request.getSession().setAttribute("user", user);
        return "redirect:/account-info";
    }

    @GetMapping(value = "/change-pass")
    public String changePassUser(Model model, HttpServletRequest request) {
        HomeController a = new HomeController();
        a.storeSession(model, request);
        Users users = (Users) request.getSession().getAttribute("user");
        UserDto dto = new UserDto(users.getId(), users.getPhone(), users.getPassword(), null,
                users.getFullName(), users.getBirthday(), users.getGender(), users.getEmail(), users.getImage(), users.isEnabled());
        model.addAttribute("getUser", dto);
        return "fe/change_pass";
    }

    @PostMapping(value = "/save-pass")
    public String savePassUser(HttpServletRequest request, @ModelAttribute("getUser") UserDto userPass, RedirectAttributes redirectAttributes) {
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");
        if (passwordEncoder.matches(userPass.getPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(userPass.getNewPassword()));
            userService.update(user.getId(), user);
            return "redirect:/account-info";
        } else {
            redirectAttributes.addFlashAttribute("message", "Password not match !!!!");
            return "redirect:/change-pass";
        }
    }

    @GetMapping("/order-history")
    public String accDetail(Model model, HttpServletRequest request) {
        HomeController a = new HomeController();
        a.storeSession(model, request);
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");
        model.addAttribute("orderList", orderService.findByUserId(user.getId()));
        model.addAttribute("address_list", addressService.findByUserId(user.getId()));
        return "fe/order_history";
    }


    //    ======================================
    @GetMapping("/forgot_password")
    public String showForgotPasswordForm() {
        return "fe/forgot_password_form";
    }

    @PostMapping("/forgot_password")
    public String processForgotPassword(HttpServletRequest request, Model model) {
        String email = request.getParameter("email");
        String token = RandomString.make(30);

        try {
            userService.updateResetPasswordToken(token, email);
            String resetPasswordLink = Utility.getSiteURL(request) + "/reset_password?token=" + token;
            sendEmail(email, resetPasswordLink);
            model.addAttribute("message", "We have sent a reset password link to your email. Please check.");

        } catch (NotFoundException ex) {
            model.addAttribute("error", ex.getMessage());
        } catch (UnsupportedEncodingException | MessagingException e) {
            model.addAttribute("error", "Error while sending email");
        }

        return "fe/forgot_password_form";
    }

    public void sendEmail(String recipientEmail, String link)
            throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("contact@we.com", "Project Sem4");
        helper.setTo(recipientEmail);

        String subject = "Here's the link to reset your password";

        String content = "<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>"
                + "<p><a href=\"" + link + "\">Change my password</a></p>"
                + "<br>"
                + "<p>Ignore this email if you do remember your password, "
                + "or you have not made the request.</p>";

        helper.setSubject(subject);

        helper.setText(content, true);

        mailSender.send(message);
    }


    @GetMapping("/reset_password")
    public String showResetPasswordForm(@Param(value = "token") String token, Model model) {
        Users customer = userService.getByResetPasswordToken(token);
        model.addAttribute("token", token);

        if (customer == null) {
            model.addAttribute("message", "Invalid Token");
            return "message";
        }

        return "fe/reset_password_form";
    }

    @PostMapping("/reset_password")
    public String processResetPassword(HttpServletRequest request, Model model) {
        String token = request.getParameter("token");
        String password = request.getParameter("password");

        Users customer = userService.getByResetPasswordToken(token);
        model.addAttribute("title", "Reset your password");

        if (customer == null) {
            model.addAttribute("message", "Invalid Token");
            return "message";
        } else {
            userService.updatePassword(customer, password);

            model.addAttribute("message", "You have successfully changed your password.");
        }

        return "fe/account";
    }

}
