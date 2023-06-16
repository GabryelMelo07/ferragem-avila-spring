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
    id serial,
    descricao text not null,
    preco double precision,
    estoque integer default 0 CHECK (estoque >= 0),
    cod_barras bigint unique,
    ativo boolean default true,
    primary key (id)
);

CREATE TABLE venda (
    id serial,
    concluida boolean DEFAULT FALSE,
    -- vendedor_id integer references vendedor (id),
    data_hora timestamp default current_timestamp,
    forma_pagamento varchar(100),
    primary key (id)
);

CREATE TABLE item ( 
    id serial,
    quantidade integer,
    preco_item double precision,
    produto_id integer,
    venda_id integer,
    foreign key (produto_id) references produto (id),
    foreign key (venda_id) references venda (id) ON DELETE CASCADE,
    UNIQUE(produto_id, venda_id),
    primary key(id)
);

INSERT INTO produto (descricao, preco, estoque, cod_barras) VALUES
('Prego', 19.99, 100, 123456711),
('teste2', 100.0, 1000, 123891055),
('semEstoque', 100.0, 1, 1121212),
('semEstoque1', 100.0, 1, 1213123213),
('semEstoque2', 100.0, 1, 414441141),
('semEstoque3', 100.0, 1, 564545454),
('semEstoque4', 100.0, 1, 454545663),
('semEstoque5', 100.0, 1, 678687),
('semEstoque6', 100.0, 1, 575674),
('semEstoque7', 100.0, 1, 34534637),
('semEstoque8', 100.0, 1, 5676575734),
('semEstoque9', 100.0, 1, 2345345634),
('semEstoque10', 100.0, 1, 2436256252),
('semEstoque11', 100.0, 1, 64566456456),
('semEstoque12', 100.0, 1, 4535345345),
('semEstoque13', 100.0, 1, 2323425),
('semEstoque14', 100.0, 1, 34636363),
('semEstoque15', 100.0, 1, 1132342342),
('semEstoque16', 100.0, 1, 4532536),
('semEstoque17', 100.0, 1, 7658678),
('semEstoque18', 100.0, 1, 34534535),
('semEstoque19', 100.0, 1, 56787575),
('semEstoque20', 100.0, 1, 56784563),
('semEstoque21', 100.0, 1, 4325263246),
('semEstoque22', 100.0, 1, 67576575),
('semEstoque23', 100.0, 1, 25626246),
('semEstoque24', 100.0, 1, 35463663),
('semEstoque25', 100.0, 1, 23563456);

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
