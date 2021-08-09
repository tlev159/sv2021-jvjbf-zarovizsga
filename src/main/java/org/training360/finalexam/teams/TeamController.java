package org.training360.finalexam.teams;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.training360.finalexam.players.CreatePlayerCommand;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/teams")
public class TeamController {

    private TeamService service;

    @PostMapping
    public TeamDTO createTeam(@Valid @RequestBody CreateTeamCommand command) {
        return service.createTeam(command);
    }

    @GetMapping
    public List<TeamDTO> listAllTeamWithPlayers() {
        return service.listAllTeamsWithPlayers();
    }

    @PostMapping("/{id}/players")
    public TeamDTO addNewPlayerToATeam(@PathVariable("id") Long id, @Valid @RequestBody CreatePlayerCommand command) {
        return service.addNewPlayerToATeam(id, command);
    }

    @PutMapping("/{id}/players")
    public TeamDTO addExistingPlayerToATeam(@PathVariable("id") Long id, @RequestBody UpdateWithExistingPlayerCommand command) {
        return service.addExistingPlayerToATeamById(id, command);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Problem> handleNotFound(IllegalArgumentException iae) {
        Problem problem = Problem.builder()
                .withType(URI.create("teams/not-found"))
                .withTitle("Team not found")
                .withStatus(Status.NOT_FOUND)
                .withDetail(iae.getMessage())
                .build();

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(problem);
    }

}
