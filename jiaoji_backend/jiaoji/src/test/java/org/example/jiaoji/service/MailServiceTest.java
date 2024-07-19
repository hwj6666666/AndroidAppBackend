package org.example.jiaoji.service;

import org.example.jiaoji.mapper.UserMapper;
import org.example.jiaoji.pojo.MailStructure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MailServiceTest {

    @InjectMocks
    private MailService mailService;
    @Mock
    private JavaMailSender mailSender;
    @Mock
    private UserMapper userMapper;

    @Value("${spring.mail.username}")
    private String fromMail = "test@example.com";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void sendMain_shouldReturnFalseIfUserExistsWhenRegistering() {
        String email = "test@example.com";
        MailStructure mailStructure = new MailStructure();
        mailStructure.setSubject("Test Subject");
        mailStructure.setMessage("Test Message");

        when(userMapper.selectIdByEmail(email)).thenReturn(1);

        Boolean result = mailService.sendMain(email, mailStructure, true);

        assertFalse(result);
        verify(userMapper, times(1)).selectIdByEmail(email);
        verify(mailSender, times(0)).send(any(SimpleMailMessage.class));
    }

    @Test
    void sendMain_shouldSendEmailWhenUserDoesNotExistWhenRegistering() {
        String email = "test@example.com";
        MailStructure mailStructure = new MailStructure();
        mailStructure.setSubject("Test Subject");
        mailStructure.setMessage("Test Message");

        when(userMapper.selectIdByEmail(email)).thenReturn(null);

        Boolean result = mailService.sendMain(email, mailStructure, true);

        assertTrue(result);
        verify(userMapper, times(1)).selectIdByEmail(email);
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void sendMain_shouldSendEmailWhenNotRegistering() {
        String email = "test@example.com";
        MailStructure mailStructure = new MailStructure();
        mailStructure.setSubject("Test Subject");
        mailStructure.setMessage("Test Message");

        Boolean result = mailService.sendMain(email, mailStructure, false);

        assertTrue(result);
        verify(userMapper, times(0)).selectIdByEmail(email);
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}
