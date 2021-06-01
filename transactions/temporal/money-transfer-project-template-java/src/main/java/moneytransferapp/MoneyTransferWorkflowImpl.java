package moneytransferapp;

import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;
import io.temporal.common.RetryOptions;

import java.time.Duration;

/**
 * Funcion de flujo de trabajo
 * Indica al servidor temporal que rastree el estado del flujo de trabajo
 * Luego los trabajadores ejecutarán el código de flujo de trabajo pieza por pieza
 */
public class MoneyTransferWorkflowImpl implements MoneyTransferWorkflow {
    // RetryOptions especifica cómo manejar automáticamente los reintentos cuando las actividades fallan.
    // Políticas de reintento
    private final RetryOptions retryoptions = RetryOptions.newBuilder()
            .setInitialInterval(Duration.ofSeconds(1))
            .setMaximumInterval(Duration.ofSeconds(100))
            .setBackoffCoefficient(2)
            // Numero de reintentos
            .setMaximumAttempts(500)
            .build();
    // Configuraciones de tiempo de espera
    private final ActivityOptions options = ActivityOptions.newBuilder()
            // Las opciones de tiempo de espera especifican cuándo agotar automáticamente las actividades si el proceso está tardando demasiado.
            .setStartToCloseTimeout(Duration.ofSeconds(5))
            // Opcionalmente, proporcione RetryOptions personalizadas.
            // Fallos de reintentos temporales de forma predeterminada, esto es simplemente un ejemplo.
            .setRetryOptions(retryoptions)
            .build();
    // ActivityStubs habilita llamadas a métodos como si el objeto Activity fuera local, pero en realidad realiza un RPC.
    // Las configuraciones de tiempo de espera y las políticas de rendimiento se especifican en el código del flujo de trabajo como opciones de la actividad.
    private final AccountActivity account = Workflow.newActivityStub(AccountActivity.class, options);

    // El método de transferencia es el punto de entrada al flujo de trabajo.
    // Las ejecuciones de métodos de actividad se pueden orquestar aquí o desde otros métodos de actividad.
    @Override
    public void transfer(String fromAccountId, String toAccountId, String referenceId, double amount) {

        account.withdraw(fromAccountId, referenceId, amount);
        account.deposit(toAccountId, referenceId, amount);
    }
}
