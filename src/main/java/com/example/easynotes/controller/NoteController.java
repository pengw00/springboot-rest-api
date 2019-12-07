package com.example.easynotes.controller;

import com.example.easynotes.exception.ResourceNotFoundException;
import com.example.easynotes.model.Note;
import com.example.easynotes.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins="http://localhost:3000")
@RestController
@RequestMapping("/api")
public class NoteController {

    @Autowired
    NoteRepository noteRepository;

    @GetMapping("/notes")
    public List<Note> getAllNotes(){
        return noteRepository.findAll();
    }

    @PostMapping("/notes")
    public Note createNote(@Valid @RequestBody Note note){
        return noteRepository.save(note);
    }

//    The @RequestBody annotation is used to bind the request body with a method parameter.
//    The @Valid annotation makes sure that the request body is valid. Remember, we had marked Note’s title and content with @NotBlank annotation in the Note model?
//    If the request body doesn’t have a title or a content, then spring will return a 400 BadRequest error to the client.
    @GetMapping("notes/{id}")
    public Note getNoteById(@PathVariable(value = "id") Long noteId) {
        return noteRepository.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException("Note", "id", noteId));
    }
//    The @PathVariable annotation, as the name suggests, is used to bind a path variable with a method parameter.

    // Update a Note
    @PutMapping("/notes/{id}")
    public Note updateNote(@PathVariable(value = "id") Long noteId,
                           @Valid @RequestBody Note noteDetails) {

        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException("Note", "id", noteId));

        note.setTitle(noteDetails.getTitle());
        note.setContent(noteDetails.getContent());

        Note updatedNote = noteRepository.save(note);
        return updatedNote;
    }

    // Delete a Note
    @DeleteMapping("/notes/{id}")
    public ResponseEntity<?> deleteNote(@PathVariable(value = "id") Long noteId) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException("Note", "id", noteId));

        noteRepository.delete(note);

        return ResponseEntity.ok().build();
    }
}




//    @RestController annotation is a combination of Spring’s
//    @Controller and @ResponseBody annotations.

//     The @Controller annotation is used to define a controller
//     and the @ResponseBody annotation is used to indicate that the return
//     value of a method should be used as the response body of the request.

//    we are throwing a ResourceNotFoundException whenever a Note with the given id is not found.

//    This will cause Spring Boot to return a 404 Not Found error to the client (Remember, we had
//    added a @ResponseStatus(value = HttpStatus.NOT_FOUND) annotation to the ResourceNotFoundException class).

