package com.data.controller.user;

import com.data.dto.LoginDTO;
import com.data.dto.StudentDTO;
import com.data.entity.Enrollment;
import com.data.entity.EnrollmentStatus;
import com.data.entity.Student;
import com.data.service.courses.CourseService;
import com.data.service.enrollment.EnrollmentService;
import com.data.service.students.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/enrollment")
public class EnrollmentController {
    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private StudentService studentService;

    @GetMapping
    public String enrollmentPage(
            Model model, HttpSession session,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "8") int size
    ) {
        // Check if user is logged in
        if (session.getAttribute("user") == null) {
            return "redirect:/auth/login";
        }

        // Validate page
        int currentPage = page < 1 ? 1 : page;

        // Get logged-in user's email
        String email = ((LoginDTO) session.getAttribute("user")).getEmail();
        StudentDTO student = studentService.findStudentByEmail(email);

        // Fetch paginated enrollments
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByStudentId(
                student.getId(), sort, name, currentPage, size
        );

        // Count total enrollments
        long totalEnrollments = enrollmentService.countEnrollmentsByStudentId(student.getId(), sort, name);
        int totalPages = (int) Math.ceil((double) totalEnrollments / size);

        // Add attributes to model
        model.addAttribute("sort", sort);
        model.addAttribute("name", name);
        model.addAttribute("size", size);
        model.addAttribute("enrollments", enrollments);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);

        return "user/enrollment-history";
    }

    @PostMapping("/cancel/{id}")
    public String cancelEnrollment(@PathVariable("id") int id,
                                   @RequestParam(defaultValue = "1") int page,
                                   @RequestParam(required = false) String sort,
                                   @RequestParam(required = false) String name,
                                   RedirectAttributes redirectAttributes) {
        try {
            enrollmentService.changeEnrollmentStatus(id, EnrollmentStatus.CANCEL);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to cancel enrollment.");
        }
        return String.format("redirect:/enrollment?page=%d%s%s",
                page,
                sort != null ? "&sort=" + sort : "",
                name != null ? "&name=" + name : "");
    }
}
