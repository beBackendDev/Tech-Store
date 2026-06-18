package net.myapplication.myapp.mapper;

import java.util.HashSet;
import java.util.Set;

import net.myapplication.myapp.enumpack.ERole;
import net.myapplication.myapp.user.dto.UserDTO;
import net.myapplication.myapp.user.entity.Role;
import net.myapplication.myapp.user.entity.User;

public class UserMapper {

    public static UserDTO mapToUserDTO(User user) {
        if (user == null) {
            return null;
        }

        ERole role = null;

        // lấy role đầu tiên
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            role = user.getRoles().iterator().next().getName();
        }

        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                role
        );
    }

    public static User mapToUser(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }

        User user = new User();

        user.setId(userDTO.getUserId());
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setEmail(userDTO.getEmail());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());

        // mặc định enabled
        user.setEnabled(true);

        // convert ERole -> Set<Role>
        if (userDTO.getRole() != null) {
            Role role = new Role();
            role.setName(userDTO.getRole());

            Set<Role> roles = new HashSet<>();
            roles.add(role);

            user.setRoles(roles);
        }

        return user;
    }
}