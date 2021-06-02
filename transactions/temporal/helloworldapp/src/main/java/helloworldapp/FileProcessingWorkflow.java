package helloworldapp;

import java.util.List;
import io.temporal.workflow.QueryMethod;
import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

/**
 * Interfaz del flujo de trabajo. Un flujo de trabajo es una clase que implementa una interfaz de
 * flujo de trabajo. Las interfaces de flujo de trabajo pueden formar jerarquías de herencia Los
 * métodos anotados con (@SignalMethos o @QueryMethod) se pueden invocar durante la ejecución de un
 * flujo de trabajo, así como también cuando el flujo de trabajo esté en estado Completed.
 */
@WorkflowInterface
public interface FileProcessingWorkflow extends Retryable {

    /**
     * Denota el punto de partida de la ejecución del flujo de trabajo. La ejecución del flujo de
     * trabajo se completa cuando regresa este método. Se puede definir solo un método anotado con
     * (@WorkflowMethod) La anotación tiene un parametro name, que se puede utilizar para indicar el
     * tipo de flujo de trabajo, sino se establece toma de forma predeterminada el nombre corto de
     * la interfaz del flujo de trabajo. Este método puede tener cualquier número de parámetros. Se
     * recomienda pasar un solo parámetro que contenga todos los campos de entrada, esto hace
     * compatible con versiones anteriores.
     *
     * @param args
     * @return
     */
    @WorkflowMethod
    String processFile(String args);

    /**
     * Indica que el método se puede utilizar para consultar el estado del flujo de trabajo en
     * cualquier momento durante su ejecución. Puede tener parámetros que se pueden usar para
     * filtrar un subconjunto del estado del flujo de trabajo que devuelve. La anotación contiene un
     * parámetro name para indicar el nombre de la consulta, si no se lo establece toma por defecto
     * el nombre del método.
     *
     * @return
     */
    @QueryMethod(name = "history")
    List<String> getHistory();

    @QueryMethod
    String getStatus();

    /**
     * Indica que es un método de manejo de señales y que puede reaccionar a señales externas. Puede
     * tener parámatros que pueden contener la carga útil de la señal. No devuelven un valor, por lo
     * que debe tener un void como tipo de retorno. La anotación tiene un parámetro name, para
     * indicar el tipo de señal, si no se lo establece se toma por defecto el nombre del método.
     */
    @SignalMethod
    void abandon();
}