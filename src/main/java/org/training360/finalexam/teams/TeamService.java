package org.training360.finalexam.teams;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.training360.finalexam.CreatePlayerCommand;
import org.training360.finalexam.Player;
import org.training360.finalexam.PlayerRepository;
import org.training360.finalexam.PositionType;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class TeamService {

    private ModelMapper modelMapper;

    private TeamsRepository repository;
    private PlayerRepository playerRepository;

    public TeamDTO createTeam(CreateTeamCommand command) {
        Team team = new Team(command.getName());
        repository.save(team);
        return modelMapper.map(team, TeamDTO.class);
    }

    public List<TeamDTO> listAllTeamsWithPlayers() {
        List<Team> teams = repository.findAll();
        return teams.stream().map(t -> modelMapper.map(t, TeamDTO.class)).collect(Collectors.toList());
    }

    public TeamDTO addNewPlayerToATeam(Long id, CreatePlayerCommand command) {
        Team teamTemplate = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Team not exists!"));
        Player playerTemplate = new Player(command);
        playerTemplate.setTeam(teamTemplate);
        playerRepository.save(playerTemplate);
        repository.save(teamTemplate);
        return modelMapper.map(teamTemplate, TeamDTO.class);
    }


    public TeamDTO addExistingPlayerToATeamById(Long id, UpdateWithExistingPlayerCommand command) {
        Team teamTemplate = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Team not found!"));
        Player playerTemplate = playerRepository.findById(command.getId()).orElseThrow(() -> new IllegalArgumentException("Player not found!"));
        int numberOfPlayersInThePosition = findPlayersInTheGivenPosition(playerTemplate.getPosition(), teamTemplate);
        if (playerTemplate.getTeam() == null && numberOfPlayersInThePosition < 2) {
            playerTemplate.setTeam(teamTemplate);
            repository.save(teamTemplate);
        }
        return modelMapper.map(teamTemplate, TeamDTO.class);
    }

    private int findPlayersInTheGivenPosition(PositionType type, Team team) {
        return (int) team.getPlayers().stream().filter(p -> p.getPosition() == type).count();
    }


}
