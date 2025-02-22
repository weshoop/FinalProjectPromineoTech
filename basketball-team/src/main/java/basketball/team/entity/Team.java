package basketball.team.entity;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Team {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long teamId;
	private String teamName;
	private String city;
	
	@OneToMany(mappedBy = "team", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true, fetch = FetchType.EAGER)
	@JsonManagedReference
	private Set<Player> players = new HashSet<>();
}
