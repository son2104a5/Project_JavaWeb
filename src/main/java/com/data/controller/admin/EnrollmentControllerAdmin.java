package com.data.controller.admin;

import com.data.entity.EnrollmentStatus;
import com.data.service.enrollment.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin/enrollments")
public class EnrollmentControllerAdmin {
    @Autowired
    private EnrollmentService enrollmentService;

    @GetMapping
    public String enrollmentPage(
            Model model, HttpSession session,
            @RequestParam(value = "filter", required = false) String filter,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "8") int size
    ) {
        // Check if user is logged in
        if (session.getAttribute("user") == null) {
            return "redirect:/auth/login";
        }

        // Check if user has admin role
        if (!"admin".equals(session.getAttribute("role"))) {
            return "/templates/error/403";
        }
        reloadPage(page, filter, name, model, size);
        return "admin/enrollments/enrollment-list";
    }

    @GetMapping("/confirm/{id}")
    public String confirmEnrollment(
            @PathVariable int id,
            @RequestParam(value = "filter", required = false) String filter,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "8") int size,
            Model model
    ) {
        reloadPage(page, filter, name, model, size);
        enrollmentService.changeEnrollmentStatus(id, EnrollmentStatus.CONFIRM);
        return "redirect:/admin/enrollments";
    }

    @GetMapping("/denied/{id}")
    public String deniedEnrollment(
            @PathVariable int id,
            @RequestParam(value = "filter", required = false) String filter,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "8") int size,
            Model model
    ) {
        reloadPage(page, filter, name, model, size);
        enrollmentService.changeEnrollmentStatus(id, EnrollmentStatus.DENIED);
        return "redirect:/admin/enrollments";
    }

    public void reloadPage(int page, String filter, String name, Model model, int size) {
        int currentPage = Math.max(page, 1);
        int offset = currentPage - 1;

        model.addAttribute("filter", filter);
        model.addAttribute("name", name);
        model.addAttribute("enrollments", enrollmentService.getEnrollmentsByPage(offset, size, filter, name));
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", Math.ceil((double) enrollmentService.countEnrollments(filter, name) / size));
    }
}
