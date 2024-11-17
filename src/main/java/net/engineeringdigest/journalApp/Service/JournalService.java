package net.engineeringdigest.journalApp.Service;

import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.JournalRepository;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JournalService {

  //  private static final Logger logger = LoggerFactory.getLogger(JournalService.class);
    @Autowired
    private JournalRepository journalRepository;

    @Autowired
    private UserService userService;

    @Transactional // This annotation is used when we want save/delete some data from 2 or more table if data save in one table after that get some NullException or any excepryion that date are not saved in another table ,
    // then this annotation will rollback the save data from first table and data will not saved in both table. connecting using atlas with AWS as databse mongodb
    public void  saveJournalEntry(JournalEntry journalEntry, String userName) {
        User user = userService.findByUserName(userName);
        journalEntry.setDate(LocalDateTime.now());
        JournalEntry save = journalRepository.save(journalEntry);
        user.getJournalEntry().add(save);
        userService.createNewUser(user);
       // user.setUsername(null); // add only for understand transactinal and also annotate in config package TransactionalCongig class using atlas
       // userService.createUser(user);
    }

    public Optional<JournalEntry> findById(ObjectId id){
        return journalRepository.findById(id);
    }

    public List<JournalEntry> findAll() {
        return journalRepository.findAll();
    }

    @Transactional
    public boolean deleteById(ObjectId id, String userName) {
        boolean remove = false;
        try {
            User user = userService.findByUserName(userName);
             remove = user.getJournalEntry().removeIf(u -> u.getId().equals(id));
            if (remove) {
                userService.createUser(user);
                journalRepository.deleteById(id);
            }
        }catch (Exception e){
           // logger.info("Error occurred for {} :",id,e);
             log.info("Error occurred for {} :",id,e);

            throw new RuntimeException("An error occured while deleting the entry.",e);
        }
        return remove;

    }

    public JournalEntry updateJournal(JournalEntry oldEntry) {
      return  journalRepository.save(oldEntry);
    }

//    public Optional<JournalEntry> findjournalByUsername(String username) {
//     return journalRepository.findByUsername(username);
//    }

    public void deleteJournalById(List<JournalEntry> userJournalId) {
        List<ObjectId> id = userJournalId.stream().map(JournalEntry::getId).collect(Collectors.toList());
        journalRepository.deleteAllById(id);
    }
}
