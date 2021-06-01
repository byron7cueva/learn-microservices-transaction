package helloworldapp;

import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.serviceclient.WorkflowServiceStubsOptions;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;

/**
 * Trabajador
 */
public class HelloWorldWorker {

    public static void main(String[] args) {
        // Se crea el contenedor de código de gRPC que se comunica con la instacia del servidor temporal
        WorkflowServiceStubs service = WorkflowServiceStubs.newInstance(
            WorkflowServiceStubsOptions.newBuilder().setTarget("192.168.1.120:7233").build()
        );
        // Cliente
        WorkflowClient client = WorkflowClient.newInstance(service);

        // Creando una fábrica de trabajadores, para crear trabajadores que sondeen la cola de tareas
        WorkerFactory factory = WorkerFactory.newInstance(client);
        // Trabajador
        Worker worker = factory.newWorker(Shared.HELLO_WORLD_TASK_QUEUE);
        // Alojando al trabajador implementacion de flujo de trabajo
        // Los flujos de trabajo tienen estado por lo que deben proporcionar un tipo para crear instancias
        worker.registerWorkflowImplementationTypes(HelloWorldWorkflowImpl.class);
        // Alojando al trabajador implementacion de actividad
        // Las actividades no tienen estado y son seguras para subprocesos, por lo que se utiliza una instancia compartida.
        worker.registerActivitiesImplementations(new FormatImpl());

        // Iniciar el sondeo de la cola de tareas
        factory.start();
    }
}
