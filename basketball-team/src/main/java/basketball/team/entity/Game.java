package basketball.team.entity;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
public class Game {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long gameId;
	
	private String date;
	private String city;
	
	@ManyToMany(mappedBy = "games")
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@JsonManagedReference
	private Set<Player> players = new HashSet<>();
}
