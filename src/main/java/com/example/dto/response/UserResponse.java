package com.example.dto.response;

import com.example.enums.AuthProvider;
import com.example.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private AuthProvider authProvider;
    private String avatarUrl;
    private Role role;
    private LocalDateTime createdAt;
}
