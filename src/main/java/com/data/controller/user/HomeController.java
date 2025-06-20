package com.data.controller.user;

import com.data.dto.CourseDTO;
import com.data.dto.LoginDTO;
import com.data.dto.StudentDTO;
import com.data.entity.Course;
import com.data.entity.Enrollment;
import com.data.entity.Student;
import com.data.service.CloudinaryService;
import com.data.service.courses.CourseService;
import com.data.service.enrollment.EnrollmentService;
import com.data.service.students.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/home")
public class HomeController {
    @Autowired
    private CourseService courseService;

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private StudentService studentService;

    @GetMapping
    public String home(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "confirmCourseId", required = false) Integer confirmCourseId,
            Model model,
            HttpSession session) {
        int currentPage = page < 1 ? 1 : page;

        List<Course> courseList = courseService.getCourses(page, size, name, null, null);
        long totalItems = courseService.countCourses(name, null);
        int totalPages = (int) Math.ceil((double) totalItems / size);

        Set<Integer> enrolledCourseIds = null;
        if (session.getAttribute("user") != null) {
            String email = ((LoginDTO) session.getAttribute("user")).getEmail();
            StudentDTO student = studentService.findStudentByEmail(email);
            if (student != null) {
                List<Enrollment> enrollments = enrollmentService.getEnrollmentsByStudentId(student.getId());
                enrolledCourseIds = enrollments.stream()
                        .map(e -> e.getCourse().getId())
                        .collect(Collectors.toSet());

                if (confirmCourseId != null && !enrolledCourseIds.contains(confirmCourseId)) {
                    CourseDTO course = courseService.getCourseById(confirmCourseId);
                    if (course != null) {
                        model.addAttribute("courseName", course.getName());
                    }
                }
            }
        }

        model.addAttribute("enrolledCourseIds", enrolledCourseIds);
        model.addAttribute("confirmCourseId", confirmCourseId);
        model.addAttribute("courses", courseList);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("name", name);
        return "user/home";
    }

    @PostMapping("/enroll/{id}")
    public String addEnrollment(@PathVariable int id, HttpSession session) {
        // Check if user is logged in
        if (session.getAttribute("user") == null) {
            return "redirect:/auth/login";
        }

        CourseDTO course = courseService.getCourseById(id);
        if (course == null) {
            return "redirect:/home";
        }
        LoginDTO user = (LoginDTO) session.getAttribute("user");
        String email = user.getEmail();
        StudentDTO student = studentService.findStudentByEmail(email);
        if (student == null) {
            return "redirect:/home";
        }
        enrollmentService.addEnrollment(student.getId(), id);
        return "redirect:/home";
    }
}
