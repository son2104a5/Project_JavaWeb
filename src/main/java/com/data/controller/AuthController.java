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
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private StudentService studentService;

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("student", new StudentDTO());
        return "/auth/register";
    }

    @PostMapping("/register")
    public String registerConfirm(@Valid @ModelAttribute("student") StudentDTO studentDTO, BindingResult result) {
        if (result.hasErrors()) {
            return "/auth/register";
        }
        if (studentService.findStudentByEmail(studentDTO.getEmail()) != null) {
            if (result.hasFieldErrors("email")) {
                result.rejectValue("email", "error.email", "Email đã tồn tại");
            }
            return "/auth/register";
        }
        if (studentService.findStudentByPhone(studentDTO.getPhone()) != null) {
            result.rejectValue("phone", "error.phone", "Số điện thoại đã tồn tại");
            return "/auth/register";
        }
        studentService.save(studentDTO);
        return "redirect:/auth/login";
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("student", new LoginDTO());
        return "/auth/login";
    }

    @PostMapping("/login")
    public String loginConfirm(@ModelAttribute("student") @Valid LoginDTO loginDTO, BindingResult result, HttpSession session) {
        if (result.hasErrors()) {
            return "/auth/login";
        }
        StudentDTO existingStudent = studentService.findStudentByEmail(loginDTO.getEmail());
        if (existingStudent == null || !existingStudent.getPassword().equals(loginDTO.getPassword())) {
            result.rejectValue("password", "error.password",  "Email hoặc mật khẩu không đúng");
            return "/auth/login";
        }
        if (!existingStudent.getStatus()) {
            result.rejectValue("password", "error.password", "Tài khoản của bạn đã bị khóa");
            return "/auth/login";
        }
        session.setAttribute("user", loginDTO);
        if (existingStudent.getRole()) {
            return "redirect:/admin/dashboard";
        } else {
            return "redirect:/home";
        }
    }

    @GetMapping("/logout")
    public String logout(Model model, HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/auth/login";
    }
}
