package moneytransferapp;

import io.temporal.api.common.v1.WorkflowExecution;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.serviceclient.WorkflowServiceStubsOptions;

import java.util.UUID;

/**
 * Inicia el flujo de trabajo
 * La llamada al servidor temporal se la realiza de forma asíncrona
 * ./gradlew initiateTransfer
 */
public class InitiateMoneyTransfer {

    public static void main(String[] args) throws Exception {

        //WorkflowServiceStubs es un contenedor de stubs de gRPC que se comunica con la instancia del servidor temporal.
        WorkflowServiceStubs service = WorkflowServiceStubs.newInstance(
            WorkflowServiceStubsOptions.newBuilder().setTarget("192.168.1.120:7233").build()
        );
        WorkflowOptions options = WorkflowOptions.newBuilder()
                // Indicando la cola de tareas
                .setTaskQueue(Shared.MONEY_TRANSFER_TASK_QUEUE)
                // Un WorkflowId evita que tenga instancias duplicadas, elimínelo para duplicarlo.
                .setWorkflowId("money-transfer-workflow")
                .build();
        // WorkflowClient se puede utilizar para iniciar, señalar, consultar, cancelar y finalizar Workflows.
        WorkflowClient client = WorkflowClient.newInstance(service);
        // WorkflowStubs habilita llamadas a métodos como si el objeto de flujo de trabajo fuera local, pero en realidad realiza un RPC.
        // Creando flujo de trabajo.
        MoneyTransferWorkflow workflow = client.newWorkflowStub(MoneyTransferWorkflow.class, options);
        // Parametros de entrada
        String referenceId = UUID.randomUUID().toString();
        String fromAccount = "001-001";
        String toAccount = "002-002";
        double amount = 18.74;
        // Ejecución asincrónica. Este proceso terminará después de realizar esta llamada.
        WorkflowExecution we = WorkflowClient.start(workflow::transfer, fromAccount, toAccount, referenceId, amount);
        System.out.printf("\nTransfer of $%f from account %s to account %s is processing\n", amount, fromAccount, toAccount);
        System.out.printf("\nWorkflowID: %s RunID: %s", we.getWorkflowId(), we.getRunId());
        System.exit(0);
    }
}
