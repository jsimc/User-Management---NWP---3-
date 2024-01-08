package rs.raf.demo.mapper;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import rs.raf.demo.dtos.UserDto;
import rs.raf.demo.model.User;
@Component
public class UserMapper {
    public UserDto mapToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setUserId(user.getUserId());
        userDto.setUsername(user.getUsername());
        userDto.setPassword(user.getPassword());
        userDto.setEmail(user.getEmail());
        userDto.setCanCreateUsers(user.isCanCreateUsers());
        userDto.setCanReadUsers(user.isCanReadUsers());
        userDto.setCanUpdateUsers(user.isCanUpdateUsers());
        userDto.setCanDeleteUsers(user.isCanDeleteUsers());

        return userDto;
    }

    public User mapToUser(Long id, UserDto userDto, PasswordEncoder passwordEncoder, Boolean isPasswordChanged) {
        User updatedUser = new User();
        updatedUser.setUserId(id); // nadam se da ce ovo da radi
        updatedUser.setUsername(userDto.getUsername());
        // ako je trenutni password isti kao sto je vec u bazi onda se nije menjao i ne moramo ga encode-ovati opet.
        if (isPasswordChanged) {
            updatedUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        } else {
            updatedUser.setPassword(userDto.getPassword());
        }
        updatedUser.setEmail(userDto.getEmail());
        updatedUser.setCanCreateUsers(userDto.isCanCreateUsers());
        updatedUser.setCanUpdateUsers(userDto.isCanUpdateUsers());
        updatedUser.setCanDeleteUsers(userDto.isCanDeleteUsers());
        updatedUser.setCanReadUsers(userDto.isCanReadUsers());
        return updatedUser;
    }
}
