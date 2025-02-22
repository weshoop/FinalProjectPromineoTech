package basketball.team.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import basketball.team.entity.Player;

public interface PlayerDao extends JpaRepository<Player, Long> {

}
