package com.data.controller.admin;

import com.data.entity.Student;
import com.data.service.students.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/admin/students")
public class StudentControllerAdmin {
    @Autowired
    private StudentService studentService;

    @GetMapping
    public String listStudents(Model model,
                               HttpSession session,
                               @RequestParam(value = "page", defaultValue = "1") int page,
                               @RequestParam(value = "size", defaultValue = "8") int size,
                               @RequestParam(value = "sort", required = false) String sort,
                               @RequestParam(value = "name", required = false) String name,
                               @RequestParam(value = "lockId", required = false) Integer lockId) {
        // Check if user is logged in
        if (session.getAttribute("user") == null) {
            return "redirect:/auth/login";
        }
        // Check if user has admin role
        if (!"admin".equals(session.getAttribute("role"))) {
            return "/templates/error/403";
        }

        long totalStudents = studentService.countTotalStudents();
        int totalPages = (int) Math.ceil((double) totalStudents / size);

        List<Student> students = studentService.getStudentsByPage(page, size, sort, name);

        model.addAttribute("students", students);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("sort", sort);
        model.addAttribute("name", name);

        if (lockId != null) {
            Student student = studentService.findStudentById(lockId);
            model.addAttribute("isLock", true);
            model.addAttribute("student", student);
            model.addAttribute("message", student.getStatus() ? "Bạn có chắc chắn muốn khóa tài khoản này không?" : "Bạn có chắc chắn muốn mở khóa tài khoản này không?");
            model.addAttribute("button", student.getStatus() ? "Khóa" : "Mở khóa");
        }

        return "admin/students/student-list";
    }

    @PostMapping("/lock/{id}")
    public String lockStudentAccount(@PathVariable("id") int id, HttpSession session) {
        if (session.getAttribute("user") == null)
            return "redirect:/auth/login";

        studentService.lockAccount(id);
        return "redirect:/admin/students";
    }
}
