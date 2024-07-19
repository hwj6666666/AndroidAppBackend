package org.example.jiaoji.controller;

import org.example.jiaoji.pojo.MailStructure;
import org.example.jiaoji.service.MailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class MailControllerTest {

    @InjectMocks
    private MailController mailController;

    @Mock
    private MailService mailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendMailSuccess() {
        MailStructure mailStructure = new MailStructure();
        when(mailService.sendMain(eq("test@example.com"), any(MailStructure.class), eq(true))).thenReturn(true);

        String response = mailController.sendMail("test@example.com", mailStructure);
        assertEquals("Mail Sent Successfully !!", response);
        verify(mailService, times(1)).sendMain("test@example.com", mailStructure, true);
    }

    @Test
    void testSendMailFailure() {
        MailStructure mailStructure = new MailStructure();
        when(mailService.sendMain(eq("test@example.com"), any(MailStructure.class), eq(true))).thenReturn(false);

        String response = mailController.sendMail("test@example.com", mailStructure);
        assertEquals("Mail Sent Failed!!", response);
        verify(mailService, times(1)).sendMain("test@example.com", mailStructure, true);
    }

    @Test
    void testResetMailSuccess() {
        MailStructure mailStructure = new MailStructure();
        when(mailService.sendMain(eq("test@example.com"), any(MailStructure.class), eq(false))).thenReturn(true);

        String response = mailController.resetMail("test@example.com", mailStructure);
        assertEquals("Mail Sent Successfully !!", response);
        verify(mailService, times(1)).sendMain("test@example.com", mailStructure, false);
    }

    @Test
    void testResetMailFailure() {
        MailStructure mailStructure = new MailStructure();
        when(mailService.sendMain(eq("test@example.com"), any(MailStructure.class), eq(false))).thenReturn(false);

        String response = mailController.resetMail("test@example.com", mailStructure);
        assertEquals("Mail Sent Failed!!", response);
        verify(mailService, times(1)).sendMain("test@example.com", mailStructure, false);
    }
}
