package ma.elbouchouki.digitalbanking.security.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.elbouchouki.digitalbanking.security.config.SecurityConstant;
import ma.elbouchouki.digitalbanking.security.dto.AuthRequest;
import ma.elbouchouki.digitalbanking.security.dto.AuthResponse;
import ma.elbouchouki.digitalbanking.security.entities.AppRole;
import ma.elbouchouki.digitalbanking.security.entities.AppUser;
import ma.elbouchouki.digitalbanking.security.exception.InvalidCredentialsException;
import ma.elbouchouki.digitalbanking.security.repositories.AppRoleRepository;
import ma.elbouchouki.digitalbanking.security.repositories.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;


@Service
@Slf4j
@Transactional
@AllArgsConstructor
public class SecurityServiceImpl implements SecurityService {
    private AppUserRepository userRepository;
    private AppRoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @Override
    public AppUser addNewUser(AppUser appUser) {
        appUser.setId(UUID.randomUUID().toString());
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        return userRepository.save(appUser);
    }

    @Override
    public void addNewRole(AppRole appRole) {
        roleRepository.save(appRole);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        AppUser appUser = userRepository.findByUsername(username);
        AppRole appRole = roleRepository.findByRoleName(roleName);
        appUser.getRoles().add(appRole);
    }


    @Override
    public AppUser loadUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public AppUser loadUserById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new RuntimeException("User not found")
        );
    }

    @Override
    public AppUser loadUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public AppUser update(AppUser appUser) {
        AppUser user = userRepository.findByUsername(appUser.getUsername());
        user.setPassword(passwordEncoder.encode(appUser.getPassword()));
        user.setEmail(appUser.getEmail());
        return userRepository.save(user);
    }

    @Override
    public List<AppUser> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<AppRole> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public AppRole findRoleByRoleName(String roleName) {
        return roleRepository.findByRoleName(roleName);
    }


    @Override
    public AuthResponse authenticate(AuthRequest authRequest) {
        String username = authRequest.username();
        String password = authRequest.password();
        AppUser user = userRepository.findByUsername(username);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException();
        }
        String accessToken = generateAccessToken(user);
        String refreshToken = generateRefreshToken(user);
        return new AuthResponse(accessToken, refreshToken);
    }

    @Override
    public AuthResponse refreshToken(String refreshToken) {
        System.out.println("here");
        String userId = JWT.require(Algorithm.HMAC256(SecurityConstant.SECRET)).build().verify(refreshToken).getSubject();
        AppUser user = userRepository.findById(Long.valueOf(userId)).orElseThrow(
                () -> new RuntimeException("User not found")
        );
        String newAccessToken = generateAccessToken(user);
        return new AuthResponse(newAccessToken, refreshToken);
    }


    public String generateAccessToken(AppUser user) {
        return JWT.create()
                .withSubject(user.getId())
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstant.ACCESS_EXPIRATION_TIME))
                .withClaim("roles", user.getRoles()
                        .stream()
                        .map(
                                AppRole::getRoleName
                        ).toList())
                .sign(Algorithm.HMAC256(SecurityConstant.SECRET));
    }

    public String generateRefreshToken(AppUser user) {
        return JWT.create()
                .withSubject(user.getId())
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstant.REFRESH_TOKEN_EXPIRATION_TIME))
                .sign(Algorithm.HMAC256(SecurityConstant.SECRET));
    }

}
