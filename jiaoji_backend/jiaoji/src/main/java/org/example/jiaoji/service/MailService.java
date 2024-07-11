package org.example.jiaoji.service;

import org.example.jiaoji.mapper.UserMapper;
import org.example.jiaoji.pojo.MailStructure;
import org.example.jiaoji.pojo.RetType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserMapper userMapper;

    @Value("${spring.mail.username}")
    private String fromMail;

    public Boolean sendMain(String mail, MailStructure mailStructure) {
        Integer id = userMapper.selectIdByEmail(mail);
        if (id != null) return false;

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(fromMail);
        simpleMailMessage.setSubject(mailStructure.getSubject());
        simpleMailMessage.setText(mailStructure.getMessage());
        simpleMailMessage.setTo(mail);

        mailSender.send(simpleMailMessage);
        return true;
    }
}
