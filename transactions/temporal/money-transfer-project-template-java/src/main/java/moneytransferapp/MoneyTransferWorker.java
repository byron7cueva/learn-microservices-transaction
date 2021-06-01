package moneytransferapp;

import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.serviceclient.WorkflowServiceStubsOptions;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;

/**
 * Trabajador
 */
public class MoneyTransferWorker {

    public static void main(String[] args) {

        // WorkflowServiceStubs es un contenedor de stubs de gRPC que se comunica con la instancia del servidor temporal.
        WorkflowServiceStubs service = WorkflowServiceStubs.newInstance(
            WorkflowServiceStubsOptions.newBuilder().setTarget("192.168.1.120:7233").build()
        );
        WorkflowClient client = WorkflowClient.newInstance(service);
        // Worker factory (La fábrica de trabajadores) se utiliza para crear trabajadores que sondean colas de tareas específicas.
        WorkerFactory factory = WorkerFactory.newInstance(client);
        Worker worker = factory.newWorker(Shared.MONEY_TRANSFER_TASK_QUEUE);
        // Este trabajador aloja implementaciones de flujo de trabajo y de actividad.
        // Los flujos de trabajo tienen estado, por lo que se necesita un tipo para crear instancias.
        worker.registerWorkflowImplementationTypes(MoneyTransferWorkflowImpl.class);
        // Las actividades no tienen estado y son seguras para subprocesos, por lo que se utiliza una instancia compartida.
        worker.registerActivitiesImplementations(new AccountActivityImpl());
        // Empiece a escuchar la lista de tareas en cola.
        factory.start();
    }
}
