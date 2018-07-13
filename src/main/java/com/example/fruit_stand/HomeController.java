package com.example.fruit_stand;

import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;

@Controller
public class HomeController {

    @Autowired
    FruitRepository fruitRepository;

    @Autowired
    CloudinaryConfig cloudc;


    @RequestMapping("/")
    public String home(Model model) {
        model.addAttribute("fruits", fruitRepository.findAll());
        return "home";
    }

    @PostMapping("/process")
    public String fruitform(@ModelAttribute("fruit") Fruit fruit, @RequestParam("file")MultipartFile file){
        if (file.isEmpty()){
            return "redirect:/process";
        }
        try{
            Map uploadResult= cloudc.upload(file.getBytes(),
                    ObjectUtils.asMap("resourcetype", "auto"));
            fruit.setImg(uploadResult.get("url").toString());
            fruitRepository.save(fruit);
        }catch (IOException e){
            e.printStackTrace();
            return "redirect:/process";
        }return "redirect:/";
    }

    @RequestMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("fruits", fruitRepository.findAll());
        return "adminhome";
    }

    @RequestMapping("detail/{id}")
    public String showDay(@PathVariable("id") long id, Model model){
        model.addAttribute("fruit", fruitRepository.findById(id).get());
        return "customerpage";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("login", new Login());
        return "adminlogin";
    }

    @PostMapping("/login")
    public String processLogin(HttpServletRequest request, Model model) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        //if (username != "Admin" && password != "password") {
        if (true){
            return "adminlogin";
        }
        return "redirect:/admin";
    }

    @RequestMapping("update/{id}")
    public String updateDay(@PathVariable("id") long id, Model model){
        model.addAttribute("fruit", fruitRepository.findById(id));
        return "fruitform";
    }

//
//    @GetMapping("/signup")
//    public String signUpForm(Model model) {
//        model.addAttribute("login", new Login());
//        return "signup";
//    }
//
//    @PostMapping("/processSignUp")
//    public String processSignUp(@ModelAttribute("login") Login login){
//        loginRepository.save(login);
//        return "redirect:/";
//    }
//
//    @GetMapping("/login")
//    public String loginform(Model model) {
//        model.addAttribute("login", new Login());
//        return "loginpage";
//    }

//    @PostMapping("/processlogin")
//    public String login(HttpServletRequest request, Model model) {
//        String page="/login";
//        String username= request.getParameter("username");
//        String password=request.getParameter("password");
//        long count =loginRepository.countByUsernameandPassword(username,password);
//
//        if (count>0){
//            page =  "adminpage";
//        }
//        else{
//            page="redirect:/login";
//        }
//
//        return page;
//    }

//    @PostMapping("/processLogin")
//    public String show() {
//        return "home";
//    }


}


