# Temporal

* Proporciona primitivas de de confiabilidad, como seguimiento del estado de la aplicación sin interrupciones y tolerante a fallas, reintentos automáticos, tiempos de espera, bases de datos para rastrear los estados de la aplicación, reversiones debido a fallas del proceso.
* Preserva automáticamente el estado de su flujo de trabajo incluso si el servidor no funciona.
* Se puede depurar un problema de ejecución de una actividad, mientras se ejecuta el flujo de trabajo. Se puede encontrar la solución a un problema  y para actualizar el código de flujo de trabajo que esta medio completado, solo se debe reiniciar el trabajador. En el próximo intento programado, el trabajador continuará justo donde el flujo de trabajo estaba fallando y ejecutara con éxito la actividad recién compilada, completando el flujo de trabajo.
* El inicio del flujo de trabajo y el trabajador que lo ejecuta, utilizan la misma lista de tareas en cola.
* Para ejecutar la aplicación debemos iniciar el flujo de trabajo y el trabajador. Se puede iniciar en cualquier orden.
* Aunque Temporal tiene la capacidad de reproducción, se debe concentrar en implementar la lógica comercial y escribir sus flujos de trabajo con el fin de que se ejecuten una sola vez.

## SDK

### Flujo de trabajo

* Son programas resistentes, lo que significa que continuarán ejecutándose incluso en presencia de diferentes condiciones de falla.
* Encapsulan la ejecución / orquestación de tareas que incluyen actividades y flujos de trabajo secundarios.
* También necesitan reaccionar a eventos externos, responder a solicitudes de consulta y lidiar con los tiempos de espera.
* Además de las actividades, un flujo de trabajo también puede orquestar otros flujos de trabajo. A través de Workflow.newChildWorkflowStub.
* Limitaciones en el JAVA:
  * No se debe usar ninguna construcción que dependa de la hora del sistema.
  * No utilizar ninguna variable global mutable en las implementaciones de flujo de trabajo. Esto asegura que múltiples instancias de flujo de trabajo estén completamente aisladas.
  * No llamar a ninguna función no determinista como una función random o aleatoria, directamente desde el código de flujo de trabajo. El SDK proporciona una API para llamar a código no determinista.
  * Realizar todas las operaciones de IO y llamadas de servicios de terceros en actividades y no en flujos de trabajo, ya que generalmente son de naturaleza no determinista.
  * No utilizar ninguna clase de subprocesos múltiples como Thread o ThreadPoolExecutor. Utilizar a su vez Async.function o Aync.procedure, proporcionado por el SDK, para ejecutar código de forma asíncrona.
  * No utilizar ninguna sincronización, bloqueos ni otras clases estándar relacionadas con la cimultaneidad de bloque de Java, además de las proporcionadas por la clase Workflow. No hay necesidad de una sincronización explícita porque el cñodigo de subprocesos múltiples dentro de un flujo de trabajo se ejecuta un subproceso a la vez y bajo un bloqueo global.
    * Llame a WorkflowThread.sleep en lugar de Thread.sleep
    * Utilice Promise y CompletablePromise en lugar de Future y CompletableFuture.
    * Utilice WorkflowQueue en lugar de BlockingQueue.
  * Use Worflow.getVersion al realizar cambios en el código del flujo de trabajo. Sin esto cualquier implementación de código de flujo de trabajo actualizado podría romper los flujos de trabajo que ya se están ejecutando.
  * No acceda a la API de configuraciones directamente desde un flujo de trabajo porque los cambios en la configuración pueden afectar la ruta de ejecución del flujo de trabajo. Páselo como argumento a una función de flujo de trabajo o use una actividad para cargarlo.

### Función de Flujo de trabajo.

* Organiza la ejecución de la ejecución de las funciones de actividad.
* Son funciones que organizan las llamadas a los métodos de actividad.
* Es el punto de entrada de la aplicación.
* Cuando inicia un flujo de trabajo, se esta diciendo al servidor temporal que rastree el estado del flujo de trabajo con esta firma de función.
* Las configuraciones de tiempo de espera y las políticas de rendimiento se especifican en el código del flujo de trabajo como opciones de la actividad.
* Las funciones de flujo de trabajo son donde se configura y organiza la ejecución de las funciones de Actividad.
* El objeto de flujo de trabajo se define como cualquier objeto de java y su interface incluye decoradores temporales.

### Actividad

* Es una función que contiene la lógica empresarial.
* Está diseñadas para manejar código no determinista que podría resultar en resultados inesperados o errores.
* Un objeto de actividad se define como cualquier otro objeto en Java.
* Necesita una interfaz y una implementación. La interfaz incluye decoradores temporales.
* Cuando se reproduce la ejecución de un flujo de trabajo, las actividades ejecutadas con éxito no se vuelven a ejecutar porque sus resultados ya están registrados en el historial de eventos del flujo de trabajo.
* La cantidad de datos que se transfiere a través de los parámetros de invocación de actividad o los valores de retorno, podría generar un gran historial de ejecución y eso afectaría negativamente al rendimiento de su flujo de trabajo.

### Trabajador

* Es un contenedor del flujo de trabajo compilado y código de actividad.
* El único trabajo de un trabajador es ejecutar las funciones de actividad y flujo de trabajo y comunicar los resultados y eventos de ejecución al servidor temporal.
* Es responsable de ejecutar partes del código de flujo de trabajo y actividad.
* Solo puede ejecutar código que se haya registrado en él.
* Sabe qué fragmento de código ejecutar de las tareas que obtiene de la cola de tareas.
* Solo escucha la cola de tareas en la que está registrado.
* Una vez que el trabajador ejecuta el código, devuelve los resultados al servidor temporal.
* Alojan el código de la actividad y flujo de trabajo y ejecuta el código pieza por pieza.
* Aloja funciones de flujo de trabajo y actividad y  las ejecuta una a la vez.
* Se instruye al trabajador para que ejecute las funciones específicas a través de la información que obtiene de la cola de tareas y, después de la ejecución comunica los resultados al servidor.

#### Proceso del trabajador

1. Cuando inicia un trabajador, comienza a sondear la cola de tareas.
2. La primera tarea que encuentra es la que indica que ejecute la función de flujo de trabajo.
3. El trabajador comunica el evento al servidor, lo que hace que el servidor envíe también tareas de actividad a la cola de tareas.
4. Luego el trabajador toma cada una de las tareas de actividad en su orden respectivo de la cola de tareas y ejecuta cada una de las actividades correspondientes.

### Iniciador de flujo de trabajo

* Para iniciar un flujo de trabajo, se debe enviar una señal al servidor temporal para indicarle que realice un seguimiento del estado del flujo de trabajo.
* Hay dos formas de iniciar un flujo de trabajo, a través de la CLI temporal o el SDK temporal.

### Enrutamiento de tareas

* Es un mecanismo integrado para el equilibrio de carga.
* Es cuando un trabajador escucha la misma cola de tareas a la que se envían las tareas de flujo de trabajo y actividad.

### Cola de tareas

* Son la forma en que el servidor temporal proporciona información a los trabajadores.
* Cuando inicia un flujo de trabajo, se indica al servidor que cola de tareas utiliza el flujo de trabajo o las actividades como cola de información.
* Se define mediante un nombre de cadena simple.



Para ejecutar la aplicación se debe realizar los siguiente:

1. Envíar una señal al servidor temporal para iniciar el proceso. El servidor temporal comenzará a rastrear el progreso de la ejecución de la función de flujo de trabajo.
2. Ejecuta un trabajador.

<img src="./img/temporal-high-level-application-design.png" alt="img" style="zoom:100%;" />