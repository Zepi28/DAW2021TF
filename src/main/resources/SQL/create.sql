CREATE TABLE GIT_USER(
    username    VARCHAR(50)     NOT NULL UNIQUE,
    password    VARCHAR(20)     NOT NULL,
    email   VARCHAR(60)         NOT NULL UNIQUE,
    PRIMARY KEY(username)
);

CREATE TABLE PROJECT(
	p_name			VARCHAR(50)		NOT NULL UNIQUE,
	p_description 	VARCHAR(100) 	NOT NULL,

	PRIMARY KEY (p_name)
);

CREATE TABLE PROJECT_USER(
    username    VARCHAR(50)     NOT NULL,
    p_name	    VARCHAR(50)		NOT NULL ,

    PRIMARY KEY (username, p_name),
    FOREIGN KEY (p_name) REFERENCES PROJECT(p_name) ON DELETE CASCADE,
    FOREIGN KEY (username) REFERENCES GIT_USER(username)
);
		
CREATE TABLE ISSUE(
	id				SERIAL 			NOT NULL UNIQUE,
	i_name			VARCHAR(50)		NOT NULL,
	p_name			VARCHAR(50)				,
	i_description 	VARCHAR(100) 	NOT NULL,
	creationDate	DATE			NOT NULL,
	closeDate		DATE					,

	PRIMARY KEY (id, p_name),
	FOREIGN KEY (p_name) REFERENCES PROJECT(p_name) ON DELETE CASCADE
);
	
CREATE TABLE LABEL(
	id				SERIAL 																		 ,
	name			VARCHAR(20) 	CHECK(name IN('Defect', 'New-Functionality', 'Exploration')),

	PRIMARY key (id)
);

CREATE TABLE ISSUE_LABEL(
	issue_id		INTEGER			NOT NULL,
	p_name			VARCHAR(50)				,
	label_id		INTEGER 		NOT NULL,
	
	PRIMARY KEY (issue_id, label_id),
	FOREIGN KEY (issue_id, p_name) REFERENCES ISSUE(id, p_name) ON DELETE CASCADE,
	FOREIGN KEY (label_id) REFERENCES LABEL(id) ON DELETE CASCADE
);

CREATE TABLE COMMENT(
	id				SERIAL			NOT NULL UNIQUE,
	issue_id		INTEGER					,
	p_name			VARCHAR(50)				,
	description		VARCHAR(100) 	NOT NULL,
	dt				DATE			NOT NULL,
	
	PRIMARY KEY (id),
	FOREIGN KEY (issue_id, p_name) REFERENCES ISSUE(id, p_name) ON DELETE CASCADE
);

CREATE TABLE STATE(
	sid				serial 			NOT NULL UNIQUE,
	name 			VARCHAR(20) 	CHECK(name IN('Open', 'Closed', 'Archived')),
	PRIMARY KEY(sid)
);

CREATE TABLE ISSUE_STATE(
	sid				serial 			NOT NULL,
    iid				serial 			NOT NULL,
    p_name			VARCHAR(50)		NOT NULL,

	PRIMARY KEY (sid, iid, p_name),
	FOREIGN KEY(iid, p_name) REFERENCES ISSUE(id, p_name) ON DELETE CASCADE,
	FOREIGN KEY(sid) REFERENCES STATE(sid) ON DELETE CASCADE
);