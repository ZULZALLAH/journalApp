package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.Service.JournalService;
import net.engineeringdigest.journalApp.Service.UserService;
import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal-entry")
public class JournalEntryContoller {

    @Autowired
    private JournalService journalService;
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/get/All")
    public ResponseEntity<List<JournalEntry>> getAllJournalEntriesOfUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        List<JournalEntry> all = user.getJournalEntry();
        if(all !=null && !all.isEmpty()){
            return new ResponseEntity<>(all,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/create") // Query failed with error code 20 with name 'IllegalOperation' and error message 'Transaction numbers are only allowed on a replica set member or mongos' on server localhost:27017
    // When MongoDB is running as a standalone server (a single-node setup without replicas), it cannot perform transactions because there are no additional nodes to coordinate with
    // when you attempt a transaction on a standalone MongoDB instance, MongoDB raises this error to indicate that transactions require a replica set or sharded cluster environment.
    // In MongoDB, nodes serve different purposes based on their configuration within a replica set node  and sharded cluster nodes.
    // so we used Atlas used server of aws with atlas to perform transaction
    public ResponseEntity<JournalEntry > createEntry(@RequestBody JournalEntry journalEntry){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        journalService.saveJournalEntry(journalEntry, userName);
        return new ResponseEntity<>(journalEntry,HttpStatus.CREATED);
    }
    @GetMapping("/getId/{id}")
    public ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable ObjectId id){

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            User user = userService.findByUserName(userName);
            List<JournalEntry> collect = user.getJournalEntry().stream().filter(x -> x.getId().equals(id)).collect(Collectors.toList());
            if(!collect.isEmpty()){
                Optional<JournalEntry> journalServiceById = journalService.findById(id);
                if(journalServiceById.isPresent()){
                    return new ResponseEntity<>(journalServiceById.get(),HttpStatus.OK);
                }
            }

            //  return new ResponseEntity<>(journalService.findById(id), HttpStatus.OK);

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }

    @DeleteMapping("/delete/{id}") // we can delete journalentry by id,it was deleted but that journal
    // is used in user which not deleted ,in relationalDB it has been deleted by cascade but in mongodb cascade not work so,here we pass username as well to delete that from user.
    public ResponseEntity<?> deleteById(@PathVariable ObjectId id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        boolean removed = journalService.deleteById(id, userName);
        if(removed){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<JournalEntry> updateById(@RequestBody JournalEntry journalEntry,@PathVariable ObjectId id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        List<JournalEntry> collect = user.getJournalEntry().stream().filter(x -> x.getId().equals(id)).collect(Collectors.toList());
        if(!collect.isEmpty()){
            Optional<JournalEntry> journalServiceById = journalService.findById(id);
            if(journalServiceById.isPresent()){
               JournalEntry oldEntry =  journalServiceById.get();
                if(oldEntry!=null) {
                    oldEntry.setTitle(journalEntry.getTitle() != null && !journalEntry.getTitle().equals("") ? journalEntry.getTitle() : oldEntry.getTitle());
                    oldEntry.setContent(journalEntry.getContent() != null && !journalEntry.getContent().equals("") ? journalEntry.getContent() : oldEntry.getContent());
                    oldEntry.setDate(LocalDateTime.now());
                    return new ResponseEntity(journalService.updateJournal(oldEntry), HttpStatus.ACCEPTED);
                }
            }
        }
        //  JournalEntry  oldEntry  = journalService.findById(id).orElseThrow(() -> { return new RuntimeException("Journal entry not found");});

       return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
