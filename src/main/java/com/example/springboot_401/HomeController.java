package com.example.springboot_401;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class HomeController {
    @Autowired
    private UserService userService;

    @Autowired
    CourseRepository courseRepository;

    @RequestMapping("/")
    public String loadCourses(Model model) {
        model.addAttribute("courses", courseRepository.findAll());
        if(userService.getUser() != null){
            model.addAttribute("user_id", userService.getUser().getId());
            model.addAttribute("user", userService.getUser());
        }
        return "list";
    }

    @GetMapping("/add")
    public String addCourse(Model model) {
        model.addAttribute("course", new Course());
        model.addAttribute("user", userService.getUser());
        model.addAttribute("users", userService.userRepository.findAll());
        return "courseform";
    }

    @PostMapping("/process")
    public String process(@Valid Course course, BindingResult result) {
        if (result.hasErrors()) {
            return "courseform";
        }
        courseRepository.save(course);
        return "redirect:/";
    }

    @RequestMapping("/detail/{id}")
    public String showCourse(@PathVariable("id") long id, Model model) {
        model.addAttribute("course", courseRepository.findById(id).get());
        model.addAttribute("user", userService.getUser());
        return "show";
    }

    @RequestMapping("/update/{id}")
    public String updateCourse(@PathVariable("id") long id, Model model) {
        model.addAttribute("course", courseRepository.findById(id).get());
        model.addAttribute("user", userService.getUser());
        return "courseform";
    }

    @RequestMapping("/delete/{id}")
    public String deleteCourse(@PathVariable long id) {
        courseRepository.deleteById(id);
        return "redirect:/";
    }
/*------------------------------------------------------
    SPRING-SECURITY
 ------------------------------------------------------*/
    @GetMapping("/register")
    public String showRegistrationPage(Model model){
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/register")
    public String processRegistration(@Valid @ModelAttribute("user") User user, BindingResult result,
                                      Model model){
        model.addAttribute("user", user);
        if (result.hasErrors()){
            return "registration";
        }else{
            userService.saveUser(user, "USER");
            model.addAttribute("message", "User Account Created");
        }
        return "redirect:/login";
    }

    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    @RequestMapping("/secure")
    public String secure(Model model){
        model.addAttribute("myuser", userService.getUser());
        return "secure";
    }
}
