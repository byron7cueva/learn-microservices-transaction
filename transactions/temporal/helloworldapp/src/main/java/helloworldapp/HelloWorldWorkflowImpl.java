package helloworldapp;

import java.time.Duration;
import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;

/**
 * Implementacion del flujo de trabajo Cada vez que se inicia una nueva ejecución de flujo de
 * trabajo, se crea una nueva instancia del objeto de la implememtación.
 */
public class HelloWorldWorkflowImpl implements HelloWorldWorkflow {

    // Opciones de la actividad
    ActivityOptions options = ActivityOptions.newBuilder()
        .setScheduleToCloseTimeout(Duration.ofSeconds(2))
        .build();

    // Habilita las llamadas a actividades como mñetodos locales utilizando RPC
    private final Format format = Workflow.newActivityStub(Format.class, options);

    /**
     * Punto de entrada al flujo de trabajo Tan pronto como este método regrese, la ejecución del
     * flujo de trabajo se considera completa.
     *
     * @param name
     * @return
     */
    @Override
    public String getGreeting(String name) {
        // Si hubieran otros métodos de actividad, serñian orquestados aquí o desde dentro de otras actividades
        return format.composeGreeting(name);
    }
}
