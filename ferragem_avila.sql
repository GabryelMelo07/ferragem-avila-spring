/* DROP DATABASE IF EXISTS ferragem_avila;

CREATE DATABASE ferragem_avila;

\c ferragem_avila; */

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
('teste1', 100.0, 1000, 123456711);

INSERT INTO produto (descricao, preco, estoque, cod_barras) VALUES
('teste2', 100.0, 1000, 123891055);

CREATE FUNCTION removeProdutoEstoque(INTEGER, INTEGER) RETURNS BOOLEAN AS
$$
DECLARE
    resultado BOOLEAN;
    prod ALIAS FOR $1;
    item_id ALIAS FOR $2;
    registro INTEGER;
BEGIN
    IF prod = null and item_id = null THEN
        resultado := false;
    ELSE
        resultado := true;
        SELECT quantidade INTO registro FROM item WHERE id = item_id;
        UPDATE produto SET estoque = (estoque * 1) - registro WHERE id = prod;
    END IF;
    RETURN resultado;
END;
$$ LANGUAGE 'plpgsql';

CREATE FUNCTION removeProdutoEstoque_trigger() RETURNS TRIGGER AS
$$
BEGIN
    IF (removeProdutoEstoque(NEW.produto_id, NEW.id) = true) THEN
        RETURN NEW;
    ELSE
        RAISE EXCEPTION 'Deu erro.';
        RETURN NULL;
    END IF;
END;
$$ LANGUAGE 'plpgsql';

CREATE TRIGGER removeProdutoEstoque_trigger BEFORE INSERT OR UPDATE ON item FOR EACH ROW EXECUTE PROCEDURE removeProdutoEstoque_trigger();
