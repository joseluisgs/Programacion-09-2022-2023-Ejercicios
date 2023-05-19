# VehiculosCrudInterface

### Routes Manager

RoutesManager se encarga de controlar las rutas entre los diferentes Stages.

![img_8.png](img_8.png)

### Repositorio

Gestiona las consultas de la base de datos y almacena y gestiona los vehículos

![img_9.png](img_9.png)

### Vista principal
La vista principal está dividida en dos zonas. En la zona de la izquierda tenemos un ListView
con vehículos en la base de datos y una barra de búsqueda. En la zona derecha tenemos la
información del vehículo seleccionado y dos botones "Editar" y "Borrar".

La lista de vehículos (ListView) tiene un "listener" que reacciona cuando se selecciona un
coche para así mostrar la información en la pantalla derecha.

![img.png](img.png)
> Controlador de la vista:
![img_2.png](img_2.png)

### Vista Editar

En la vista editar podremos editar los campos de un vehículo existente en la base de datos 
(menos la matrícula). Para el tipo de motor y los colores he usado un ChoiceBox y para la 
fecha un DatePicker. Al guardar se validan los datos introducidos.

![img_1.png](img_1.png)
> Controlador de la vista:
![img_3.png](img_3.png)

### Vista Borrar

En la vista borrar nos aparecerán los datos del vehículo. Tendremos que pulsar el botón
borrar y confirmar nuestra acción en una alerta de confirmación.

![img_4.png](img_4.png)
> Controlador de la vista:
![img_5.png](img_5.png)

### Vista Agregar

En la vista agregar tendremos una ficha vacía para rellenar con los campos del nuevo
vehículo a añadir. Tendremos que comprobar los datos y una vez validados se habilita el 
botón Guardar.

![img_6.png](img_6.png)

> Controlador de la vista:
![img_7.png](img_7.png)