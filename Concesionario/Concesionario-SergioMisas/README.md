# CRUD de concesionario

Refactorización del ejercicio de expedientes del respositorio de Programacion 9 para poder almacenar y guardar coches utilizando SQDelight y JavaFX, aparte se ha realizado Railway Oriented Programming.

## Cambios realizados

Se han realizado las correspondientes funciones de un CRUD para los coches, de forma interactiva y visual usando Model-View-ViewModel.

Toda la lógica de la parte visual de la interfaz está en el ViewModel.

Se ha configurado para que se pueda exportar la base de datos en JSON.

## Interfaz

La ventana principal contiene:

- En la parte superior una barra con un menú file para exportar a JSON y un menu ayuda para acceder a la ventana de acerca de

- Una tabla en la parte izquierda en la que aparecen el id, la marca y modelo y la matrícula de los coches, y encima de esta un campo de texto y una caja para filtrar por matrícula y tipo de motor

- En la parte derecha se exponen los datos del coche seleccionado en la tabla y debajo de esta presentación hay botones para crear y editar que llevan a la ventana de edicion/creacion y un boton de eliminar

La ventana acerca de contien información y un link a mi github

La ventana de edición/creación contiene los mismos datos que la presentación de la principal pero esta vez se pueden editar, abajo están los botones de guardar, limpiar los campos y salir.

## Crédito

Hecho por Sergio Misas sobre la base de Jose Luis GS