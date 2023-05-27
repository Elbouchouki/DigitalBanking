package ma.elbouchouki.digitalbanking.security.service;


import ma.elbouchouki.digitalbanking.security.dto.AuthRequest;
import ma.elbouchouki.digitalbanking.security.dto.AuthResponse;
import ma.elbouchouki.digitalbanking.security.entities.AppRole;
import ma.elbouchouki.digitalbanking.security.entities.AppUser;

import java.util.List;

public interface SecurityService {
    AppUser addNewUser(AppUser appUser);

    AppUser loadUserByUsername(String username);

    AppUser loadUserByEmail(String email);

    AppUser loadUserById(Long id);

    AppUser update(AppUser appUser);

    List<AppUser> getAllUsers();

    void addNewRole(AppRole appRole);

    void addRoleToUser(String username, String roleName);

    List<AppRole> getAllRoles();

    AppRole findRoleByRoleName(String roleName);

    AuthResponse authenticate(AuthRequest authRequest);

    AuthResponse refreshToken(String refreshToken);

}
