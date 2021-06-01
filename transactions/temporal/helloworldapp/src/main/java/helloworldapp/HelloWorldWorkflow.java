package helloworldapp;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

/**
 * Flujo de trabajo
 */
@WorkflowInterface
public interface HelloWorldWorkflow {

    @WorkflowMethod
    String getGreeting(String name);
}
