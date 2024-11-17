package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.Service.UserService;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.UserRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTests {
    // when we run application without using @SpringBootTest  i got NullPointer Exception because userRepository,userService,journalService,etc are bean null .So,Spring application context are null that why getting Exception. To overcome we use this annotation @SpringbootTest to insert bean in this class or interface while performing test.
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Test
    public void  testAdd(){
        assertEquals(4,2+2);
    }

    @Disabled
    @ParameterizedTest
    @ArgumentsSource(UserArgumentProvider.class)
    public void createUserTest(User user){
        System.out.println("Condition value: " + user.getUsername());
        assertTrue(userService.createUserTest(user));
    }

    // @Disabled // this annotation describe that if we not test this method then disabled it
    @ParameterizedTest
    @ValueSource(strings = {
            "JB",
            "NL",
            "KK"
    })
//    @EnumSource(names = {
//            "JB",
//            "NL",
//            "KK"
//    })
   // @Test
    public void  testFindByUserName(String name){
        User user = userRepository.findByUsername(name);
//        assertNotNull(userRepository.findByUsername(name),"failed for : " + name);
//        assertTrue(!user.getJournalEntry().isEmpty());
    }
    @Disabled
    @ParameterizedTest
    @CsvSource({
            "1,1,2",
            "2,10,12",
            "3,3,6"
    })
    public void test(int a, int b, int expected){
        assertEquals(expected, a + b);
    }


}
