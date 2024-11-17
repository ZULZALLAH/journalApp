package net.engineeringdigest.journalApp;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
//it will says to spring to find and get all method where transcational anntation used and then springboot create transactional context/container for each method where trans annotion used,and all method treat at each as issolated(Alag-alag) if any failed then it rollback it,if two user  where for each trancation context, data are saved if any failed then that will rollback only because all context are separate.
//who will manage/run this transaction - Interface=PlatformTransactionManager & Class = MongoTransactionManager they both will perform for commit/rollback data , add in config package.
public class JournalApplication {

    public static void main(String[] args) {
        SpringApplication.run(JournalApplication.class, args);

//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        String plaintextPassword = "JB";
//        String hashedPassword = "$2a$10$Izc9b.OHEBN2wJN1ul7pv.7gmoKRZl16PPaOfikMmnMgU/jP8RDJ6";

//        boolean matches = encoder.matches(plaintextPassword, hashedPassword);
//        System.out.println("Password matches: " + matches);
    }

    @Bean
    public PlatformTransactionManager platformTransactionManager(MongoDatabaseFactory mongoDatabaseFactory){ // it help in connecting mongodb for perform commit/rollback.
        return new MongoTransactionManager(mongoDatabaseFactory);
    }



}