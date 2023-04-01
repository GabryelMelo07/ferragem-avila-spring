DROP DATABASE IF EXISTS ferragem_avila;

CREATE DATABASE ferragem_avila;

\c ferragem_avila;

/*
CREATE TABLE vendedor (
    id serial primary key,
    email text not null,
    senha text not null,
    unique(email)
);*/ 

CREATE TABLE produto (
    id serial primary key,
    descricao text not null,
    preco double precision,
   -- status boolean default true,
    estoque integer,
    cod_barras integer
);

CREATE TABLE venda (
    id serial primary key,
    -- vendedor_id integer references vendedor (id),
    data_hora timestamp default current_timestamp
);

CREATE TABLE item ( 
    id serial primary key,
    quantidade double precision,
    -- valor_unitario double precision,
    produto_id integer references produto (id),
    venda_id integer references venda (id) ON DELETE CASCADE,
    UNIQUE(produto_id, venda_id)
);

INSERT INTO produto (descricao, preco, estoque, cod_barras) VALUES
('teste1', 100.0, 1000, 1234567891011);

INSERT INTO produto (descricao, preco, estoque, cod_barras) VALUES
('teste2', 100.0, 1000, 1234567891055);
