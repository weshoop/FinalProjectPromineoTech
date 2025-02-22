-- Insert Teams
INSERT INTO team (team_name, city) VALUES 
    ('Lakers', 'Los Angeles'),
    ('Warriors', 'San Francisco'),
    ('Bulls', 'Chicago'),
    ('Celtics', 'Boston'),
    ('Heat', 'Miami');

-- Insert Players (team_id references an existing team)
INSERT INTO player (player_name, position, team_id) VALUES 
    ('LeBron James', 'Forward', 1),
    ('Stephen Curry', 'Guard', 2),
    ('Michael Jordan', 'Guard', 3),
    ('Larry Bird', 'Forward', 4),
    ('Dwyane Wade', 'Guard', 5),
    ('Anthony Davis', 'Center', 1),
    ('Klay Thompson', 'Guard', 2),
    ('Scottie Pippen', 'Forward', 3),
    ('Paul Pierce', 'Forward', 4),
    ('Shaquille O\'Neal', 'Center', 5);

-- Insert Games
INSERT INTO game (date, city) VALUES 
    ('2025-03-10', 'Los Angeles'),
    ('2025-03-12', 'San Francisco'),
    ('2025-03-15', 'Chicago'),
    ('2025-03-18', 'Boston'),
    ('2025-03-20', 'Miami');

-- Insert Game-Player Relationships (Many-to-Many)
INSERT INTO game_player (game_id, player_id) VALUES 
    (1, 1), (1, 2), (1, 3), 
    (2, 4), (2, 5), (2, 6), 
    (3, 7), (3, 8), (3, 9), 
    (4, 10), (4, 1), (4, 2), 
    (5, 3), (5, 4), (5, 5);
