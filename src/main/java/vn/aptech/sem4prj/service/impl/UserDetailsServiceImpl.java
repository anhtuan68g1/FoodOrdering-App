package vn.aptech.sem4prj.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import vn.aptech.sem4prj.entity.Administrator;
import vn.aptech.sem4prj.entity.Role;
import vn.aptech.sem4prj.entity.Users;
import vn.aptech.sem4prj.repository.AdminRepository;
import vn.aptech.sem4prj.repository.RoleRepository;

import java.util.ArrayList;
import java.util.List;


public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private AdminRepository repo;

    @Autowired
    private RoleRepository rRepo;

    public UserDetailsServiceImpl() {

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Administrator u = repo.findByUsername(username);
        if (u == null) {
            throw new UsernameNotFoundException("Could not found account!!!!");
        }
        List<Role> roles = rRepo.findRoleByUsername(username);


        List<GrantedAuthority> grantList = new ArrayList<>();
        if (roles != null && roles.size() > 0) {
            for (Role role : roles) {
                GrantedAuthority authority = new SimpleGrantedAuthority(role.getName());
                grantList.add(authority);
            }
        } else {
            grantList.add(new SimpleGrantedAuthority("ROLE_USER"));
        }


        boolean enabled = u.getStatus();
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;
        return new User(u.getUsername(), u.getPassword(), enabled, accountNonExpired
                , credentialsNonExpired, accountNonLocked, grantList);

    }
}
