-- DROP DATABASE IF EXISTS ferragem_avila;

-- CREATE DATABASE ferragem_avila;

-- \c ferragem_avila;

DROP TABLE IF EXISTS item;
DROP TABLE IF EXISTS venda;
DROP TABLE IF EXISTS produto;
DROP FUNCTION IF EXISTS removeProdutoEstoque;
DROP FUNCTION IF EXISTS removeProdutoEstoque_trigger;

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
    estoque integer default 0 CHECK (estoque >= 0),
    cod_barras integer
);

CREATE TABLE venda (
    id serial primary key,
    concluida boolean DEFAULT FALSE,
    -- vendedor_id integer references vendedor (id),
    data_hora timestamp default current_timestamp
);

CREATE TABLE item ( 
    id serial primary key,
    quantidade integer,
    preco_item double precision,
    produto_id integer references produto (id),
    venda_id integer references venda (id) ON DELETE CASCADE,
    UNIQUE(produto_id, venda_id)
);

INSERT INTO produto (descricao, preco, estoque, cod_barras) VALUES
('teste1', 100.0, 1000, 123456711);

INSERT INTO produto (descricao, preco, estoque, cod_barras) VALUES
('teste2', 100.0, 1000, 123891055);

INSERT INTO produto (descricao, preco, estoque, cod_barras) VALUES
('semEstoque', 100.0, 1, 1121212);

CREATE or REPLACE FUNCTION removeProdutoEstoque(integer) RETURNS BOOLEAN AS
$$
DECLARE
    tupla_produto RECORD;
    item_rec RECORD;
BEGIN
    FOR item_rec IN SELECT * from item where venda_id = $1 LOOP
        SELECT INTO tupla_produto * from produto where id = item_rec.produto_id; 
        IF (tupla_produto.estoque >= item_rec.quantidade) THEN
            UPDATE produto SET estoque = estoque - item_rec.quantidade WHERE id = tupla_produto.id;
        ELSE
            FOR item_rec IN SELECT * from item where venda_id = $1 AND id < item_rec.id LOOP
                SELECT INTO tupla_produto * from produto where id = item_rec.produto_id; 
                UPDATE produto SET estoque = estoque + item_rec.quantidade WHERE id = tupla_produto.id;
            END LOOP;
            RETURN FALSE;
        END IF;
    END LOOP;
    RETURN TRUE;
END;
$$ LANGUAGE 'plpgsql';

CREATE OR replace FUNCTION removeProdutoEstoque_trigger() RETURNS TRIGGER AS
$$
BEGIN
    IF(removeProdutoEstoque(NEW.id)) THEN
        RETURN NEW;
    ELSE
        DELETE FROM venda WHERE id = NEW.id;
        RETURN OLD;
    END IF;
END;
$$ LANGUAGE 'plpgsql';

CREATE or replace TRIGGER removeProdutoEstoque_trigger after UPDATE ON venda FOR EACH ROW EXECUTE PROCEDURE removeProdutoEstoque_trigger();
