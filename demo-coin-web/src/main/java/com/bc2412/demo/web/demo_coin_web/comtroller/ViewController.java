package com.bc2412.demo.web.demo_coin_web.comtroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;
// @RestController // return JSON as response
@Controller
public class ViewController {
  

  @GetMapping(value = "/bootcamp")
  public String sayHelloPage(Model model){
    model.addAttribute("tutor", "vincent");
    return "hello"; // html file name
  }
}
