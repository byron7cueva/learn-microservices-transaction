# Temporal

* Proporciona primitivas de de confiabilidad, como seguimiento del estado de la aplicación sin interrupciones y tolerante a fallas, reintentos automáticos, tiempos de espera, bases de datos para rastrear los estados de la aplicación, reversiones debido a fallas del proceso.
* Preserva automáticamente el estado de su flujo de trabajo incluso si el servidor no funciona.
* Se puede depurar un problema de ejecución de una actividad, mientras se ejecuta el flujo de trabajo. Se puede encontrar la solución a un problema  y para actualizar el código de flujo de trabajo que esta medio completado, solo se debe reiniciar el trabajador. En el próximo intento programado, el trabajador continuará justo donde el flujo de trabajo estaba fallando y ejecutara con éxito la actividad recién compilada, completando el flujo de trabajo.
* El inicio del flujo de trabajo y el trabajador que lo ejecuta, utilizan la misma lista de tareas en cola.

## SDK

### Función de flujo de trabajo.

* Organiza la ejecución de la ejecución de las funciones de actividad.

### Trabajador

* Es un contenedor del flujo de trabajo compilado y código de actividad.
* El único trabajo de un trabajador es ejecutar las funciones de actividad y flujo de trabajo y comunicar los resultados y eventos de ejecución al servidor temporal.
* Es responsable de ejecutar partes del código de flujo de trabajo y actividad.
* Solo puede ejecutar código que se haya registrado en él.
* Sabe qué fragmento de código ejecutar de las tareas que obtiene de la cola de tareas.
* Solo escucha la cola de tareas en la que está registrado.
* Una vez que el trabajador ejecuta el código, devuelve los resultados al servidor temporal.

#### Proceso del trabajador

1. Cuando inicia un trabajador, comienza a sondear la cola de tareas.
2. La primera tarea que encuentra es la que indica que ejecute la función de flujo de trabajo.
3. El trabajador comunica el evento al servidor, lo que hace que el servidor envíe también tareas de actividad a la cola de tareas.
4. Luego el trabajador toma cada una de las tareas de actividad en su orden respectivo de la cola de tareas y ejecuta cada una de las actividades correspondientes.

### Enrutamiento de tareas

* Es un mecanismo integrado para el equilibrio de carga.
* Es cuando un trabajador escucha la misma cola de tareas a la que se envían las tareas de flujo de trabajo y actividad.

### Función del Flujo de Trabajo

* Es el punto de entrada de la aplicación.
* Cuando inicia un flujo de trabajo, se esta diciendo al servidor temporal que rastree el estado del flujo de trabajo con esta firma de función.
* Las configuraciones de tiempo de espera y las políticas de rendimiento se especifican en el código del flujo de trabajo como opciones de la actividad.

### Cola de tareas

* Se define mediante un nombre de cadena simple.



Para ejecutar la aplicación se debe realizar los siguiente:

1. Envíar una señal al servidor temporal para iniciar el proceso. El servidor temporal comenzará a rastrear el progreso de la ejecución de la función de flujo de trabajo.
2. Ejecuta un trabajador.

<img src="./img/temporal-high-level-application-design.png" alt="img" style="zoom:100%;" />