package basketball.team.entity;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
public class Player {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long playerId;
	private String playerName;
	private String position;
	
	@ManyToMany
	@JoinTable(
			name = "game_player",
			joinColumns = @JoinColumn(name = "player_id"), 
			inverseJoinColumns = @JoinColumn(name = "game_id"))
	@JsonBackReference
	private Set<Game> games = new HashSet<>();
	
	@ManyToOne
	@JoinColumn(name = "team_id")
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@JsonBackReference
	private Team team;
}
