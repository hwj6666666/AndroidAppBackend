package org.example.jiaoji.Mail.Controller;

import org.example.jiaoji.Mail.Model.MailStructure;
import org.example.jiaoji.Mail.Service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/mail")
public class MailController {

  @Autowired private MailService mailService;

  @PostMapping("/send/{mail}")
  public String sendMail(@PathVariable String mail, @RequestBody MailStructure mailStructure) {

    System.out.println("Begin mailing!!");
    mailService.sendMain(mail, mailStructure);
    System.out.println("Mail Sent Successfully!!");
    return "Mail Sent Successfully !!";
  }
}
