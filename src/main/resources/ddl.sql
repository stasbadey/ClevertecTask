CREATE TABLE product
(
    id    serial      PRIMARY KEY,
    price decimal     NOT NULL,
    name  varchar(50) NOT NULL
);

CREATE TABLE discount_card
(
    id             serial      PRIMARY KEY,
    number_of_card varchar(50) NOT NULL,
    percentage     int         NOT NULL
);

