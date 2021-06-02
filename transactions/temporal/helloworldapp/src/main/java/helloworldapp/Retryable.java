package helloworldapp;

import io.temporal.workflow.SignalMethod;

/**
 * Una interfaz de flujo base puede definir el métodos de señal o de consulta, pero no puede definir
 * ningun método workflow (@WorkflowMethod)
 */
public interface Retryable {

    @SignalMethod
    void retryNow();
}
