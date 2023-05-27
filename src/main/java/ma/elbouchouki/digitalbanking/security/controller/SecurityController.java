package ma.elbouchouki.digitalbanking.security.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import ma.elbouchouki.digitalbanking.security.config.SecurityConstant;
import ma.elbouchouki.digitalbanking.security.dto.AuthRequest;
import ma.elbouchouki.digitalbanking.security.dto.AuthResponse;
import ma.elbouchouki.digitalbanking.security.entities.AppUser;
import ma.elbouchouki.digitalbanking.security.exception.InvalidTokenException;
import ma.elbouchouki.digitalbanking.security.service.SecurityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth")
public class SecurityController {
    private SecurityService securityService;

    @PostMapping("/sign-in")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(securityService.authenticate(request));
    }

    @PostMapping("/sign-up")
    public ResponseEntity<AppUser> signUp(@RequestBody AppUser appUser) {
        return ResponseEntity.ok(securityService.addNewUser(appUser));
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String jwt_token = request.getHeader(SecurityConstant.HEADER_STRING);
        if (jwt_token == null || !jwt_token.startsWith(SecurityConstant.TOKEN_PREFIX)) {
            throw new InvalidTokenException();
        }
        return ResponseEntity.ok(
                securityService.refreshToken(jwt_token)
        );
    }

    @GetMapping("/profile")
    public ResponseEntity<AppUser> getUser(Principal principal) {
        System.out.println("principal = " + principal);
        System.out.println("principal.getName() = " + principal.getName());
        AppUser user = securityService.loadUserByUsername(principal.getName());
        System.out.println("user = " + user);
        return ResponseEntity.ok(
                user
        );
    }
}
