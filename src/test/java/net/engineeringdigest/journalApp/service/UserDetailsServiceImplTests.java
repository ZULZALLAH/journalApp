package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.Service.ServiceImpl.UserDetailsServiceImpl;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;

import static org.mockito.Mockito.when;

@ActiveProfiles("dev") //prod
//@SpringBootTest //If we not used this annotation then we get NullPointerException because bean are not crete in spring Application Context/IOContainer  we get null for all @Autowired - service,repositry,etc
// If we used then we easily interact this all service,repository method to do process
// But their is also one approach without using this @SpringBootTest used this @InjectMocks in all service,repository to perform process to overcome from NULlExcept
public class UserDetailsServiceImplTests {
    //Note very impoertant--- we can perform from Both condition Mock vs MockBean

    // 1.using SpringContext and want to Mock some operations and some not
    // Ex - using Redis & MongoDB - i want to mock only that service/Repositry that take data from MongoDB, then it will not take from DB we pass dummy data
    // So, we used @SpringBootTest,@Autowired,@MockBean

    //2. Without using SpringConttext and outside it we have to do Mock
    // So, we used @InjectMocks,@Mock,@BeforeALL,etc

    //@Autowired
    @InjectMocks // it take all @Mock field which using this class to inject in it bean class. without using spring context outside it.
    private UserDetailsServiceImpl userDetailsService;

    //Spring Application context take all details of user to check from DB ,if not conect to db then pass dummy data.
    //Mockito used for test method but we does not interact with DB we can pass dummy data to test that method.
   // @Mock using this annotation when run this test application class So, it takes data from DB without using dummy data we intialize thenReturn(User.builder().username("KK").password("ASDFSW") to use for test.
    //So here using @Mock does not disable bean of userRepository when we run we get all details of user related to it.
   // @MockBean // this annotation will diable the bean of userRepository to not fetch data from DB ,take dummy data to test method run again this class as debug
    @Mock // using again we take this and inject this in UserDetailsServiceImpl where i used @InjectMocks bean in it & also remove @SpringBootTest
    // but then also userRepository are null then we used @BeforeEach ,we get NullPointerException to overcome we used (MockitoAnnotations.initMocks() using only class we used this keyword ,There no used of Spring Context we are outside of Spring context
    private UserRepository userRepository;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loadUserByUsernameTests(){
        when(userRepository.findByUsername(ArgumentMatchers.anyString())).thenReturn(User.builder().username("KK").password("ASDFSW").roles(new ArrayList<>()).build());
       // when(userRepository.findByUsername("Saif")).thenReturn(User.builder().username("Saif").password("ASDFSW").build());
        UserDetails user = userDetailsService.loadUserByUsername("KK");
        Assertions.assertNotNull(user);
    }
}
