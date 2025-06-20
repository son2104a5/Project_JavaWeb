package com.data.controller.user;

import com.data.dto.ChangePasswordDTO;
import com.data.dto.LoginDTO;
import com.data.dto.StudentDTO;
import com.data.service.students.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    private StudentService studentService;

    @GetMapping
    public String profilePage(Model model, HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/auth/login";
        }
        String email = ((LoginDTO) session.getAttribute("user")).getEmail();
        StudentDTO student = studentService.findStudentByEmail(email);
        model.addAttribute("student", student != null ? student : new StudentDTO());
        model.addAttribute("isForm", false);
        return "user/profile";
    }

    @PostMapping("/update")
    public String updateProfile(@Valid @ModelAttribute("student") StudentDTO studentDTO, BindingResult result, HttpSession session) {
        String currentEmail = ((LoginDTO) session.getAttribute("user")).getEmail();
        StudentDTO existing = studentService.findStudentByEmail(currentEmail);
        studentDTO.setId(existing.getId()); // Set ID
        studentDTO.setPassword(existing.getPassword()); // Giữ password cũ

        // Bỏ qua lỗi validate cho password
        if (result.hasErrors()) {
            List<FieldError> errorsToKeep = new ArrayList<>();
            for (FieldError error : result.getFieldErrors()) {
                if (!error.getField().equals("password")) {
                    errorsToKeep.add(error);
                }
            }
            // Log lỗi để debug
            errorsToKeep.forEach(error -> System.out.println("Error: " + error.getField() + " - " + error.getDefaultMessage()));

            if (!errorsToKeep.isEmpty()) {
                return "user/profile";
            }
        }

        // Kiểm tra trùng email
        if (!existing.getEmail().equals(studentDTO.getEmail())) {
            result.rejectValue("email", "error.email", "Email đã tồn tại");
            return "user/profile";
        }

        // Kiểm tra trùng số điện thoại
        StudentDTO existingPhone = studentService.findStudentByPhone(studentDTO.getPhone());
        if (existingPhone != null && !existingPhone.getEmail().equals(studentDTO.getEmail())) {
            result.rejectValue("phone", "error.phone", "Số điện thoại đã tồn tại");
            return "user/profile";
        }

        studentService.update(studentDTO);
        return "redirect:/profile";
    }

    @GetMapping("/change-password")
    public String changePasswordForm(Model model, HttpSession session) {
        String email = ((LoginDTO) session.getAttribute("user")).getEmail();
        StudentDTO student = studentService.findStudentByEmail(email);
        model.addAttribute("student", student != null ? student : new StudentDTO());
        model.addAttribute("isForm", true);
        model.addAttribute("password", new ChangePasswordDTO());
        return "user/profile";
    }

    @PostMapping("/change-password")
    public String changePassword(@Valid @ModelAttribute("password") ChangePasswordDTO changePasswordDTO, BindingResult result, HttpSession session, Model model) {
        String email = ((LoginDTO) session.getAttribute("user")).getEmail();
        StudentDTO student = studentService.findStudentByEmail(email);

        if (result.hasErrors()) {
            model.addAttribute("student", student != null ? student : new StudentDTO());
            model.addAttribute("isForm", true);
            return "user/profile";
        }

        // Kiểm tra mật khẩu cũ
        if (!student.getPassword().equals(changePasswordDTO.getOldPassword())) {
            result.rejectValue("oldPassword", "error.oldPassword", "Mật khẩu cũ không đúng");
            model.addAttribute("student", student);
            model.addAttribute("isForm", true);
            return "user/profile";
        }
        //Kiểm tra mật khẩu mới
        if (changePasswordDTO.getNewPassword().equals(changePasswordDTO.getOldPassword())) {
            model.addAttribute("student", student);
            model.addAttribute("isForm", true);
            result.rejectValue("newPassword", "error.newPassword", "Mật khẩu mới không được trùng với mật khẩu cũ");
            return "user/profile";
        }
        // Kiểm tra trùng lặp mật khẩu mới
        if (!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmNewPassword())) {
            model.addAttribute("student", student);
            model.addAttribute("isForm", true);
            result.rejectValue("confirmNewPassword", "error.confirmNewPassword", "Mật khẩu mới không khớp");
            return "user/profile";
        }
        // Cập nhật mật khẩu mới
        student.setPassword(changePasswordDTO.getNewPassword());
        studentService.update(student);

        return "redirect:/profile";
    }
}