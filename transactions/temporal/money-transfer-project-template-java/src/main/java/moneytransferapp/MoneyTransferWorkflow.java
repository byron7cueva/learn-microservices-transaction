package moneytransferapp;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface MoneyTransferWorkflow {

    // El iniciador llama al método de flujo de trabajo mediante código o CLI.
    @WorkflowMethod
    void transfer(String fromAccountId, String toAccountId, String referenceId, double amount);
}
