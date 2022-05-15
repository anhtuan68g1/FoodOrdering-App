package vn.aptech.sem4prj.service.impl;

import javassist.NotFoundException;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.aptech.sem4prj.entity.Users;
import vn.aptech.sem4prj.repository.UserRepository;
import vn.aptech.sem4prj.service.UserService;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository repo;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public List<Users> findAll() {
        return repo.findAll();
    }

    @Override
    public Optional<Users> findById(int id) {
        return repo.findById(id);
    }

    @Override
    public Users save(Users users) {
        users.setPassword(passwordEncoder.encode(users.getPassword()));
        return repo.save(users);
    }

    @Override
    public Users update(int id, Users users) {
        Users u = repo.findById(id).get();
        u.setPhone(users.getPhone());
        u.setPassword(users.getPassword());
        u.setFullName(users.getFullName());
        u.setBirthday(users.getBirthday());
        u.setGender(users.getGender());
        u.setEmail(u.getEmail());
        u.setImage(users.getImage());
        u.setEnabled(u.isEnabled());
        u.setProvider(u.getProvider());
        u.setCreatedAt(u.getCreatedAt());
        u.setModifiedAt(users.getModifiedAt());
        return repo.save(u);
    }

    @Override
    public void deleteById(int id) {
        repo.deleteById(id);
    }

    @Override
    public boolean checkLogin(String email, String pwd) {
        Users check = repo.checkLogin(email);
        if (check != null && check.isEnabled()) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            if (passwordEncoder.matches(pwd, check.getPassword())) {
                return true;
            }
            return false;
        } else {
            return false;
        }
    }

    @Override
    public List<Users> findByFullnameLike(String fullname) {
        return repo.findByFullnameLike(fullname);
    }

    @Override
    public Users findByEmail(String email) {
        return repo.findByEmail(email);
    }



    //email sender forgot password
    @Override
    public void updateResetPasswordToken(String token, String email) throws NotFoundException {
        Users user = repo.findByEmail(email);
        if (user != null) {
            user.setResetPasswordToken(token);
            repo.save(user);
        } else {
            throw new NotFoundException("Could not find any customer with the email " + email);
        }
    }

    @Override
    public Users getByResetPasswordToken(String token) {
        return repo.findByResetPasswordToken(token);
    }

    @Override
    public void updatePassword(Users user, String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);

        user.setResetPasswordToken(null);
        repo.save(user);
    }

    //confirm email register
    public boolean checkEmailDuplicate(String email) {
        List<Users> list = repo.findAll();
        for (Users u : list) {
            if (u.getEmail().equals(email)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void register(Users user, String siteURL)
            throws Exception {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        String randomCode = RandomString.make(64);
        user.setVerificationCode(randomCode);
        user.setEnabled(false);
        if (!checkEmailDuplicate(user.getEmail())) {
            throw new Exception("Email already exists!!!");
        } else {
            repo.save(user);
            sendVerificationEmail(user, siteURL);
        }
    }

    @Override
    public void sendVerificationEmail(Users user, String siteURL)
            throws MessagingException, UnsupportedEncodingException {
        String toAddress = user.getEmail();
        String fromAddress = "tintotiato@gmail.com";
        String senderName = "Project Sem 4";
        String subject = "Please verify your registration";
        String content = "Dear [[name]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "Your company name.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", user.getFullName());
        String verifyURL = siteURL + "/verify?code=" + user.getVerificationCode();

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        mailSender.send(message);
    }

    @Override
    public boolean verify(String verificationCode) {
        Users user = repo.findByVerificationCode(verificationCode);

        if (user == null || user.isEnabled()) {
            return false;
        } else {
            user.setVerificationCode(null);
            user.setEnabled(true);
            repo.save(user);
            return true;
        }

    }
}
