package basketball.team.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import basketball.team.controller.model.PlayerGame;
import basketball.team.controller.model.TeamData;
import basketball.team.controller.model.TeamPlayer;
import basketball.team.entity.Game;
import basketball.team.entity.Player;
import basketball.team.service.BasketballTeamService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/team")
@Slf4j
public class BasketballTeamController {
	
	@Autowired
	private BasketballTeamService basketballTeamService;
	
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public TeamData insertTeam(
			@RequestBody TeamData teamData) {
		log.info("Creating basketball team: {}", teamData);
		return basketballTeamService.saveTeam(teamData);
	}
	
	@PutMapping("/{teamId}")
	public TeamData updateTeam(@PathVariable Long teamId, 
			@RequestBody TeamData teamData) {
		log.info("Updating basketball team: {}", teamData);
		teamData.setTeamId(teamId);
		return basketballTeamService.saveTeam(teamData);
	}
	
	@GetMapping
	public List<TeamData> listAllTeams() {
		log.info("Retrieving all basketball teams");
		return basketballTeamService.getAllTeams();
	}
	
	@GetMapping("/{teamId}")
	public TeamData retrieveTeamById(@PathVariable Long teamId) {
		log.info("Retrieving team with ID: {}", teamId);
		return basketballTeamService.retrieveTeamById(teamId);
	}
	
	@DeleteMapping("/{teamId}")
	public Map<String, String> deleteTeamById(@PathVariable Long teamId) {
		log.info("Deleting basketball team with ID: {}", teamId);
		basketballTeamService.deleteTeamById(teamId);
		return Map.of("message", "Team with ID=" + teamId + " was deleted.");
	}
	
	@PostMapping("/player")
	@ResponseStatus(code = HttpStatus.CREATED)
	public TeamPlayer insertPlayer(@RequestBody TeamPlayer teamPlayer) {
		log.info("Creating player: {}", teamPlayer);
		
		TeamPlayer savedPlayer = basketballTeamService.savePlayer(teamPlayer);
		
		Long teamId = teamPlayer.getTeamId();
		
		
		if(teamId != null) {
			basketballTeamService.assignPlayerToTeam(savedPlayer.getPlayerId(), teamId);
		} else {
			log.warn("No teamId provided for player: {}", savedPlayer);
		}
		
		return savedPlayer;
	}
	
	@PutMapping("/player/{playerId}")
	public ResponseEntity<Player> updatePlayer(@PathVariable Long playerId, @RequestParam Long teamId, @RequestBody Player updatedPlayer) {
		log.info("Updating player with ID: {}. New details: Name={}, Position={}, TeamId{}"
				, playerId, updatedPlayer.getPlayerName(), updatedPlayer.getPosition(), teamId);
		
		Player player = basketballTeamService.updatePlayer(playerId, teamId, updatedPlayer);
		
		return ResponseEntity.ok(player);
		}
	
	@GetMapping("/{teamId}/player")
	public List<TeamPlayer> getAllPlayersByTeam(@PathVariable Long teamId) {
		log.info("Retrieving all players on team with ID: {}", teamId);
		return basketballTeamService.getAllPlayersByTeam(teamId);
	}
	
	@GetMapping("/{teamId}/player/{playerId}")
	public TeamPlayer retrievePlayerById(@PathVariable Long teamId, 
			@PathVariable Long playerId) {
		log.info("Retrieving player with ID: {} for Team ID: {}", playerId, teamId);
		Player player = basketballTeamService.findPlayerById(playerId, teamId);
		
		return new TeamPlayer(player);
	}
	
	@PutMapping("/player/{playerId}/team/{teamId}")
	public Map<String, String> assignPlayerToTeam(@PathVariable Long playerId, 
			@PathVariable Long teamId) {
		log.info("Assigning player with ID: {} to Team with ID: {}", playerId, teamId);
		basketballTeamService .assignPlayerToTeam(playerId, teamId);
		
		return Map.of("message", "Player with ID=" + playerId + " has been assigned to Team with ID: " + teamId);
	}
	
	@DeleteMapping("{teamId}/player/{playerId}")
	public Map<String, String> deletePlayer(@PathVariable Long playerId, 
			@PathVariable Long teamId) { 
		log.info("Deleting player with ID: {} for Team ID: {}", playerId, teamId);
		
		basketballTeamService.deletePlayerFromTeam(playerId, teamId);
		
		return Map.of("message", "Player with ID=" + playerId + " for Team=" + teamId + " was deleted.");
		
	}
	
	@PostMapping("/game")
	@ResponseStatus(code = HttpStatus.CREATED)
	public PlayerGame insertGame(@RequestBody PlayerGame playerGame) {
		log.info("Creating game: {}", playerGame);
		return basketballTeamService.saveGame(playerGame);
	}
	
	@PutMapping("/game/{gameId}")
	public PlayerGame updateGame(@PathVariable Long gameId, @RequestBody PlayerGame playerGame) {
		log.info("Updating game with ID: {}", gameId);
		return basketballTeamService.updateGame(gameId, playerGame);
	}
	
	@GetMapping("/game")
	public List<PlayerGame> listAllGames() {
		log.info("Retrieving all games");
		
		return basketballTeamService.getAllGames();
	}
	
	@GetMapping("/game/{gameId}")
	public PlayerGame retrieveGameById(@PathVariable Long gameId) {
		log.info("Retrieving game with ID: {}", gameId);
		Game game = basketballTeamService.findGameById(gameId);
		
		PlayerGame playerGame = new PlayerGame(game.getGameId(), game.getDate(), game.getCity());
		
		for (Player player: game.getPlayers()) {
			playerGame.getPlayers().add(new TeamPlayer(player));
		}
		
		return playerGame;
	}
	
	@GetMapping("/game/{gameId}/player")
	public List<TeamPlayer> retrievePlayersByGame(@PathVariable long gameId) {
		log.info("Retrieving players by game with ID: {}", gameId);
		
		return basketballTeamService.getAllPlayersByGame(gameId);
	}
	
	@PutMapping("/game/{gameId}/player/{playerId}")
	public Map<String, String> addPlayerToGame(@PathVariable Long gameId, @PathVariable Long playerId) {
		log.info("Adding player with ID: {} to game with ID: {}", gameId, playerId);
		
		basketballTeamService.addPlayerToGame(gameId, playerId);
		
		return Map.of("message", "Player with ID=" + playerId + " has been added to Game with ID=" + gameId);
	}
	
	@DeleteMapping("/game/{gameId}/player/{playerId}")
	public Map<String, String> removePlayerFromGame(@PathVariable Long gameId, @PathVariable Long playerId) {
		log.info("Removing player with ID: {} from game with ID: {}", playerId, gameId);
		basketballTeamService.removePlayerFromGame(gameId, playerId);
		
		return Map.of("message", "Player with ID=" + playerId + " has been removed from Game with ID=" + gameId);
	}
	
	@DeleteMapping("/game/{gameId}")
	public Map<String, String> deleteGameById(@PathVariable Long gameId) {
		log.info("Deleting game with ID: {}", gameId);
		
		basketballTeamService.deleteGameById(gameId);
		
		return Map.of("message", "Game with ID=" + gameId + " was deleted");
	}
}
