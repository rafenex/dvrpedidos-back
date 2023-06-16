CREATE TABLE categoria (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255)
);

CREATE TABLE produto (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255),
    valor_padrao DECIMAL(10, 2),
    categoria_id INT,
    FOREIGN KEY (categoria_id) REFERENCES categoria (id)
);

CREATE TABLE pedido (
    id INT AUTO_INCREMENT PRIMARY KEY,
    data DATE,
    cliente_id INT,
    FOREIGN KEY (cliente_id) REFERENCES cliente (id)
);

CREATE TABLE item_pedido (
    id INT AUTO_INCREMENT PRIMARY KEY,
    produto_id INT,
    quantidade BIGINT,
    cor VARCHAR(255),
    preco DECIMAL(10, 2),
    FOREIGN KEY (produto_id) REFERENCES produto (id)
);

CREATE TABLE pedido_item_pedido (
    pedido_id INT,
    item_pedido_id INT,
    FOREIGN KEY (pedido_id) REFERENCES pedido (id),
    FOREIGN KEY (item_pedido_id) REFERENCES item_pedido (id)
);

