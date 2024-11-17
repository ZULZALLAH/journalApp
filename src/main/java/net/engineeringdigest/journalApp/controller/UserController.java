package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.Service.UserService;
import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/get/{id}")
    public ResponseEntity<User> getById(@PathVariable ObjectId id){
        try {

            User user = userService.findById(id).orElseThrow(() -> {
                return new RuntimeException("User Not Found");
            });
            return new ResponseEntity<>(user, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @PutMapping("/update")
    public ResponseEntity<User> updateUser(@RequestBody User user){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User username = userService.findByUserName(userName);

            if(username!=null) {
                username.setUsername(user.getUsername());
                username.setPassword(passwordEncoder.encode(user.getPassword()));
                username.setJournalEntry(user.getJournalEntry());
                return new ResponseEntity<>(userService.updateUser(username), HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);


    }

    @GetMapping("/get")
    public ResponseEntity<List<User>> getAll(){
        List<User> all = userService.getAllUser();
        if(all !=null && !all.isEmpty()){
            return new ResponseEntity<>(all,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> createUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
//            User userById = userService.findById(id).orElseThrow(() -> {
//                return new RuntimeException("User Not Found");
//            });
            //if (userById != null) {
               // userService.deleteUserById(id);
                userService.deleteUserByUserName(username);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
          //  }

    }
}
