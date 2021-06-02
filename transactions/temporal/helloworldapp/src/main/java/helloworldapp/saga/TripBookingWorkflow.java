package helloworldapp.saga;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface TripBookingWorkflow {

    @WorkflowMethod
    void bookTrip(String name);
}
