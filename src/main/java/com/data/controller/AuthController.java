package com.data.controller;

import com.data.dto.StudentDTO;
import com.data.entity.Student;
import com.data.repository.StudentRepository;
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
    private StudentRepository studentRepository;

    @GetMapping("admin/register")
    public String registerAdminForm(Model model) {
        model.addAttribute("student", new StudentDTO());
        return "admin/auth/register";
    }

    @PostMapping("admin/register")
    public String registerAdminConfirm(@Valid @ModelAttribute("student") StudentDTO studentDTO, BindingResult result) {
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            return "admin/auth/register";
        }
        if (studentRepository.findStudentByUsername(studentDTO.getUsername()) != null) {
            result.rejectValue("username", "error.username", "Tên tài khoản đã tồn tại");
            return "admin/auth/register";
        }
        studentDTO.setRole(true);
        studentRepository.save(studentDTO);
        return "admin/auth/login";
    }

    @GetMapping("admin/logout")
    public String logoutAdmin(Model model, HttpSession session) {
        session.removeAttribute("user");
        return "admin/auth/login";
    }

    @GetMapping("admin/login")
    public String loginAdminForm(Model model) {
        model.addAttribute("student", new StudentDTO());
        return "admin/auth/login";
    }

    @PostMapping("admin/login")
    public String loginAdminConfirm(@ModelAttribute("student") StudentDTO studentDTO, BindingResult result, HttpSession session) {
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            return "admin/auth/login";
        }
        Student existingStudent = studentRepository.findStudentByUsername(studentDTO.getUsername());
        if (existingStudent == null || !existingStudent.getPassword().equals(studentDTO.getPassword())) {
            result.rejectValue("username", "error.username", "Tên tài khoản hoặc mật khẩu không đúng");
            return "admin/auth/login";
        }
        session.setAttribute("user", studentDTO);
        return "admin/dashboard";
    }
}
