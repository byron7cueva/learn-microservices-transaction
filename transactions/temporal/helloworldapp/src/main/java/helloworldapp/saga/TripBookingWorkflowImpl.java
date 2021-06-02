package helloworldapp.saga;

import java.time.Duration;
import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.failure.ActivityFailure;
import io.temporal.workflow.Saga;
import io.temporal.workflow.Workflow;

public class TripBookingWorkflowImpl implements TripBookingWorkflow {

    private final ActivityOptions options = ActivityOptions.newBuilder()
        .setStartToCloseTimeout(Duration.ofHours(1))
        // Deshabilitar reintentos, por ejemplo, para correr más rápido
        .setRetryOptions(RetryOptions.newBuilder()
            .setMaximumAttempts(1)
            .build())
        .build();

    private final TripBookingActivities activities = Workflow
        .newActivityStub(TripBookingActivities.class, options);

    @Override
    public void bookTrip(String name) {
        // Configurar SAGA para ejecutar actividades de compensación en paralelo
        Saga.Options sagaOptions = new Saga.Options.Builder().setParallelCompensation(true).build();
        Saga saga = new Saga(sagaOptions);

        try {
            String carReservationID = activities.reserveCar(name);
            saga.addCompensation(activities::cancelCar, carReservationID, name);

            String hotelReservationID = activities.bookHotel(name);
            saga.addCompensation(activities::cancelHotel, hotelReservationID, name);

            String flightReservationID = activities.bookFlight(name);
            saga.addCompensation(activities::cancelFlight, flightReservationID, name);
        } catch (ActivityFailure e) {
            saga.compensate();
            throw e;
        }
    }
}
