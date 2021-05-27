package com.bookstore.bookstore.utilities;

import java.util.Locale;

import com.bookstore.bookstore.domain.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

@Component
public class MailConstructor {

    @Autowired
    private Environment env;

    public SimpleMailMessage constructResetTokenMailMessage(String contextPath, Locale locale, String token, User user, String password) {
        String url = contextPath + "/newUser?token=" + token;

        String message = "\nPlease click on the below link to verify your email and edit you personal information. \nYour password is : \n" + password;

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(user.getEmail());
        mail.setFrom(env.getProperty("support.email"));
        mail.setSubject("Le's BookStore new user");
        mail.setText(url + message);
        
        return mail;
    }
}
