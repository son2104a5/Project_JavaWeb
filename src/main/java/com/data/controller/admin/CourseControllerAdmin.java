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
            Model model) {
        int currentPage = page < 1 ? 1 : page;
        int offset = currentPage - 1;

        List<Course> courseList = courseService.getCourses(offset, size, name, status, sort);
        long totalItems = courseService.countCourses(name, status);
        int totalPages = (int) Math.ceil((double) totalItems / size);

        model.addAttribute("course", new CourseDTO());
        model.addAttribute("courses", courseList);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("name", name);
        model.addAttribute("status", status);
        model.addAttribute("sort", sort);
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
        int currentPage = page < 1 ? 1 : page;
        int offset = currentPage - 1;

        List<Course> courseList;
        long totalItems;
        if (name != null && !name.trim().isEmpty()) {
            courseList = courseService.findCoursesByName(name, offset, size);
            totalItems = courseService.countCoursesByName(name);
        } else {
            courseList = courseService.getCourses(page, size, name, status, sort);
            totalItems = courseService.countCourses(name, status);
        }

        int totalPages = (int) Math.ceil((double) totalItems / size);

        model.addAttribute("course", new CourseDTO());
        model.addAttribute("formAction", "/admin/courses/add");
        model.addAttribute("formTitle", "Add New Course");
        model.addAttribute("formButtonLabel", "Add");
        model.addAttribute("isAdd", true);
        model.addAttribute("isEdit", false);
        model.addAttribute("isDelete", false);
        model.addAttribute("courses", courseList);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
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
        int currentPage = page < 1 ? 1 : page;
        int offset = currentPage - 1;

        List<Course> courseList;
        long totalItems;
        if (name != null && !name.trim().isEmpty()) {
            courseList = courseService.findCoursesByName(name, offset, size);
            totalItems = courseService.countCoursesByName(name);
        } else {
            courseList = courseService.getCourses(page, size, name, status, sort);
            totalItems = courseService.countCourses(name, status);
        }

        int totalPages = (int) Math.ceil((double) totalItems / size);

        if (result.hasErrors() || !courseService.getCoursesByName(courseDTO.getName()).isEmpty()) {
            model.addAttribute("formAction", "/admin/courses/add");
            model.addAttribute("formTitle", "Add New Course");
            model.addAttribute("formButtonLabel", "Add");
            model.addAttribute("isAdd", true);
            model.addAttribute("isEdit", false);
            model.addAttribute("isDelete", false);
            model.addAttribute("courses", courseList);
            model.addAttribute("currentPage", currentPage);
            model.addAttribute("totalPages", totalPages);
            return "/admin/courses/course-list";
        }

        if (courseDTO.getImage() != null && !courseDTO.getImage().isEmpty()) {
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
            @RequestParam(value = "sort", required = false) String sort) {
        CourseDTO courseDTO = courseService.getCourseById(id);
        if (courseDTO == null) {
            model.addAttribute("isAdd", false);
            model.addAttribute("isEdit", false);
            model.addAttribute("isDelete", false);
            return courses(page, size, name, status, sort, model);
        }

        int currentPage = page < 1 ? 1 : page;
        int offset = currentPage - 1;

        List<Course> courseList;
        long totalItems;
        if (name != null && !name.trim().isEmpty()) {
            courseList = courseService.findCoursesByName(name, offset, size);
            totalItems = courseService.countCoursesByName(name);
        } else {
            courseList = courseService.getCourses(page, size, name, status, sort);
            totalItems = courseService.countCourses(name, status);
        }

        int totalPages = (int) Math.ceil((double) totalItems / size);

        model.addAttribute("course", courseDTO);
        model.addAttribute("formAction", "/admin/courses/update/" + id);
        model.addAttribute("formTitle", "Update Course");
        model.addAttribute("formButtonLabel", "Update");
        model.addAttribute("isAdd", false);
        model.addAttribute("isEdit", true);
        model.addAttribute("isDelete", false);
        model.addAttribute("courses", courseList);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
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
        int currentPage = page < 1 ? 1 : page;
        int offset = currentPage - 1;

        List<Course> courseList;
        long totalItems;
        if (name != null && !name.trim().isEmpty()) {
            courseList = courseService.findCoursesByName(name, offset, size);
            totalItems = courseService.countCoursesByName(name);
        } else {
            courseList = courseService.getCourses(page, size, name, status, sort);
            totalItems = courseService.countCourses(name, status);
        }

        int totalPages = (int) Math.ceil((double) totalItems / size);

        List<Course> matchedCourses = courseService.getCoursesByName(courseDTO.getName());
        boolean isDuplicate = matchedCourses.stream().anyMatch(c -> c.getId() != id);

        if (result.hasErrors() || isDuplicate) {
            model.addAttribute("formAction", "/admin/courses/update/" + id);
            model.addAttribute("formTitle", "Update Course");
            model.addAttribute("formButtonLabel", "Update");
            model.addAttribute("isAdd", false);
            model.addAttribute("isEdit", true);
            model.addAttribute("isDelete", false);
            model.addAttribute("courses", courseList);
            model.addAttribute("currentPage", currentPage);
            model.addAttribute("totalPages", totalPages);
            return "/admin/courses/course-list";
        }

        if (courseDTO.getImage() != null && !courseDTO.getImage().isEmpty()) {
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
            Model model,
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
            return courses(page, size, name, status, sort, model);
        }

        int currentPage = page < 1 ? 1 : page;
        int offset = currentPage - 1;

        List<Course> courseList;
        long totalItems;
        if (name != null && !name.trim().isEmpty()) {
            courseList = courseService.findCoursesByName(name, offset, size);
            totalItems = courseService.countCoursesByName(name);
        } else {
            courseList = courseService.getCourses(page, size, name, status, sort);
            totalItems = courseService.countCourses(name, status);
        }

        int totalPages = (int) Math.ceil((double) totalItems / size);

        model.addAttribute("course", new CourseDTO());
        model.addAttribute("deleteCourseId", id);
        model.addAttribute("isAdd", false);
        model.addAttribute("isEdit", false);
        model.addAttribute("isDelete", true);
        model.addAttribute("courses", courseList);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
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
}