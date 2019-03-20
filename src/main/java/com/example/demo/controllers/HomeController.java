package com.example.demo.controllers;

import com.cloudinary.utils.ObjectUtils;
import com.example.demo.beans.Cafe;
import com.example.demo.beans.User;
import com.example.demo.configurations.CloudinaryConfig;
import com.example.demo.repositories.CafeRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;

@Controller
public class HomeController {

  private UserService userService;

  @Autowired
  CloudinaryConfig cloudc;

  @Autowired
  CafeRepository cafeRepository;

  @Autowired
  UserRepository userRepository;

  @RequestMapping("/")
  public String index(Model model){
    model.addAttribute("cafes", cafeRepository.findAll());
    return "index";
  }

  @RequestMapping("/login")
  public String login(){
    return "login";
  }

  @RequestMapping(value = "/register", method = RequestMethod.GET)
  public String showRegistrationPage(Model model) {
    model.addAttribute("user", new User());
    return "registration";
  }

  @RequestMapping(value="/register", method=RequestMethod.POST)
  public String processRegistrationPage(@Valid @ModelAttribute("user")
                                                User user, BindingResult
                                                result,
                                        Model model) {
    model.addAttribute("user", new User());
    if (result.hasErrors()) {
      return "registration";
    }
    else {
      userService.saveUser(user);
      model.addAttribute("message", "User Account Successfully Created");
    }
    return "index";
  }

  @GetMapping("/add")
  public String messageForm(Model model){
    model.addAttribute("cafe", new Cafe());
    return "cafeform";
  }

  @PostMapping("/process")
  public String processForm(@Valid @ModelAttribute Cafe cafe, @RequestParam
          ("file")MultipartFile file, BindingResult
          result){
    if(result.hasErrors()){
      return "cafeform";
    }
    cafe.setUser(getUser());
    
    if(file.isEmpty()){
      return "redirect:/add";
    }try {
      Map uploadResult = cloudc.upload(file.getBytes(),
              ObjectUtils.asMap("resourcetype", "auto"));
      cafe.setImage(uploadResult.get("url").toString());
      cafeRepository.save(cafe);
    }catch (IOException e){
      e.printStackTrace();
      return "redirect:/add";
    }
    return "redirect:/";
  }


  @RequestMapping("/detail/{id}")
  public String showMsg(@PathVariable("id") long id, Model model) {
    model.addAttribute("cafe", cafeRepository.findById(id).get());
    model.addAttribute("user_id", getUser().getId());
    return "show";
  }

  @RequestMapping("/update/{id}")
  public String updateMsg(@PathVariable("id") long id, Model model){
    model.addAttribute("cafe", cafeRepository.findById(id).get());
    return "cafeform";
  }



  private User getUser(){
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String currentusername = authentication.getName();
    User user = userRepository.findByUsername(currentusername);
    return user;
  }

}
