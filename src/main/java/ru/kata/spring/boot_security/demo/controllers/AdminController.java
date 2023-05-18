package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.services.AdminService;
import ru.kata.spring.boot_security.demo.services.RoleService;

import java.security.Principal;


@Controller
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;
    private final RoleService roleService;

    @Autowired
    public AdminController(AdminService adminService, RoleService roleService) {
        this.adminService = adminService;
        this.roleService = roleService;
    }

    @GetMapping()
    public String adminPage(Model model, Model role, Principal principal){
        role.addAttribute("rolesList", roleService.getRolesList());
        model.addAttribute("users", adminService.showAllUsers());
        User princ = adminService.getUserByUsername(principal.getName());
        model.addAttribute("princ", princ);
        model.addAttribute("newUser", new User());
        return "adminpage";
    }

    @GetMapping("/user")
    public String userPageInfo(Model model, Principal principal) {
        model.addAttribute("user",adminService.getUserByUsername(principal.getName()));
        return "user";
    }

    @GetMapping("/user/{id}")
    public String userPage(Model model, @PathVariable("id") Long id) {
        model.addAttribute("user",adminService.getUserById(id));
        return "user";
    }

    @GetMapping("/new")
    public String addUser(Model model, Model role) {
        role.addAttribute("rolesList", roleService.getRolesList());
        model.addAttribute("user", new User());
        return "new";
    }

    @PostMapping("/new")
    public String add(@ModelAttribute("user") User user){
        adminService.addUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/{id}/edit")
    public String updateUser(Model model, @PathVariable("id") Long id, Model role) {
        role.addAttribute("rolesList", roleService.getRolesList());
        model.addAttribute("user", adminService.getUserById(id));

        return "edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("user") User user,
                         @PathVariable("id") Long id) {
        adminService.editUser(id, user);

        return "redirect:/admin";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        adminService.deleteUser(id);

        return "redirect:/admin";
    }

}
