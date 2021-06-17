CREATE TABLE IF NOT EXISTS Users(
    Username VARCHAR(32) PRIMARY KEY,
    Name VARCHAR(30),
    Email VARCHAR(32),
    Password VARCHAR(128)
);

CREATE TABLE IF NOT EXISTS Organizations(
    Name VARCHAR(20),
    UniqueID VARCHAR(64) PRIMARY KEY,
    Password VARCHAR(64)
);

CREATE TABLE IF NOT EXISTS OrganizationMembers(
    Username VARCHAR(32),
    OrganizationID VARCHAR(64),
    Role INT,

    PRIMARY KEY (Username, OrganizationID),
    FOREIGN KEY (OrganizationID) REFERENCES Organizations(UniqueID),
    FOREIGN KEY (Username) REFERENCES Users(Username)
);

CREATE TABLE IF NOT EXISTS Teams(
    UniqueID VARCHAR(64) PRIMARY KEY,
    Name VARCHAR(32)
);

CREATE TABLE IF NOT EXISTS OrganizationTeam(
    TeamID VARCHAR(64),
    OrganizationID VARCHAR(64),

    PRIMARY KEY(TeamID, OrganizationID),
    FOREIGN KEY (OrganizationID) REFERENCES Organizations(UniqueID),
    FOREIGN KEY (TeamID) REFERENCES Teams(UniqueID)
);

CREATE TABLE IF NOT EXISTS Tasks(
    UniqueID VARCHAR(64) PRIMARY KEY,
    ResponsibleUser VARCHAR(32),
    Type INT,
    PostedDate DATETIME,
    DeadLine DATETIME,
    Title VARCHAR(32),
    FOREIGN KEY (ResponsibleUser) REFERENCES Users(Username)
);

CREATE TABLE IF NOT EXISTS TeamTasks(
    TeamID VARCHAR(64),
    TaskID VARCHAR(64),

    PRIMARY KEY (TeamID, TaskID),
    FOREIGN KEY (TeamID) REFERENCES Teams(UniqueID),
    FOREIGN KEY (TaskID) REFERENCES Tasks(UniqueID)
);

CREATE TABLE IF NOT EXISTS TaskObservation(
    TaskID VARCHAR(64),
    Title VARCHAR(32),
    PostedDate DATETIME,
    FOREIGN KEY (TaskID) REFERENCES Tasks(UniqueID)
);

CREATE TABLE IF NOT EXISTS CommonSubtasks (
    TaskID VARCHAR(64),
    PostedDate DATETIME,
    Title VARCHAR(32),

    FOREIGN KEY (TaskID) REFERENCES Tasks(UniqueID)
);




