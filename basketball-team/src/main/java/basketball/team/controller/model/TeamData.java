package basketball.team.controller.model;

import java.util.HashSet;
import java.util.Set;

import basketball.team.entity.Player;
import basketball.team.entity.Team;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TeamData {
	
	private long teamId;
	private String teamName;
	private String city;
	
	private Set<TeamPlayer> players = new HashSet<>();
	
	public TeamData(Team team) {
		
		teamId = team.getTeamId();
		teamName = team.getTeamName();
		city = team.getCity();
		
		for(Player player: team.getPlayers()) {
			players.add(new TeamPlayer(player));
		}
	}
}
