CREATE TABLE IF NOT EXISTS tVehicle(
    nIdVehicle INTEGER PRIMARY KEY AUTOINCREMENT,
    cMarca TEXT NOT NULL,
    cModelo TEXT NOT NULL,
    cMotor TEXT NOT NULL,
    dMatriculacion TEXT NOT NULL,
    cImageUrl TEXT NULL
);

INSERT INTO tVehicle(cMarca, cModelo, cMotor, dMatriculacion, cImageUrl) VALUES ('Audi', 'A4', 'DIESEL', '2007-12-03', null);