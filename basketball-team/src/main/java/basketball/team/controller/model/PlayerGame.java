package basketball.team.controller.model;

import java.util.HashSet;
import java.util.Set;

import basketball.team.entity.Game;
import basketball.team.entity.Player;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PlayerGame {
	
	private long gameId;
	private String date;
	private String city;
	private Set<TeamPlayer> players = new HashSet<>();
	
	public PlayerGame(long gameId, String date, String city) {
		this.gameId = gameId;
		this.date = date;
		this.city = city;
	}
}
