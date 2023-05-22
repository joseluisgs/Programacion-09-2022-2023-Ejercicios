DROP TABLE IF EXISTS TVehiculos;

CREATE TABLE IF NOT EXISTS TVehiculos(
    cId             VARCHAR PRIMARY KEY NOT NULL,
    cMarca          VARCHAR NOT NULL,
    cModelo         VARCHAR NOT NULL,
    cTipoMotor      VARCHAR NOT NULL,
    nKilometraje    REAL    NOT NULL,
    cMatriculacion  VARCHAR NOT NULL,
    cImagen         VARCHAR NOT NULL
);