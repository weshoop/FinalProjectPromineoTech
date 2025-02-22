DROP TABLE IF EXISTS game_player;
DROP TABLE IF EXISTS player;
DROP TABLE IF EXISTS game;
DROP TABLE IF EXISTS team;

CREATE TABLE team (
	team_id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
	team_name varchar(255) NOT NULL, 
	city varchar(255)
);

CREATE TABLE game (
	game_id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
	date DATE NOT NULL,
	city varchar(255) NOT NULL	
);

CREATE TABLE player (
	player_id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
	player_name varchar(255) NOT NULL,
	position varchar(255) NOT NULL,
	team_id int NOT NULL,
	FOREIGN KEY (team_id) REFERENCES Team(team_id) ON DELETE CASCADE
);

CREATE TABLE game_player (
	player_id int NOT NULL,
	game_id int NOT NULL,
	PRIMARY KEY (player_id, game_id),
	FOREIGN KEY (player_id) REFERENCES Player(player_id) ON DELETE CASCADE,
	FOREIGN KEY (game_id) REFERENCES Game(game_id) ON DELETE CASCADE
);
