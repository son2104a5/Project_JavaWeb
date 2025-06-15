package com.data.controller;

import com.data.dto.LoginDTO;
import com.data.dto.StudentDTO;
import com.data.entity.Student;
import com.data.service.students.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class AuthController {
    @Autowired
    private StudentService studentService;

    @GetMapping("/admin/register")
    public String registerAdminForm(Model model) {
        model.addAttribute("student", new StudentDTO());
        return "/admin/auth/register";
    }

    @PostMapping("/admin/register")
    public String registerAdminConfirm(@Valid @ModelAttribute("student") StudentDTO studentDTO, BindingResult result) {
        if (result.hasErrors()) {
            return "/admin/auth/register";
        }
        if (studentService.findStudentByUsernameOrEmail(studentDTO.getUsername(), studentDTO.getEmail()) != null) {
            if (result.hasFieldErrors("username")) {
                result.rejectValue("username", "error.username", "Tên tài khoản đã tồn tại");
            } else if (result.hasFieldErrors("email")) {
                result.rejectValue("email", "error.email", "Email đã tồn tại");
            }
        }
        studentDTO.setRole(true);
        studentService.save(studentDTO);
        return "redirect:/admin/login";
    }

    @GetMapping("/admin/logout")
    public String logoutAdmin(Model model, HttpSession session) {
        session.removeAttribute("user");
        return "/admin/auth/login";
    }

    @GetMapping("admin/login")
    public String loginAdminForm(Model model) {
        model.addAttribute("student", new LoginDTO());
        return "/admin/auth/login";
    }

    @PostMapping("admin/login")
    public String loginAdminConfirm(@ModelAttribute("student") @Valid LoginDTO loginDTO, BindingResult result, HttpSession session) {
        if (result.hasErrors()) {
            return "/admin/auth/login";
        }
        Student existingStudent = studentService.findStudentByUsernameOrEmail(loginDTO.getUsername(), loginDTO.getEmail());
        if (existingStudent == null || !existingStudent.getPassword().equals(loginDTO.getPassword())) {
            result.rejectValue("password", "error.password",  "Email hoặc mật khẩu không đúng");
            return "/admin/auth/login";
        }
        session.setAttribute("user", loginDTO);
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/user/register")
    public String registerUserForm(Model model) {
        model.addAttribute("student", new StudentDTO());
        return "/user/auth/register";
    }

    @PostMapping("/user/register")
    public String registerUserConfirm(@Valid @ModelAttribute("student") StudentDTO studentDTO, BindingResult result) {
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            return "/user/auth/register";
        }
        if (studentService.findStudentByUsernameOrEmail(studentDTO.getUsername(), studentDTO.getEmail()) != null) {
            if (result.hasFieldErrors("username")) {
                result.rejectValue("username", "error.username", "Tên tài khoản đã tồn tại");
            } else if (result.hasFieldErrors("email")) {
                result.rejectValue("email", "error.email", "Email đã tồn tại");
            }
        }
        studentDTO.setRole(false);
        studentService.save(studentDTO);
        return "redirect:/user/login";
    }

    @GetMapping("/user/login")
    public String loginUserForm(Model model) {
        model.addAttribute("student", new LoginDTO());
        return "/user/auth/login";
    }

    @PostMapping("/user/login")
    public String loginUserConfirm(@ModelAttribute("student") LoginDTO loginDTO, BindingResult result, HttpSession session) {
        if (result.hasErrors()) {
            return "/user/auth/login";
        }
        Student existingStudent = studentService.findStudentByUsernameOrEmail(loginDTO.getUsername(), loginDTO.getEmail());
        if (existingStudent == null || !existingStudent.getPassword().equals(loginDTO.getPassword())) {
            result.rejectValue("password", "error.password",  "Email hoặc mật khẩu không đúng");
            return "/user/auth/login";
        }
        session.setAttribute("user", loginDTO);
        return "redirect:/home";
    }

    @GetMapping("/user/logout")
    public String logoutUser(Model model, HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/user/login";
    }
}
