package org.example.jiaoji.controller;

import org.example.jiaoji.pojo.MailStructure;
import org.example.jiaoji.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/mail")
public class MailController {

  @Autowired private MailService mailService;

  @PostMapping("/send/{mail}")
  public String sendMail(@PathVariable String mail, @RequestBody MailStructure mailStructure) {

    System.out.println("Begin register mailing!!");
    if (mailService.sendMain(mail, mailStructure,true)) {
      System.out.println("Mail Sent Successfully!!");
      return "Mail Sent Successfully !!";
    }
    return "Mail Sent Failed!!";
  }

  @PostMapping("/reset/{mail}")
  public String resetMail(@PathVariable String mail, @RequestBody MailStructure mailStructure) {

    System.out.println("Begin reset mailing!!");
    if (mailService.sendMain(mail, mailStructure, false)) {
      System.out.println("Mail Sent Successfully!!");
      return "Mail Sent Successfully !!";
    }
    return "Mail Sent Failed!!";
  }
}
