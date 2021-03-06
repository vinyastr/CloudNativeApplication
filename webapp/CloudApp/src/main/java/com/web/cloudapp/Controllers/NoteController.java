package com.web.cloudapp.Controllers;

import com.timgroup.statsd.StatsDClient;
import com.web.cloudapp.model.Note;
import com.web.cloudapp.service.LogService;
import com.web.cloudapp.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class NoteController {

    @Autowired
    private NoteService noteService;

    @Autowired
    private StatsDClient statsDClient;

    @Autowired
    private LogService logService;


    ResponseEntity res =new ResponseEntity(HttpStatus.NOT_IMPLEMENTED);

    //Getting All Notes
    @GetMapping("/note")
    public @ResponseBody
    ResponseEntity getAllNotes(){
        statsDClient.incrementCounter("note.getAll");
        List<Note> out= noteService.getAllNotes();
        logService.logger.info("Request completed successfully : "+ HttpStatus.OK.toString());
        return new ResponseEntity(out, HttpStatus.OK);
    }

    //Get one note with NoteId
    @GetMapping("/note/{id}")
    public @ResponseBody ResponseEntity getNote(@PathVariable(value = "id") String id){
        statsDClient.incrementCounter("note.get");
        Note n = noteService.getNote(id);
        if(n!=null)res= new ResponseEntity(n,HttpStatus.OK);
        logService.logger.info("Request completed successfully : "+ HttpStatus.OK.toString());
     return res;
    }

    //Creating a Note
    @PostMapping("/note")
    public @ResponseBody ResponseEntity addNote(@RequestBody Note note) {
        statsDClient.incrementCounter("note.post");
        if(noteService.createNote(note)) res= new ResponseEntity(note, HttpStatus.CREATED);
        logService.logger.info("Request completed successfully : "+ HttpStatus.CREATED.toString());
        return res;
    }

    //Updating the Note
    @PutMapping("/note/{id}")
    public @ResponseBody ResponseEntity updateNote(@PathVariable(value = "id") String id, @RequestBody Note note) {
        statsDClient.incrementCounter("note.put");
        if(noteService.updateNote(id,note)) res = new ResponseEntity(HttpStatus.NO_CONTENT);
        logService.logger.info("Request completed successfully : "+ HttpStatus.NO_CONTENT.toString());
        return res;
    }

    //Deleting the Note
    @DeleteMapping("/note/{id}")
    public @ResponseBody ResponseEntity deleteNote(@PathVariable(value = "id")String id) throws Exception{
        try {
            statsDClient.incrementCounter("note.delete");
            if (noteService.deleteNote(id)) res = new ResponseEntity(HttpStatus.NO_CONTENT);
            logService.logger.info("Request completed successfully : " + HttpStatus.NO_CONTENT.toString());
            return res;
        }catch(Exception ex){
            logService.logger.warning(ex.getMessage());
            throw ex;
        }
    }
}

