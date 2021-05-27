package com.bookstore.bookstore.controller;

import java.net.http.HttpRequest;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.bookstore.bookstore.domain.User;
import com.bookstore.bookstore.domain.security.PasswordResetToken;
import com.bookstore.bookstore.domain.security.Role;
import com.bookstore.bookstore.domain.security.UserRole;
import com.bookstore.bookstore.service.UserService;
import com.bookstore.bookstore.service.impl.UserSecurityService;
import com.bookstore.bookstore.utilities.MailConstructor;
import com.bookstore.bookstore.utilities.SecurityUtility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    private MailConstructor mailConstructor;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserSecurityService userSecurityService;
    
    @RequestMapping("/")
    public String index() {
       return "index";
    }

    @RequestMapping("/login")
    public String login(Model model) {
        model.addAttribute("classActiveLogin", true);

        return "myAccount";
    }

    // @RequestMapping(value = "/newUser", method = RequestMethod.POST)
    @PostMapping("/newUser")
    public String newUser(
        HttpServletRequest httpRequest, 
        @ModelAttribute("email") String email,
        @ModelAttribute("username") String username,
        Model model) throws Exception {

        model.addAttribute("classActiveNewAccount", true);

        if (null != userService.findByUsername(username)) {
            model.addAttribute("usernameExists", true);
        }

        if (null != userService.findByEmail(email)) {
            model.addAttribute("emailExists", true);
        }

        User user = new User();
        user.setEmail(email);
        user.setUsername(username);

        String rawPassword = SecurityUtility.randomPassword();
        String password = SecurityUtility.passwordEncoder().encode(rawPassword);
        user.setPassword(password);

        Role role = new Role();
        role.setRoleId(1l);
        role.setName("ROLE_USER");
        
        Set<UserRole> userRoles = new HashSet<UserRole>();
        userRoles.add(new UserRole(user, role));
        userService.createUser(user, userRoles);

        String token = UUID.randomUUID().toString();
        userService.createPasswordResetTokenForUser(user, token);

        String appUrl = "http://" 
        + httpRequest.getServerName() 
        + ":" 
        + httpRequest.getServerPort()
        + httpRequest.getContextPath();

        SimpleMailMessage mail = mailConstructor.constructResetTokenMailMessage(appUrl, httpRequest.getLocale(), token, user, rawPassword);

        mailSender.send(mail);

        model.addAttribute("emailSent", true);
        
        return "/myAccount";
    }

    @RequestMapping("/newUser")
    public String newUser(Locale locale, @RequestParam(value = "token", required = true) String token, Model model) {

        PasswordResetToken resetToken = userService.getPasswordResetToken(token);

        if (resetToken != null) {
            String message = "Invalid token.";
            model.addAttribute("message", message);

            return "redirect:/badRequest";
        }

        User user = resetToken.getUser();
        // UserDetails userDetails = new UserDetails();

        return null;
    }
}
