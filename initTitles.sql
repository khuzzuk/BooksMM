create table if not exists Titles(
  titleId INT(11) PRIMARY KEY,
  author VARCHAR(255),
  tag varchar(255),
  title varchar(255),
  libID INT(11)
);
CREATE TABLE IF NOT EXISTS Queries(
  id INT(11) PRIMARY KEY,
  date VARCHAR(255),
  name VARCHAR(255)
);
CREATE TABLE IF NOT EXISTS Queries_Titles(
  Library_id INT(11),
  titles_titleId INT(11)
);