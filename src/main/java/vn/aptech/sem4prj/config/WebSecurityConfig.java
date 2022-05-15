
package vn.aptech.sem4prj.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.aptech.sem4prj.service.CustomOAuth2UserService;
import vn.aptech.sem4prj.service.OAuth2Service;
import vn.aptech.sem4prj.service.impl.UserDetailsServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    OAuth2Service userService;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private CustomOAuth2UserService oauth2UserService;

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Bean
    public BCryptPasswordEncoder pwdEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(pwdEncoder());
        return authProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        //routes config do not authentication
        http.authorizeRequests().antMatchers("/login", "/logout", "/oauth/**").permitAll();
        //only admin account
        http.authorizeRequests().antMatchers("/be/admin/**").access("hasRole('ROLE_ADMIN')");
        //admin and employee
        http.authorizeRequests().antMatchers("/be/account", "/be/user/**", "/be/uas/**", "/be/product/**", "/be/fap/**", "/be/cate/**", "/be/discount/**", "/be/order/**", "/be/items/**")
                .access("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')");
        //config form login
        http.authorizeRequests().and().formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/j_spring_security_check")
                .defaultSuccessUrl("/be/order")
                .failureUrl("/login?error=true")
                .usernameParameter("username")
                .passwordParameter("password")
                //google login
                .and()
                .oauth2Login()
                .loginPage("/userlogin")
                .userInfoEndpoint()
                .userService(oauth2UserService)
                .and()
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                        //get account has logged and create cookie for login
                        CustomOAuth2User user = (CustomOAuth2User) authentication.getPrincipal();
                        userService.processOAuthPostLogin(user.getEmail(), user.getName());
                        if (!userService.findByEmail(user.getEmail(), request)) {
                            request.setAttribute("message", "Your email existed!!!");
                            request.getRequestDispatcher("/userlogin").forward(request, response);
                        }
                        Cookie[] cookies = request.getCookies();
                        AtomicReference<String> emailCookie = new AtomicReference<>("");
                        AtomicReference<String> pass = new AtomicReference<>("");
                        Arrays.stream(cookies).forEach(c -> {
                            if (c.getName().equals("email")) {
                                emailCookie.set(c.getValue());
                            }
                            if (c.getName().equals("pass")) {
                                pass.set(c.getValue());
                            }
                        });
                        if (emailCookie.get().isEmpty() && pass.get().isEmpty()) {
                            Cookie email1 = new Cookie("email", user.getEmail());
                            email1.setMaxAge(60 * 60 * 24 * 15);
                            email1.setSecure(true);
                            email1.setHttpOnly(true);
                            email1.setPath("/");
                            response.addCookie(email1);
                            Cookie pass1 = new Cookie("pass", "");
                            pass1.setMaxAge(60 * 60 * 24 * 15);
                            pass1.setSecure(true);
                            pass1.setHttpOnly(true);
                            pass1.setPath("/");
                            response.addCookie(pass1);
                        }
                        response.sendRedirect("/index");
                    }
                })
                //without permission => access denide
                .and().exceptionHandling().accessDeniedPage("/403")
                //cau hinh remember me (http.authorizeRequests())
                .and()
                .rememberMe()
                .tokenRepository(this.persistentTokenRepository())
                .tokenValiditySeconds(60 * 60 * 24 * 14)
                .key("AbcdefghiJklmNoPqRstUvXyz");


    }

//    @Bean
//    public PersistentTokenRepository persistentTokenRepository() {
//        InMemoryTokenRepositoryImpl mem = new InMemoryTokenRepositoryImpl();
//        return mem;
//    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepo = new JdbcTokenRepositoryImpl();
        tokenRepo.setDataSource(dataSource);
        return tokenRepo;
    }

}