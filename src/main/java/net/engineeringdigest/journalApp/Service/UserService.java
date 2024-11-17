package net.engineeringdigest.journalApp.Service;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        System.out.println("Users: " + user.getPassword());
        user.setRoles(Arrays.asList("USERS","ADMIN"));
        return userRepository.save(user);
    }

    //Testing using JUnit

    public boolean createUserTest(User user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            System.out.println("Users: " + user.getPassword());
            System.out.println("Users: " + user.getUsername());
            user.setRoles(Arrays.asList("USERS","ADMIN"));
            userRepository.save(user);
            return true;
        }catch (Exception e){
            return false;
        }

    }
    public User createNewUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findById(ObjectId id) {
        return userRepository.findById(id);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public List<User> getAllUser(){
        return userRepository.findAll();
    }

    public void deleteUserById(ObjectId id) {
         userRepository.deleteById(id);
    }

    public User  findByUserName(String username) {
        return userRepository.findByUsername(username);
    }

    public void deleteUserByUserName(String username) {
        userRepository.deleteByUsername(username);
    }

    public List<JournalEntry> findUserJournalId(String userName) {
        User user = userRepository.findByUsername(userName);
        List<JournalEntry> journalEntry = user.getJournalEntry();
        return journalEntry;
    }
}
