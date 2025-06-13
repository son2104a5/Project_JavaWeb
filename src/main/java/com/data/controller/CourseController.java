package com.data.controller;

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
public class CourseController {
    @Autowired
    private CourseService courseService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @GetMapping("/admin/courses")
    public String courses(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "8") int size,
            @RequestParam(value = "name", required = false) String name,
            Model model) {

        int currentPage = page < 1 ? 1 : page;
        int offset = currentPage - 1;

        List<Course> courseList;
        long totalItems;
        if (name != null && !name.trim().isEmpty()) {
            courseList = courseService.findCoursesByName(name, offset, size);
            totalItems = courseService.countCoursesByName(name);
            model.addAttribute("name", name);
        } else {
            courseList = courseService.getCourses(offset, size);
            totalItems = courseService.countCourses();
        }

        int totalPages = (int) Math.ceil((double) totalItems / size);

        model.addAttribute("course", new CourseDTO());
        model.addAttribute("formTitle", "Add new course");
        model.addAttribute("formAction", "/admin/courses/add");
        model.addAttribute("formButtonLabel", "Add");
        model.addAttribute("courses", courseList);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        return "admin/courses/course-list";
    }

    @PostMapping("/admin/courses/add")
    public String addCourse(@ModelAttribute("course") @Valid CourseDTO courseDTO, BindingResult result, Model model) throws IOException {
        if (checkValidation(result, model, courseDTO)) {
            System.out.println("Validation errors: " + result.getAllErrors());
            model.addAttribute("hasErrors", true);
            model.addAttribute("formTitle", "Add new course");
            model.addAttribute("formButtonLabel", "Add");
            return "admin/courses/course-list";
        }
        if (!courseService.getCoursesByName(courseDTO.getName()).isEmpty()) {
            result.rejectValue("name", "error.name", "Tên môn học đã tồn tại");
            model.addAttribute("formTitle", "Add new course");
            model.addAttribute("formButtonLabel", "Add");
            model.addAttribute("hasErrors", true);
            return "admin/courses/course-list";
        }
        if (courseDTO.getImage() != null && !courseDTO.getImage().isEmpty()) {
            String imageUrl = cloudinaryService.uploadFile(courseDTO.getImage());
            courseDTO.setImageUrl(imageUrl);
        }

        courseService.save(courseDTO);
        return "redirect:/admin/courses";
    }

    @PostMapping("/admin/courses/update/{id}")
    public String updateCourse(
            @PathVariable("id") int id,
            @ModelAttribute("course") @Valid CourseDTO courseDTO,
            BindingResult result,
            Model model) throws IOException {
        if (checkValidation(result, model, courseDTO)) {
            model.addAttribute("formTitle", "Update course");
            model.addAttribute("formButtonLabel", "Update");
            return "admin/courses/course-list";
        }
        List<Course> matchedCourses = courseService.getCoursesByName(courseDTO.getName());
        boolean isDuplicate = matchedCourses.stream()
                .anyMatch(c -> c.getId() != id);

        if (isDuplicate) {
            result.rejectValue("name", "error.name", "Tên môn học đã tồn tại");
            model.addAttribute("formTitle", "Update course");
            model.addAttribute("formButtonLabel", "Update");
            model.addAttribute("hasErrors", true);
            return "admin/courses/course-list";
        }

        if (courseDTO.getImage() != null && !courseDTO.getImage().isEmpty()) {
            String imageUrl = cloudinaryService.uploadFile(courseDTO.getImage());
            courseDTO.setImageUrl(imageUrl);
        }
        courseDTO.setId(id);
        courseService.update(courseDTO);
        return "redirect:/admin/courses";
    }

    @PostMapping("/admin/courses/delete/{id}")
    public String deleteCourse(@PathVariable("id") int id) {
        CourseDTO courseDTO = courseService.getCourseById(id);
        if (courseDTO != null) {
            courseDTO.setStatus(false);
            courseService.update(courseDTO);
        }
        return "redirect:/admin/courses";
    }


    public boolean checkValidation(BindingResult result, Model model, CourseDTO courseDTO) {
        if (result.hasErrors()) {
            model.addAttribute("course", courseDTO); // Giữ dữ liệu đã nhập
            model.addAttribute("hasErrors", true); // Thêm flag báo có lỗi
            // Thêm các attribute để hiển thị danh sách courses
            int page = 1, size = 8;
            List<Course> courseList = courseService.getCourses(page - 1, size);
            long totalItems = courseService.countCourses();
            int totalPages = (int) Math.ceil((double) totalItems / size);
            model.addAttribute("courses", courseList);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
            return true;
        }
        return false;
    }
}
