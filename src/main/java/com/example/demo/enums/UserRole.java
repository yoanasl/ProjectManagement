package com.example.demo.enums;

import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import com.google.common.collect.Sets;

import java.util.Set;
import java.util.stream.Collectors;

import static com.example.demo.enums.Permission.*;

@AllArgsConstructor

public enum UserRole{
    USER(Sets.newHashSet(PROJECT_READ, PROJECT_WRITE, PROJECT_UPDATE)),
    ADMIN(Sets.newHashSet(PROJECT_DELETE, PROJECT_READ, PROJECT_WRITE, PROJECT_UPDATE));

    private final Set<Permission> permissions;


    public Set<Permission> getPermissions(){
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthority(){
        Set<SimpleGrantedAuthority> permissions = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return permissions;
    }
}

