package com.ivanov_sergey.todoapp.controller;

import com.ivanov_sergey.todoapp.persist.model.FriendRequest;
import com.ivanov_sergey.todoapp.persist.repository.FriendRequestRepository;
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
public class FriendRequestController {

    private final FriendRequestRepository friendRequestRepository;

    @Autowired
    public FriendRequestController(FriendRequestRepository friendRequestRepository) {
        this.friendRequestRepository = friendRequestRepository;
    }

    @Operation(summary = "Getting all FriendRequests")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok",
                    content = @Content),
            @ApiResponse(responseCode = "201", description = "Created",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content)
    })
    @GetMapping("/friendRequests")
    public ResponseEntity<Set<FriendRequest>> getAllFriendRequests(
//            @RequestParam(required = false) String name
    ) {
        Set<FriendRequest> friendRequests = new HashSet<>();

        try {

//            if (name == null)
                friendRequests.addAll(friendRequestRepository.findAll());
//            else
//                friendRequests.addAll(friendRequestRepository.findByName(name));

            if (friendRequests.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(friendRequests, HttpStatus.CREATED);
    }

    @Operation(summary = "Get a friendRequest by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content)
    })
    @GetMapping("/friendRequests/{id}")
    public ResponseEntity<FriendRequest> getFriendRequestById(@PathVariable("id") long id) {
        Optional<FriendRequest> friendRequestOptional = friendRequestRepository.findById(id);

        if (friendRequestOptional.isPresent()) {
            return new ResponseEntity<>(friendRequestOptional.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Creating a friendRequest")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FriendRequest.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content)
    })
    @PostMapping("/friendRequests")
    public ResponseEntity<FriendRequest> createFriendRequest(@RequestBody FriendRequest request) {
        try {
            FriendRequest friendRequest = friendRequestRepository.save(request);

            return new ResponseEntity<>(friendRequest, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Updating a FriendRequest by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FriendRequest.class)) }),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content)
    })
    @PutMapping("/friendRequests/{id}")
    public ResponseEntity<FriendRequest> updateFriendRequest(@PathVariable("id") long id,
                                                             @RequestBody FriendRequest friendRequest) {
        Optional<FriendRequest> optionalFriendRequest = friendRequestRepository.findById(id);

        if (optionalFriendRequest.isPresent()) {
            FriendRequest friendRequestFromDB = optionalFriendRequest.get();
            friendRequestFromDB.setStatus(friendRequest.getStatus());

            return new ResponseEntity<>(friendRequestRepository.save(friendRequestFromDB), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Deleting a friendRequest by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content)
    })
    @DeleteMapping("/friendRequests/{id}")
    public ResponseEntity<HttpStatus> deleteFriendRequest(@PathVariable("id") long id) {
        try {
            friendRequestRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Deleting all friendRequests")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content)
    })
    @DeleteMapping("/friendRequests")
    public ResponseEntity<HttpStatus> deleteAllFriendRequests() {
        try {
            friendRequestRepository.deleteAll();
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
