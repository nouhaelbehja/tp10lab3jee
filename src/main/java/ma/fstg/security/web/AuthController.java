package ma.fstg.security.web;

import java.util.Map;
import ma.fstg.security.model.StudentProfile;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class AuthController {
    private final Map<String, StudentProfile> profiles = Map.of(
            "admin", new StudentProfile("Responsable pédagogique", "admin@example.com", "Administration", "ADM-001"),
            "user", new StudentProfile("Nouha", "nouha@example.com", "Génie informatique", "ETU-2026-042"));

    @ModelAttribute
    public void connectedUser(Authentication authentication, Model model) {
        if (authentication != null) {
            model.addAttribute("username", authentication.getName());
            model.addAttribute("profile", profiles.get(authentication.getName()));
            model.addAttribute("isAdmin", authentication.getAuthorities().stream()
                    .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN")));
        }
    }

    @GetMapping("/login")
    public String login() { return "login"; }

    @GetMapping({"/", "/home"})
    public String home() { return "home"; }

    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model) {
        model.addAttribute("profiles", profiles);
        return "admin-dashboard";
    }

    @GetMapping("/user/dashboard")
    public String userDashboard() { return "user-dashboard"; }

    @GetMapping("/user/profile")
    public String profile() { return "profile"; }

    @GetMapping("/access-denied")
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String accessDenied() { return "access-denied"; }
}
