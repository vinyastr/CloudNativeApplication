package com.web.cloudapp.Repository;

import com.web.cloudapp.model.Note;
import com.web.cloudapp.model.User;

import java.util.List;

public interface NoteRepositoryCustom {
    List<Note> getAllNotes(User user);

    Note getNote(User user, String id);
}
