package com.ivanov_sergey.todoapp.controller;

import com.ivanov_sergey.todoapp.persist.model.FriendRequest;
import com.ivanov_sergey.todoapp.persist.model.Media;
import com.ivanov_sergey.todoapp.persist.repository.MediaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class MediaController {

    private final MediaRepository mediaRepository;

    @Autowired
    public MediaController(MediaRepository mediaRepository) {
        this.mediaRepository = mediaRepository;
    }

    @Operation(summary = "Getting all media")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok",
                    content = @Content),
            @ApiResponse(responseCode = "201", description = "Created",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content)
    })
    @GetMapping("/media")
    public ResponseEntity<Set<Media>> getAllMedia(@RequestParam(required = false)
                                                                       String name) {
        Set<Media> media = new HashSet<>();

        try {
            if (name == null)
                media.addAll(mediaRepository.findAll());
            else
                media.addAll(mediaRepository.findByName(name));

            if (media.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(media, HttpStatus.CREATED);
    }

    @Operation(summary = "Get a media by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content)
    })
    @GetMapping("/media/{id}")
    public ResponseEntity<Media> getMediaById(@PathVariable("id") long id) {
        Optional<Media> optionalMedia = mediaRepository.findById(id);

        if (optionalMedia.isPresent()) {
            return new ResponseEntity<>(optionalMedia.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Creating a media")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Media.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content)
    })
    @PostMapping("/media")
    public ResponseEntity<Media> createMedia(@RequestBody Media request) {
        try {
            Media media = mediaRepository.save(request);

            return new ResponseEntity<>(media, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Updating a Media by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Media.class)) }),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content)
    })
    @PutMapping("/media/{id}")
    public ResponseEntity<Media> updateMedia(@PathVariable("id") long id,
                                                             @RequestBody Media media) {
        Optional<Media> optionalMedia = mediaRepository.findById(id);

        if (optionalMedia.isPresent()) {
            Media mediaFromDB = optionalMedia.get();
            mediaFromDB.setName(media.getName());
            mediaFromDB.setUser(media.getUser());
            mediaFromDB.setMediaType(media.getMediaType());
            mediaFromDB.setFilename(media.getFilename());

            return new ResponseEntity<>(mediaRepository.save(mediaFromDB), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Deleting a Media by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content)
    })
    @DeleteMapping("/media/{id}")
    public ResponseEntity<HttpStatus> deleteMedia(@PathVariable("id") long id) {
        try {
            mediaRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Deleting all Media")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content)
    })
    @DeleteMapping("/media")
    public ResponseEntity<HttpStatus> deleteAllMedia() {
        try {
            mediaRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

//    @ExceptionHandler
//    public ResponseEntity<TaskIncorrectData> handleException(NoSuchEntityException exception) {
//        TaskIncorrectData data = new TaskIncorrectData();
//        data.setInfo(exception.getMessage());
//
//        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
//    }
//
//    @ExceptionHandler
//    public ResponseEntity<TaskIncorrectData> handleException(Exception exception) {
//        TaskIncorrectData data = new TaskIncorrectData();
//        data.setInfo(exception.getMessage());
//
//        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
//    }
}
