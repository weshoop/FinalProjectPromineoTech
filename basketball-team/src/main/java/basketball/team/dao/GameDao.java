package basketball.team.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import basketball.team.entity.Game;

public interface GameDao extends JpaRepository<Game, Long> {

}
