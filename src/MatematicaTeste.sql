CREATE TABLE genero (
                        id_genero SERIAL PRIMARY KEY  NOT NULL,
                        nome_genero VARCHAR(250) UNIQUE NOT NULL
);

CREATE TABLE filme (
                       id_filme INT PRIMARY KEY NOT NULL,
                       nome_filme VARCHAR(250) NOT NULL,
                       ano_lancamento DATE NOT NULL
);

CREATE TABLE filme_genero (
                              PRIMARY KEY (id_filme, id_genero),
                              id_filme INT,
                              id_genero INT,
                              FOREIGN KEY (id_filme) REFERENCES filme(id_filme),
                              FOREIGN KEY (id_genero) REFERENCES genero(id_genero)
);

CREATE TABLE ranking_filme (
                               id_ranking SERIAL PRIMARY KEY NOT NULL,
                               estrelas int CHECK(estrelas >= 0 AND estrelas <= 5),
                               id_filme INT,
                               FOREIGN KEY (id_filme) REFERENCES filme(id_filme)
);

CREATE TABLE diretor (
                         id_diretor SERIAL PRIMARY KEY NOT NULL,
                         nome VARCHAR(250) NOT NULL,
                         sobrenome VARCHAR(250) NOT NULL,
                         empregador VARCHAR(250) NOT NULL,
                         nacionalidade VARCHAR(250) NOT NULL,
                         data_nascimento DATE NOT NULL
);

CREATE TABLE filme_diretor (
                               id_filme_diretor SERIAL PRIMARY KEY NOT NULL,
                               id_filme INT,
                               id_diretor INT,
                               FOREIGN KEY (id_filme) REFERENCES filme(id_filme),
                               FOREIGN KEY (id_diretor) REFERENCES diretor(id_diretor),
                               salario MONEY NOT NULL
);

CREATE TABLE ator (
                      id_ator SERIAL PRIMARY KEY NOT NULL,
                      nome VARCHAR(250) NOT NULL,
                      sobrenome VARCHAR(250) NOT NULL,
                      data_nascimento DATE NOT NULL,
                      empregado VARCHAR(250) NOT NULL,
                      nacionalidade VARCHAR(250) NOT NULL,
                      nome_artistico
);

CREATE TABLE filme_ator (
                            id_filme_ator SERIAL PRIMARY KEY NOT NULL,
                            id_filme INT NOT NULL,
                            id_ator INT NOT NULL,
                            salario MONEY NOT NULL,
                            FOREIGN KEY (id_filme) REFERENCES filme(id_filme),
                            FOREIGN KEY (id_ator) REFERENCES ator(id_ator)
);


INSERT INTO GENERO (nome_genero) VALUES
                                     ('Ação'),
                                     ('Aventura'),
                                     ('Cinema de arte'),
                                     ('Chanchada'),
                                     ('Comédia'),
                                     ('Comédia de ação'),
                                     ('Comédia de terror'),
                                     ('Comédia dramática'),
                                     ('Comédia romântica'),
                                     ('Dança'),
                                     ('Documentário'),
                                     ('Docuficção'),
                                     ('Drama'),
                                     ('Espionagem'),
                                     ('Faroeste'),
                                     ('Fantasia'),
                                     ('Fantasia científica'),
                                     ('Ficção científica'),
                                     ('Filmes com truques'),
                                     ('Filmes de guerra'),
                                     ('Mistério'),
                                     ('Musical'),
                                     ('Filme policial'),
                                     ('Romance'),
                                     ('Terror'),
                                     ('Thriller');


    INSERT INTO filme VALUES
    (1,'Jurassic Park - Parque dos Dinossauros','2013-08-16'),
    (2,'Tubarão','1975-07-07'),
    (3,'Império do Sol','2021-04-14'),

    (4,'Duna','2021-10-21'),
    (5,'Duna: Parte 2','2024-02-6'),
    (6,'Incêndios','2010-09-13'),

    (7,'Avatar','2010-10-15'),
    (8,'Titanic','2012-04-13'),
    (9,'Rambo 2 - A Missão', '1985-07-16')



INSERT INTO DIRETOR (NOME, SOBRENOME,EMPREGADOR,NACIONALIDADE,DATA_NASCIMENTO) VALUES
    ('Steven','Spielberg','Amblin Parners','Norte Americano,','1946-12-18'),
    ('Denis','Villeneuve','None','Canadá','1967-10-03'),
    ('James','Cameron','None','Canadá','1954-08-16');



INSERT INTO FILME_DIRETOR (id_diretor,id_filme)VALUES
(1,1),
(1,2),
(1,3),
(2,4),
(2,5),
(2,6),
(3,7),
(3,8),
(3,9);

    INSERT INTO FILME (id_filme, nome_filme) VALUES
    (1,'Matador de Aluguel'),
    (2,'O problema dos corpos'),
    (3,'Xógun: A Gloriosa Saga do Japão'),
    (4,'Duna: Parte 1'),
    (5,'Probres  Criaturas'),
    (6,'Shaitann'),
    (7,'Oppenheimer');



INSERT INTO FILME_GENERO (ID_FILME, ID_GENERO) VALUES
                                                   (1,1),
                                                   (1,21),
                                                   (2,2),
                                                   (2,13),
                                                   (2,16),
                                                   (2,21),
                                                   (2,17),
                                                   (3,2),
                                                   (3,13),
                                                   (4,1),
                                                   (4,2),
                                                   (4,13),
                                                   (4,18);