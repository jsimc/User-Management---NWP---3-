package rs.raf.demo.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.raf.demo.dtos.UserDto;
import rs.raf.demo.exceptions.UserException;
import rs.raf.demo.model.User;
import rs.raf.demo.services.UserService;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/all",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserDto> getAllUsers(){
        return userService.findAll();
    }

    @GetMapping(value = "/{username}")
    public UserDto getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }

    // create
    @PostMapping("/create")
    public ResponseEntity<UserDto> createUser(@RequestBody User user) {
        return new ResponseEntity<>(userService.createUser(user), HttpStatus.OK);
    }

    // delete
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        try {
            boolean removed = userService.deleteUser(userId);
            if(removed){
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error during removal of user with id " + userId);
        } catch (Exception e) {
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // update
    @PutMapping("/update/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long userId, @RequestBody UserDto updatedUser) {
        try {
            return new ResponseEntity<>(userService.updateUser(userId, updatedUser), HttpStatus.ACCEPTED);
        } catch (UserException userException) {
            System.out.println(userException.getMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
