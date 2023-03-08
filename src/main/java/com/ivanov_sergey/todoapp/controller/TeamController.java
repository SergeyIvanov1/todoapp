package com.ivanov_sergey.todoapp.controller;

import com.ivanov_sergey.todoapp.persist.model.Team;
import com.ivanov_sergey.todoapp.persist.repository.TeamRepository;
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
public class TeamController {

    private final TeamRepository teamRepository;

    @Autowired
    public TeamController(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Operation(summary = "Getting all teams")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok",
                    content = @Content),
            @ApiResponse(responseCode = "201", description = "Created",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content)
    })
    @GetMapping("/teams")
    public ResponseEntity<Set<Team>> getAllTeams(@RequestParam(required = false) String name) {
        Set<Team> teams = new HashSet<>();

        try {

            if (name == null)
                teams.addAll(teamRepository.findAll());
            else
                teams.addAll(teamRepository.findByName(name));

            if (teams.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(teams, HttpStatus.CREATED);
    }

    @Operation(summary = "Get a team by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content)
    })
    @GetMapping("/teams/{id}")
    public ResponseEntity<Team> getTeamById(@PathVariable("id") long id) {
        Optional<Team> optionalTeam = teamRepository.findById(id);

        if (optionalTeam.isPresent()) {
            return new ResponseEntity<>(optionalTeam.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Creating a team")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Team.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content)
    })
    @PostMapping("/teams")
    public ResponseEntity<Team> createTeam(@RequestBody Team request) {
        try {
            Team team = teamRepository.save(request);

            return new ResponseEntity<>(team, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Updating a team by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Team.class)) }),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content)
    })
    @PutMapping("/teams/{id}")
    public ResponseEntity<Team> updateTeam(@PathVariable("id") long id,
                                                             @RequestBody Team team) {
        Optional<Team> optionalTeam = teamRepository.findById(id);

        if (optionalTeam.isPresent()) {
            Team teamFromDB = optionalTeam.get();
            teamFromDB.setName(team.getName());
            teamFromDB.setAvatar(team.getAvatar());
            teamFromDB.setCompany(team.getCompany());
            teamFromDB.setHometown(team.getHometown());

            return new ResponseEntity<>(teamRepository.save(teamFromDB), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Deleting a team by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content)
    })
    @DeleteMapping("/teams/{id}")
    public ResponseEntity<HttpStatus> deleteTeam(@PathVariable("id") long id) {
        try {
            teamRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Deleting all teams")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content)
    })
    @DeleteMapping("/teams")
    public ResponseEntity<HttpStatus> deleteAllTeams() {
        try {
            teamRepository.deleteAll();
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
