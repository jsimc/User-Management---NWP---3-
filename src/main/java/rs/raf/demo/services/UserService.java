package rs.raf.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import rs.raf.demo.model.User;
import rs.raf.demo.model.UserAuthority;
import rs.raf.demo.repositories.UserRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
}
