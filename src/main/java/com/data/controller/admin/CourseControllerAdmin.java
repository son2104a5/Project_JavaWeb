package com.data.controller.admin;

import com.data.dto.CourseDTO;
import com.data.entity.Course;
import com.data.service.CloudinaryService;
import com.data.service.courses.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/admin/courses")
public class CourseControllerAdmin {
    @Autowired
    private CourseService courseService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @GetMapping
    public String courses(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "8") int size,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "sort", required = false) String sort,
            Model model, HttpSession session) {
        // Check if user is logged in
        if (session.getAttribute("user") == null) {
            return "redirect:/auth/login";
        }
        // Check if user has admin role
        if (!"admin".equals(session.getAttribute("role"))) {
            return "/templates/error/403";
        }

        reloadPage(page, size, name, status, sort, model);

        model.addAttribute("course", new CourseDTO());
        model.addAttribute("isAdd", false);
        model.addAttribute("isEdit", false);
        model.addAttribute("isDelete", false);
        return "/admin/courses/course-list";
    }

    @GetMapping("/add")
    public String showAddForm(
            Model model,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "8") int size,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "sort", required = false) String sort) {

        reloadPage(page, size, name, status, sort, model);

        model.addAttribute("course", new CourseDTO());
        model.addAttribute("formAction", "/admin/courses/add");
        model.addAttribute("formTitle", "Add New Course");
        model.addAttribute("formButtonLabel", "Add");
        model.addAttribute("isAdd", true);
        model.addAttribute("isEdit", false);
        model.addAttribute("isDelete", false);
        return "/admin/courses/course-list";
    }

    @PostMapping("/add")
    public String addCourse(
            @ModelAttribute("course") @Valid CourseDTO courseDTO,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "8") int size,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "sort", required = false) String sort) throws IOException {

        reloadPage(page, size, name, status, sort, model);

        if (result.hasErrors() || !courseService.getCoursesByName(courseDTO.getName()).isEmpty()) {
            model.addAttribute("formAction", "/admin/courses/add");
            model.addAttribute("formTitle", "Add New Course");
            model.addAttribute("formButtonLabel", "Add");
            model.addAttribute("isAdd", true);
            model.addAttribute("isEdit", false);
            model.addAttribute("isDelete", false);
            return "/admin/courses/course-list";
        }

        if (courseDTO.getImageFile() != null && !courseDTO.getImageFile().isEmpty()) {
            String imageUrl = cloudinaryService.uploadFile(courseDTO.getImageFile());
            courseDTO.setImage(imageUrl);
        }

        courseService.save(courseDTO);
        redirectAttributes.addFlashAttribute("successMessage", "Thêm khóa học thành công!");
        return "redirect:/admin/courses";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(
            @PathVariable("id") int id,
            Model model,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "8") int size,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "sort", required = false) String sort, HttpSession session) {
        CourseDTO courseDTO = courseService.getCourseById(id);
        if (courseDTO == null) {
            model.addAttribute("isAdd", false);
            model.addAttribute("isEdit", false);
            model.addAttribute("isDelete", false);
            return courses(page, size, name, status, sort, model, session);
        }

        reloadPage(page, size, name, status, sort, model);

        model.addAttribute("course", courseDTO);
        model.addAttribute("formAction", "/admin/courses/update/" + id);
        model.addAttribute("formTitle", "Update Course");
        model.addAttribute("formButtonLabel", "Update");
        model.addAttribute("isAdd", false);
        model.addAttribute("isEdit", true);
        model.addAttribute("isDelete", false);
        return "/admin/courses/course-list";
    }

    @PostMapping("/update/{id}")
    public String updateCourse(
            @PathVariable("id") int id,
            @ModelAttribute("course") @Valid CourseDTO courseDTO,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "8") int size,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "sort", required = false) String sort) throws IOException {

        reloadPage(page, size, name, status, sort, model);

        List<Course> matchedCourses = courseService.getCoursesByName(courseDTO.getName());
        boolean isDuplicate = matchedCourses.stream().anyMatch(c -> c.getId() != id);

        if (result.hasErrors() || isDuplicate) {
            model.addAttribute("formAction", "/admin/courses/update/" + id);
            model.addAttribute("formTitle", "Update Course");
            model.addAttribute("formButtonLabel", "Update");
            model.addAttribute("isAdd", false);
            model.addAttribute("isEdit", true);
            model.addAttribute("isDelete", false);
            return "/admin/courses/course-list";
        }

        if (courseDTO.getImageFile() != null && !courseDTO.getImageFile().isEmpty()) {
            String imageUrl = cloudinaryService.uploadFile(courseDTO.getImageFile());
            courseDTO.setImage(imageUrl);
        }
        courseDTO.setId(id);
        courseService.update(courseDTO);
        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật khóa học thành công!");
        return "redirect:/admin/courses";
    }

    @GetMapping("/delete/confirm/{id}")
    public String showDeleteConfirm(
            @PathVariable("id") int id,
            Model model, HttpSession session,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "8") int size,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "sort", required = false) String sort) {
        CourseDTO courseDTO = courseService.getCourseById(id);
        if (courseDTO == null) {
            model.addAttribute("isAdd", false);
            model.addAttribute("isEdit", false);
            model.addAttribute("isDelete", false);
            return courses(page, size, name, status, sort, model, session);
        }

        reloadPage(page, size, name, status, sort, model);

        model.addAttribute("course", courseDTO);
        model.addAttribute("deleteCourseId", id);
        model.addAttribute("isAdd", false);
        model.addAttribute("isEdit", false);
        model.addAttribute("isDelete", true);
        return "/admin/courses/course-list";
    }

    @PostMapping("/delete/{id}")
    public String deleteCourse(
            @PathVariable("id") int id,
            RedirectAttributes redirectAttributes) {
        CourseDTO courseDTO = courseService.getCourseById(id);
        if (courseDTO != null) {
            courseDTO.setStatus(false);
            courseService.update(courseDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Xóa khóa học thành công!");
        }
        return "redirect:/admin/courses";
    }

    @GetMapping("/recovery/{id}")
    public String recoveryCourse(
            @PathVariable("id") int id,
            RedirectAttributes redirectAttributes) {
        CourseDTO courseDTO = courseService.getCourseById(id);
        if (courseDTO != null) {
            courseDTO.setStatus(true);
            courseService.update(courseDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Khôi phục khóa học thành công!");
        }
        return "redirect:/admin/courses";
    }

    public void reloadPage(int page, int size, String name, String status, String sort, Model model) {
        int currentPage = Math.max(page, 1);
        int offset = (currentPage - 1) * size; // Sửa ở đây

        // Xử lý chuỗi rỗng
        name = (name != null && name.trim().isEmpty()) ? null : name;
        status = (status != null && status.trim().isEmpty()) ? null : status;
        sort = (sort != null && sort.trim().isEmpty()) ? null : sort;

        List<Course> courseList = courseService.getCourses(offset, size, name, status, sort);
        long totalItems = courseService.countCourses(name, status);
        int totalPages = (int) Math.ceil((double) totalItems / size);

        model.addAttribute("courses", courseList);
        model.addAttribute("name", name);
        model.addAttribute("status", status);
        model.addAttribute("sort", sort);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
    }
}