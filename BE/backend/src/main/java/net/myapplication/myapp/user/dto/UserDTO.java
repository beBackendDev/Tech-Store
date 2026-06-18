package net.myapplication.myapp.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.myapplication.myapp.enumpack.ERole;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    
    private Long userId;
    private String username; //email or sdt
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private ERole role;
}
