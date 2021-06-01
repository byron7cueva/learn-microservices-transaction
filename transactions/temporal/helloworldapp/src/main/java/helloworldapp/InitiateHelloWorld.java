package helloworldapp;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.serviceclient.WorkflowServiceStubsOptions;

/**
 * Iniciador de flujo de trabajo
 */
public class InitiateHelloWorld {

    public static void main(String[] args) throws Exception {
        // Contenedor de código stub gRPC el cual se comunica con la instacia del servidor temporal
        WorkflowServiceStubs service = WorkflowServiceStubs.newInstance(
            WorkflowServiceStubsOptions.newBuilder().setTarget("192.168.1.120:7233").build()
        );

        // El cliente se utiliza para iniciar, señalar, consultar, cancelar y finalizar workflows
        WorkflowClient client = WorkflowClient.newInstance(service);

        // Opciones del flujo de trabajo
        WorkflowOptions options = WorkflowOptions.newBuilder()
            .setTaskQueue(Shared.HELLO_WORLD_TASK_QUEUE)
            .build();

        // Habilita las llamadas a métodos como si el objeto de flujo de trabajo fuera local, pero es RPC (Remote Procedure Call)
        HelloWorldWorkflow workflow = client.newWorkflowStub(HelloWorldWorkflow.class, options);

        // Ejecuta sincrónicamente el flujo de trabajo y espera la respuesta.
        String greeting = workflow.getGreeting("World");
        System.out.println(greeting);
        System.exit(0);
    }
}
