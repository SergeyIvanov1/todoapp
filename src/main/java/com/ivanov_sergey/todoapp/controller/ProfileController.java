package com.ivanov_sergey.todoapp.controller;

import com.ivanov_sergey.todoapp.persist.model.FriendRequest;
import com.ivanov_sergey.todoapp.persist.model.Profile;
import com.ivanov_sergey.todoapp.persist.repository.FriendRequestRepository;
import com.ivanov_sergey.todoapp.persist.repository.ProfileRepository;
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
public class ProfileController {

    private final ProfileRepository profileRepository;

    @Autowired
    public ProfileController(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Operation(summary = "Getting all profiles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok",
                    content = @Content),
            @ApiResponse(responseCode = "201", description = "Created",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content)
    })
    @GetMapping("/profiles")
    public ResponseEntity<Set<Profile>> getAllProfiles(
//            @RequestParam(required = false) String name
    ) {
        Set<Profile> profiles = new HashSet<>();

        try {

//            if (name == null)
                profiles.addAll(profileRepository.findAll());
//            else
//                profiles.addAll(profileRepository.findByName(name));

            if (profiles.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(profiles, HttpStatus.CREATED);
    }

    @Operation(summary = "Get a profile by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content)
    })
    @GetMapping("/profiles/{id}")
    public ResponseEntity<Profile> getProfileById(@PathVariable("id") long id) {
        Optional<Profile> optionalProfile = profileRepository.findById(id);

        if (optionalProfile.isPresent()) {
            return new ResponseEntity<>(optionalProfile.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Creating a profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Profile.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content)
    })
    @PostMapping("/profiles")
    public ResponseEntity<Profile> createProfile(@RequestBody Profile request) {
        try {
            Profile profile = profileRepository.save(request);

            return new ResponseEntity<>(profile, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Updating a profile by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Profile.class)) }),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content)
    })
    @PutMapping("/profiles/{id}")
    public ResponseEntity<Profile> updateProfile(@PathVariable("id") long id,
                                                             @RequestBody Profile profile) {
        Optional<Profile> optionalProfile = profileRepository.findById(id);

        if (optionalProfile.isPresent()) {
            Profile profileFromDB = optionalProfile.get();
            profileFromDB.setBirthday(profile.getBirthday());
            profileFromDB.setGender(profile.getGender());
            profileFromDB.setHometown(profile.getHometown());
            profileFromDB.setFirstName(profile.getFirstName());
            profileFromDB.setLastName(profile.getLastName());
            profileFromDB.setAvatar(profile.getAvatar());

            return new ResponseEntity<>(profileRepository.save(profileFromDB), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Deleting a profile by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content)
    })
    @DeleteMapping("/profiles/{id}")
    public ResponseEntity<HttpStatus> deleteProfile(@PathVariable("id") long id) {
        try {
            profileRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Deleting all profiles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content)
    })
    @DeleteMapping("/profiles")
    public ResponseEntity<HttpStatus> deleteAllProfiles() {
        try {
            profileRepository.deleteAll();
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
