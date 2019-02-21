package com.web.cloudapp.Controllers;


import com.web.cloudapp.Repository.AttachmentRepository;
import com.web.cloudapp.Repository.NoteRepository;
import com.web.cloudapp.model.Attachment;
import com.web.cloudapp.model.Note;
import com.web.cloudapp.model.User;
import com.web.cloudapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import com.web.cloudapp.cloud.S3Services;

@RestController
@ComponentScan(basePackages = {"com.web.cloudapp"})
public class AttachmentController {

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private UserService service;

//    @Autowired
//    S3Services s3Services;

    //Post
    @RequestMapping(value = "/note/{id}/attachment", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public @ResponseBody
    ResponseEntity addAttachment(@PathVariable(value = "id") String id, @RequestParam("file") MultipartFile file) throws IOException {

        Map<String,String> map = new HashMap<String, String>();
        Note note;
        User user = service.getUserName();
        note = noteRepository.getNote(id);
        if(note == null ){
            map.put("status",HttpStatus.NOT_FOUND.toString());
            return new ResponseEntity(map,HttpStatus.NOT_FOUND);
        }

        //s3Services.uploadFile(keyName, file);

        Attachment attachment = new Attachment();
        attachment.setNoteData(note);
        String pathDir = System.getProperty("user.home")+"/uploads/";
        File pathNew = new File(pathDir);
        if(!pathNew.exists()){
            pathNew.mkdir();
        }
        File path = new File(pathDir+"_"+file.getOriginalFilename());
        String attatchmentUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(path.getAbsolutePath()).toUriString();

        //String attatchmentUrl = ServletUriComponentsBuilder.fromCurrentContextPath().path(file.getAbsolutePath()).toUriString();
        attachment.setUrl(attatchmentUrl);
//        File convertFile =  new File(file.getOriginalFilename());
//        convertFile.createNewFile();
//        FileOutputStream fout = new FileOutputStream(convertFile);
        file.transferTo(path);
//        fout.write(file.getBytes());
//        fout.close();

        attachmentRepository.save(attachment);
        return new ResponseEntity(attachment,HttpStatus.CREATED);
    }

    //GetAll
    @RequestMapping(value ="/note/{id}/attachments")
    public @ResponseBody ResponseEntity getAllAttachments(@PathVariable(value = "id") String id){
        List<Attachment> attachments = new ArrayList<>();
        Note note;
        User user = service.getUserName();
        note = noteRepository.getNote(id);
        attachments = attachmentRepository.getAllAttachments(note);
        return new ResponseEntity(attachments,HttpStatus.OK);
    }
}
