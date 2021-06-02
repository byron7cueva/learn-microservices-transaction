package helloworldapp.saga;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowException;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.serviceclient.WorkflowServiceStubsOptions;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;

public class TripBookingSaga {

    static final String TASK_QUEUE = "TripBooking";

    public static void main(String[] args) {
        // Contenedor de stubs de gRPC que se comunica con la instancia de la ventana acoplable local del servicio temporal
        WorkflowServiceStubs service = WorkflowServiceStubs.newInstance(
            WorkflowServiceStubsOptions.newBuilder().setTarget("192.168.1.120:7233").build()
        );
        // Cliente que se puede utilizar para iniciar y señalar flujos de trabajo
        WorkflowClient client = WorkflowClient.newInstance(service);

        // Fábrica de trabajadores que se puede utilizar para crear trabajadores para colas de tareas específicas
        WorkerFactory factory = WorkerFactory.newInstance(client);

        // Trabajador que escucha en una cola de tareas y aloja implementaciones de flujo de trabajo y actividades
        Worker worker = factory.newWorker(TASK_QUEUE);

        // Los flujos de trabajo tienen estado. Entonces necesitas un tipo para crear instancias.
        worker.registerWorkflowImplementationTypes(TripBookingWorkflowImpl.class);

        // Las actividades son apátridas y seguras para subprocesos. Entonces se usa una instancia compartida
        TripBookingActivities tripBookingActivities = new TripBookingActivitiesImpl();
        worker.registerActivitiesImplementations(tripBookingActivities);

        // Iniciar todos los trabajadores creados por esta fábrica
        factory.start();
        System.out.println("Worker started for task queue: " + TASK_QUEUE);

        // Ahora podemos comenzar a ejecutar instancias de nuestra saga: su estado se mantendrá
        WorkflowOptions options = WorkflowOptions.newBuilder()
            .setTaskQueue(TASK_QUEUE)
            .build();
        TripBookingWorkflow trip1 = client.newWorkflowStub(TripBookingWorkflow.class, options);
        try {
            trip1.bookTrip("trip1");
        } catch (WorkflowException e) {
            // Expected
        }

        try {
            TripBookingWorkflow trip2 = client.newWorkflowStub(TripBookingWorkflow.class, options);
            trip2.bookTrip("trip2");
        } catch (WorkflowException e) {
            // Expected
        }

        System.exit(0);
    }

}
