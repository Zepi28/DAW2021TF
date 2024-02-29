INSERT INTO GIT_USER VALUES
('Fouto','isel', 'fouto@gmail.com'),
('Diogo','isel1', 'diogo@gmail.com'),
('Pedro','isel2', 'pedro@gmail.com');
INSERT INTO PROJECT VALUES
('daw-project','Daw project'),
('leiria', 'leiria project');
INSERT INTO PROJECT_USER VALUES
('Fouto', 'daw-project'),
('Diogo','daw-project'),
('Pedro','leiria');
INSERT INTO ISSUE VALUES(1,'daw issue','daw-project','Daw issue','2021-03-20');
INSERT INTO LABEL VALUES(1,'New-Functionality');
INSERT INTO ISSUE_LABEL VALUES(1,'daw-project',1);
INSERT INTO COMMENT VALUES(1,1,'daw-project','Daw comment','2021-04-05');
INSERT INTO STATE VALUES(1,'Open');
INSERT INTO ISSUE_STATE VALUES(1,1,'daw-project');
