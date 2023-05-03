-- DROP DATABASE IF EXISTS ferragem_avila;

-- CREATE DATABASE ferragem_avila;

-- \c ferragem_avila;

DROP TABLE IF EXISTS item;
DROP TABLE IF EXISTS venda;
DROP TABLE IF EXISTS produto;
DROP FUNCTION IF EXISTS removeProdutoEstoque1;
DROP FUNCTION IF EXISTS removeProdutoEstoque2_trigger;

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
    -- valor_unitario double precision,
    produto_id integer references produto (id),
    venda_id integer references venda (id) ON DELETE CASCADE,
    UNIQUE(produto_id, venda_id)
);

INSERT INTO produto (descricao, preco, estoque, cod_barras) VALUES
('teste1', 100.0, 1000, 123456711);

INSERT INTO produto (descricao, preco, estoque, cod_barras) VALUES
('teste2', 100.0, 1000, 123891055);

CREATE OR REPLACE FUNCTION vendaEstaAberta(integer) RETURNS BOOLEAN AS
$$
DECLARE
    venda_rec RECORD;
BEGIN
    SELECT INTO venda_rec * from venda where id = $1;
    IF (venda_rec.concluida is TRUE) THEN
        RETURN TRUE;
    ELSE
        RETURN FALSE;
    END IF;
END;
$$ LANGUAGE 'plpgsql';

CREATE or REPLACE FUNCTION removeProdutoEstoque1(venda RECORD) RETURNS BOOLEAN AS
$$
DECLARE
    tupla_produto RECORD;
    item_rec RECORD;
BEGIN
    SELECT INTO item_rec * from item where venda_id = venda.id;
    SELECT INTO tupla_produto * from produto where id = item_rec.produto_id; 
    IF (tupla_produto.estoque >= item_rec.quantidade) THEN
        IF (vendaEstaAberta(venda.id)) THEN
            RETURN NULL;
        ELSE
            UPDATE produto SET estoque = estoque - item_rec.quantidade WHERE id = item_rec.produto_id;
            RETURN TRUE;
        END IF;
    ELSE        
        RETURN FALSE;    
    END IF;
END;
$$ LANGUAGE 'plpgsql';

-- #######
CREATE OR replace FUNCTION removeProdutoEstoque2_trigger() RETURNS TRIGGER AS
$$
BEGIN
    IF (removeProdutoEstoque1(NEW)) THEN
        RETURN NEW;
    ELSE
        RAISE EXCEPTION 'Deu erro.';
        RETURN NULL;
    END IF;
END;
$$ LANGUAGE 'plpgsql';

CREATE or replace TRIGGER removeProdutoEstoque2_trigger after UPDATE ON venda FOR EACH ROW EXECUTE PROCEDURE removeProdutoEstoque2_trigger();
