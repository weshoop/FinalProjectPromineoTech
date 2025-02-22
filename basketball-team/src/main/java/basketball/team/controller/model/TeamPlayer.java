package basketball.team.controller.model;

import java.util.HashSet;
import java.util.Set;

import basketball.team.entity.Game;
import basketball.team.entity.Player;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TeamPlayer {
	
	private long playerId;
	private String playerName;
	private String position;
	private Long teamId;
	private Set<PlayerGame> games = new HashSet<>();
	
	public TeamPlayer(Player player) {
		playerId = player.getPlayerId();
		playerName = player.getPlayerName();
		position = player.getPosition();
		teamId = (player.getTeam() != null) ? player.getTeam().getTeamId() : null;
	}
}
