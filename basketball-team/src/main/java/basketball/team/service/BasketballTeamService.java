package basketball.team.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import basketball.team.controller.model.PlayerGame;
import basketball.team.controller.model.TeamData;
import basketball.team.controller.model.TeamPlayer;
import basketball.team.dao.GameDao;
import basketball.team.dao.PlayerDao;
import basketball.team.dao.TeamDao;
import basketball.team.entity.Game;
import basketball.team.entity.Player;
import basketball.team.entity.Team;

@Service
public class BasketballTeamService {
		
		@Autowired
		private TeamDao teamDao;
		
		@Autowired
		private PlayerDao playerDao;
		
		@Autowired
		private GameDao gameDao;
		
		@Transactional
		public TeamData saveTeam(TeamData teamData) {
			
			Long teamId = teamData.getTeamId();
			Team team;
			
			if (teamId != null && teamId > 0) {
				team = teamDao.findById(teamId)
						.orElseThrow(() -> new NoSuchElementException
								("Team with ID=" + teamId + " was not found"));
			} else {
				team = new Team();
			}
			
			copyTeamFields(team, teamData);
			return new TeamData(teamDao.save(team));
			
		}
		
		private void copyTeamFields(Team team, TeamData teamData) {
			team.setTeamName(teamData.getTeamName());
			team.setCity(teamData.getCity());
		}
		
		@Transactional(readOnly = true)
		public List<TeamData> getAllTeams() {
			List<Team> teams = teamDao.findAll();
			List<TeamData> result = new LinkedList<>();
			
			for(Team team: teams) {
				TeamData psd = new TeamData(team);
				
				result.add(psd);
			}
			return result;
		}
		
		public Team findOrCreateTeam(Long teamId) {
			if(Objects.isNull(teamId) || teamId <= 0) {
				return new Team();
			}
			else {
				return findTeamById(teamId);
			}
		}
		
		@Transactional(readOnly = true)
		public Team findTeamById(long teamId) {
			return teamDao.findById(teamId)
					.orElseThrow(() -> new NoSuchElementException(
					"Team with ID=" + teamId + " was not found"));
		}
		
		public TeamData retrieveTeamById(Long teamId) {
			Team team = teamDao.findById(teamId)
					.orElseThrow(() -> new NoSuchElementException(
							"Team with ID=" + teamId + " was not found"));
			
			return new TeamData(team);
		}
		
		@Transactional
		public void deleteTeamById(Long teamId) {
			
			Team team = findTeamById(teamId);
			
			teamDao.delete(team);
		}
		
		@Transactional
		public TeamPlayer savePlayer(TeamPlayer teamPlayer) {
			
			Long teamId = teamPlayer.getTeamId();
			Team team = findTeamById(teamId);
			
			Long playerId = teamPlayer.getPlayerId();
			Player player = findOrCreatePlayer(playerId);
			
			copyPlayerFields(player, teamPlayer);
			
			player.setTeam(team);
			Player dbPlayer = playerDao.save(player);
			
			return new TeamPlayer(dbPlayer);
		}

		public Player updatePlayer(Long playerId, Long teamId, Player updatedPlayer) {
			
			Player existingPlayer = findPlayerById(playerId);
			Team team = findTeamById(teamId);
			
			existingPlayer.setPlayerName(updatedPlayer.getPlayerName());
			existingPlayer.setPosition(updatedPlayer.getPosition());
			existingPlayer.setTeam(team);
			
			return playerDao.save(existingPlayer);
		}
		
		private void copyPlayerFields(Player player, TeamPlayer teamPlayer) {
			player.setPlayerName(teamPlayer.getPlayerName());
			player.setPosition(teamPlayer.getPosition());
		}
		
		@Transactional(readOnly = true)
		public List<TeamPlayer> getAllPlayersByTeam(Long teamId) {
			
			Team team = findTeamById(teamId);
			List<TeamPlayer> result = new LinkedList<>();
			
			for(Player player: team.getPlayers()) {
				result.add(new TeamPlayer(player));
			}
			
			return result;
		}
		
		public Player findOrCreatePlayer(Long playerId) {
			
			
			if(Objects.isNull(playerId) || playerId <= 0) {
				return new Player();
			}
			else {
				return findPlayerById(playerId);
			}
		}

		@Transactional(readOnly = true)
		public Player findPlayerById(Long playerId, Long teamId) {
	
			Player player = playerDao.findById(playerId)
					.orElseThrow(() -> new NoSuchElementException(
							"Player with ID=" + playerId + " was not found"));
			if(player.getTeam() == null || player.getTeam().getTeamId() != teamId) {
				throw new IllegalArgumentException(
						"Player with ID=" + playerId + " does not belong to Team with ID=" + teamId);
			}
			
			return player;
		}
		
		
		@Transactional(readOnly = true)
		public Player findPlayerById(Long playerId) {
			
			Player player = playerDao.findById(playerId)
					.orElseThrow(() -> new NoSuchElementException(
							"Player with ID=" + playerId + " was not found."));
			
			return player;
		}
		
		
		@Transactional
		public void deletePlayerFromTeam(Long playerId, Long teamId) {
			
			Player player = findPlayerById(playerId, teamId);
			
			if(player.getTeam() != null) {
				player.getTeam().getPlayers().remove(player);
				player.setTeam(null);
			}
			
			if(player.getGames() !=null && !player.getGames().isEmpty()) {
				for (Game game: player.getGames()) {
					game.getPlayers().remove(player);
				}
				player.getGames().clear();
			}
			
			playerDao.delete(player);
		}
		
