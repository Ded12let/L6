package com.example.controller;

import com.example.model.Professor;
import com.example.model.User;
import com.example.repository.ProfessorRepository;
import com.example.repository.UserRepository;
import com.example.service.AuthService;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final ProfessorRepository professorRepository;

    public AuthController(AuthService authService,
                          UserRepository userRepository,
                          ProfessorRepository professorRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
        this.professorRepository = professorRepository;
    }

    // === Вход ===
    @GetMapping("/login")
    public String loginForm(Model model,
                            @RequestParam(name = "error", required = false) String error,
                            @RequestParam(name = "success", required = false) String success) {
        if (error != null) model.addAttribute("error", true);
        if (success != null) model.addAttribute("success", true);
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String login,
                        @RequestParam String password,
                        HttpSession session) {
        if (authService.authenticate(login, password)) {
            session.setAttribute("user", login);
            session.setAttribute("role", authService.getRole(login));
            String role = authService.getRole(login);
            return role.equals("admin") ? "redirect:/admin" : "redirect:/user";
        } else {
            return "redirect:/login?error";
        }
    }

    // === Регистрация ===
    @GetMapping("/register")
    public String showRegisterForm(Model model,
                                   @RequestParam(name = "error", required = false) String error) {
        if (error != null) model.addAttribute("error", true);
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String login,
                               @RequestParam String password) {
        if (authService.userExists(login)) {
            return "redirect:/register?error";
        }
        authService.registerNewUser(login, password);
        return "redirect:/login?success";
    }

    // === Панель администратора ===
    @GetMapping("/admin")
    public String adminPanel(Model model, HttpSession session) {
        if (!"admin".equals(session.getAttribute("role"))) return "redirect:/login";

        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "admin-panel";
    }

    // === Панель пользователя (профессора) ===
    @GetMapping("/user")
    public String userPanel(Model model, HttpSession session) {
        if (!"user".equals(session.getAttribute("role"))) return "redirect:/login";

        List<Professor> professors = professorRepository.findAll();
        model.addAttribute("professors", professors);
        return "user-panel";
    }

    // === Выход ===
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}