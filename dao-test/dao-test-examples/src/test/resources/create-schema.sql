create table points (
    id BIGINT NOT NULL primary key,
    x DOUBLE NOT NULL,
    y DOUBLE NOT NULL,
    CONSTRAINT unique_coords UNIQUE (x, y)
);

create table figures (
    id BIGINT NOT NULL primary key,
    name VARCHAR(255)
);

create table vertexes (
    figure_id BIGINT NOT NULL,
    point_id BIGINT NOT NULL,
    PRIMARY KEY (figure_id, point_id),
    CONSTRAINT fk_vertex_figure FOREIGN KEY (figure_id) REFERENCES figures (id),
    CONSTRAINT fk_vertex_point FOREIGN KEY (point_id) REFERENCES points (id)
);

create sequence point_id_seq;
create sequence figure_id_seq;
