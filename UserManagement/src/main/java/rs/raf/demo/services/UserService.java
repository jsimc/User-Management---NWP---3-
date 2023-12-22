package rs.raf.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rs.raf.demo.configuration.UserPasswordEncoder;
import rs.raf.demo.dtos.UserDto;
import rs.raf.demo.exceptions.UserException;
import rs.raf.demo.mapper.UserMapper;
import rs.raf.demo.model.User;
import rs.raf.demo.model.UserAuthority;
import rs.raf.demo.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, UserPasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder.getPasswordEncoder();
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User myUser = this.userRepository.findByUsername(username);
        if(myUser == null) {
            throw new UsernameNotFoundException("User name "+username+" not found");
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        if(myUser.isCanReadUsers())
            authorities.add(new SimpleGrantedAuthority(UserAuthority.CAN_READ_USERS.name()));
        if(myUser.isCanCreateUsers())
            authorities.add(new SimpleGrantedAuthority(UserAuthority.CAN_CREATE_USERS.name()));
        if(myUser.isCanUpdateUsers())
            authorities.add(new SimpleGrantedAuthority(UserAuthority.CAN_UPDATE_USERS.name()));
        if(myUser.isCanDeleteUsers())
            authorities.add(new SimpleGrantedAuthority(UserAuthority.CAN_DELETE_USERS.name()));
        return new org.springframework.security.core.userdetails.User(myUser.getUsername(), myUser.getPassword(), authorities);
    }
    //read
    public UserDto getUserByUsername(String username) {
        return this.userMapper.mapToUserDto(userRepository.findByUsername(username));
    }
    //read
    public List<UserDto> findAll() {
        return this.userRepository.findAll()
                .stream()
                .map(this.userMapper::mapToUserDto)
                .collect(Collectors.toList());
    }
    //delete
    public boolean deleteUser(Long userId) throws UserException {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserException("No user with id: " + userId));
        userRepository.delete(user);
        return true;
    }

    //create
    public UserDto createUser(User newUser) {
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        return userMapper.mapToUserDto(userRepository.save(newUser));
    }

    // update
    public UserDto updateUser(Long id, UserDto updatedUserDto) throws UserException {
        this.userRepository.findById(id).orElseThrow(() -> new UserException("No user with id: " + id));
        User updatedUser = userMapper.mapToUser(id, updatedUserDto, passwordEncoder);
        return userMapper.mapToUserDto(userRepository.save(updatedUser));
    }


}