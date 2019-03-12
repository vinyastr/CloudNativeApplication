package com.web.cloudapp.Repository;

import com.web.cloudapp.model.Note;
import com.web.cloudapp.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface NoteRepository extends CrudRepository<Note, String> {
    @Query("select n from #{#entityName} n where n.userData = ?1")
    List<Note> getAll(User user);
}
