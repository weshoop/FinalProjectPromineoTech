package basketball.team.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import basketball.team.entity.Team;

public interface TeamDao extends JpaRepository<Team, Long> {

}
