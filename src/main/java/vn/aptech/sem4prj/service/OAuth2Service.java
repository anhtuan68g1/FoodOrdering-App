package vn.aptech.sem4prj.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.aptech.sem4prj.entity.Users;
import vn.aptech.sem4prj.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;

@Service
public class OAuth2Service {
    @Autowired
    private UserRepository repo;

    //google login
    public void processOAuthPostLogin(String email, String name) {
        Users user = repo.findByEmail(email);
        if (user == null) {
            Users u = new Users();
            u.setPhone("");
            u.setPassword("");
            u.setFullName(name);
            u.setBirthday("");
            u.setGender(false);
            u.setEmail(email);
            u.setProvider("GOOGLE");
            u.setEnabled(true);
            u.setCreatedAt(Calendar.getInstance().getTime());
            u.setModifiedAt(Calendar.getInstance().getTime());
            repo.save(u);
        }
    }

    public boolean findByEmail(String email, HttpServletRequest request) {
        try {
            Users u = repo.findByEmail(email);
            if (u.getProvider().equals("GOOGLE")) {
                request.getSession().setAttribute("user", repo.findByEmail(email));
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
}
