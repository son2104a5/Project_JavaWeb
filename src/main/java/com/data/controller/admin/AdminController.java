package com.data.controller.admin;

import com.data.service.courses.CourseService;
import com.data.service.enrollment.EnrollmentService;
import com.data.service.statistics.StatisticService;
import com.data.service.students.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.net.HttpCookie;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private StudentService studentService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private StatisticService statisticService;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        // Check if user is logged in
        if (session.getAttribute("user") == null) {
            return "redirect:/auth/login";
        }
        // Check if user is admin
        if (!"admin".equals(session.getAttribute("role"))) {
            return "/templates/error/403";
        }

        model.addAttribute("studentCount", studentService.findAllStudents().size());
        model.addAttribute("courseCount", courseService.countCourses(null, String.valueOf(true)));
        model.addAttribute("enrollmentCount", enrollmentService.getAllEnrollments().size());
        model.addAttribute("statistics", statisticService.getStatisticByCourse());
        model.addAttribute("top5Courses", statisticService.top5CoursesByEnrollmentCount());
        return "admin/dashboard";
    }
}
