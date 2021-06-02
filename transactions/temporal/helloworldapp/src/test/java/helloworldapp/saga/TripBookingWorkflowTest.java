package helloworldapp.saga;

import static helloworldapp.saga.TripBookingSaga.TASK_QUEUE;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowException;
import io.temporal.client.WorkflowOptions;
import io.temporal.failure.ApplicationFailure;
import io.temporal.testing.TestWorkflowEnvironment;
import io.temporal.worker.Worker;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class TripBookingWorkflowTest {

    private TestWorkflowEnvironment testEnv;
    private Worker worker;
    private WorkflowClient client;

    @Before
    public void setUp() {
        testEnv = TestWorkflowEnvironment.newInstance();
        worker = testEnv.newWorker(TASK_QUEUE);
        worker.registerWorkflowImplementationTypes(TripBookingWorkflowImpl.class);

        client = testEnv.getWorkflowClient();
    }

    @After
    public void tearDown() {
        testEnv.close();
    }

    /**
     * Valida que las actividades predeterminadas hacen que el flujo de trabajo falle
     */
    @Test
    public void testTripBookingFails() {
        worker.registerActivitiesImplementations(new TripBookingActivitiesImpl());
        testEnv.start();

        TripBookingWorkflow workflow = client.newWorkflowStub(TripBookingWorkflow.class,
            WorkflowOptions.newBuilder().setTaskQueue(TASK_QUEUE).build());

        try {
            workflow.bookTrip("trip1");
            Assert.fail("unreachable");
        } catch (WorkflowException e) {
            Assert.assertEquals(
                "Flight booking did not work",
                ((ApplicationFailure) e.getCause().getCause()).getOriginalMessage());
        }
    }

    /**
     * Lógica de flujo de trabajo de prueba unitaria usando actividades simuladas
     */
    @Test
    public void testSAGA() {
        TripBookingActivities activities = Mockito.mock(TripBookingActivities.class);
        Mockito.when(activities.bookHotel("trip1")).thenReturn("HotelBookingID1");
        Mockito.when(activities.reserveCar("trip1")).thenReturn("CarBookingID1");
        Mockito.when(activities.bookFlight("trip1"))
            .thenThrow(new RuntimeException("Flight booking did not work"));
        worker.registerActivitiesImplementations(activities);

        testEnv.start();

        TripBookingWorkflow workflow =
            client.newWorkflowStub(
                TripBookingWorkflow.class,
                WorkflowOptions.newBuilder().setTaskQueue(TASK_QUEUE).build());
        try {
            workflow.bookTrip("trip1");
            Assert.fail("unreachable");
        } catch (WorkflowException e) {
            Assert.assertEquals(
                "Flight booking did not work",
                ((ApplicationFailure) e.getCause().getCause()).getOriginalMessage());
        }

        Mockito.verify(activities).cancelHotel(Mockito.eq("HotelBookingID1"), Mockito.eq("trip1"));
        Mockito.verify(activities).cancelCar(Mockito.eq("CarBookingID1"), Mockito.eq("trip1"));
    }

}