		@Transactional
		public TeamPlayer updatePlayer(Long playerId, TeamPlayer teamPlayer) {
			Player player = findPlayerById(playerId);
			Long teamId = teamPlayer.getTeamId();
			
			if(player.getTeam() != null && player.getTeam().getTeamId() != teamId) {
				throw new IllegalArgumentException("Player does not belong to the team with ID=" + teamId);
			}
			
			copyPlayerFields(player, teamPlayer);
			return new TeamPlayer(playerDao.save(player));
		}
		
		@Transactional
		public void assignPlayerToTeam(Long playerId, Long teamId) {
			Player player = findPlayerById(playerId);
			Team team = findTeamById(teamId);
			
			if(!team.getPlayers().contains(player)) {
				if(player.getTeam() != null) {
					player.getTeam().getPlayers().remove(player);
				}
				player.setTeam(team);
				team.getPlayers().add(player);
			}
			
			playerDao.save(player);
		}
		
		@Transactional
		public PlayerGame saveGame(PlayerGame playerGame) {
			Long gameId = playerGame.getGameId();
			Game game = findOrCreateGame(gameId);
			
			copyGameFields(game, playerGame);
			
			Game gameDb = gameDao.save(game);
			
			return new PlayerGame(gameDb.getGameId(), gameDb.getDate(), gameDb.getCity());
		}
		
		private void copyGameFields(Game game, PlayerGame playerGame) {
			game.setDate(playerGame.getDate());
			game.setCity(playerGame.getCity());
		}
		
		@Transactional(readOnly = true)
		public List<PlayerGame> getAllGames() {
			
			List<Game> games = gameDao.findAll();
			
			return games.stream().map(game -> {
				PlayerGame playerGame = new PlayerGame();
				playerGame.setGameId(game.getGameId());
				playerGame.setDate(game.getDate());
				playerGame.setCity(game.getCity());
				
				
				for (Player player: game.getPlayers()) {
					playerGame.getPlayers().add(new TeamPlayer(player));
				}
				
				return playerGame;
			}).collect(Collectors.toList());
			
		}
		
		public Game findOrCreateGame(Long gameId) {
			
			if(Objects.isNull(gameId) || gameId <= 0) {
				return new Game();
			}
			else {
				return findGameById(gameId);
			}
		}
		
		@Transactional(readOnly = true)
		public Game findGameById(Long gameId) {
			
			return gameDao.findById(gameId)
					.orElseThrow(() -> new NoSuchElementException(
							"Game with ID=" + gameId + " was not found."));
		}
		
		@Transactional
		public void deleteGameById(Long gameId) {
			
			Game game = findGameById(gameId);
			
			for(Player player: new ArrayList<>(game.getPlayers())) {
				removePlayerFromGame(gameId, player.getPlayerId());
			}
			
			gameDao.delete(game);
			
		}
		
		@Transactional
		public void addPlayerToGame(Long gameId, Long playerId) {
			
			Game game = findGameById(gameId);
			Player player = findPlayerById(playerId);
			
			if(!game.getPlayers().contains(player)) {
				
				game.getPlayers().add(player);
				player.getGames().add(game);
				gameDao.save(game);
				playerDao.save(player);
			} else {
				throw new IllegalStateException("Player with ID=" + playerId + " is already in Game with ID=" + gameId);
			}
		}
		
		@Transactional
		public void removePlayerFromGame(Long gameId, Long playerId) {
				
			Game game = findGameById(gameId);
			Player player = findPlayerById(playerId);
			
			if(!game.getPlayers().contains(player)) {
				throw new NoSuchElementException("Player with ID=" + playerId + " is not a part of Game with ID=" + gameId);
			}
			
			game.getPlayers().remove(player);
			player.getGames().remove(game);
			
			gameDao.save(game);
			playerDao.save(player);
		}
		
		@Transactional(readOnly = true)
		public List<TeamPlayer> getAllPlayersByGame(Long gameId) {
			
			Game game = findGameById(gameId);
			List<TeamPlayer> players = new LinkedList<>();
			
			for(Player player: game.getPlayers()) {
				players.add(new TeamPlayer(player));
			}
			
			return players;
		}
		
		@Transactional
		public PlayerGame updateGame(Long gameId, PlayerGame playerGame) {
			
			Game game = findGameById(gameId);
			
			copyGameFields(game, playerGame);
			
			game = gameDao.save(game);
			
			return new PlayerGame(game.getGameId(), game.getDate(), game.getCity());
		}
	}
	
		
