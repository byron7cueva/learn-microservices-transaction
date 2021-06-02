package helloworldapp.saga;

import io.temporal.activity.ActivityInterface;

@ActivityInterface
public interface TripBookingActivities {

    /**
     * Solicitar una reserva de alquiler de coche
     *
     * @param name Nombre del cliente
     * @return reservationID
     */
    String reserveCar(String name);

    /**
     * Solicitar una reserva de vuelo
     *
     * @param name Nombre del cliente
     * @return reservationID
     */
    String bookFlight(String name);

    /**
     * Solicitar reservación de hotel
     *
     * @param name Nombre del cliente
     * @return reservationID
     */
    String bookHotel(String name);

    /**
     * Cancelar la reservación de vuelo
     *
     * @param reservationID id retornado por bookFlight
     * @param name Nombre del cliente
     * @return cancellationConfirmationID
     */
    String cancelFlight(String reservationID, String name);

    /**
     * Cancelar la reservación del hotel
     *
     * @param reservationID id retornado por bookHotel
     * @param name Nombre del cliente
     * @return cancellationConfirmationID
     */
    String cancelHotel(String reservationID, String name);

    /**
     * Cancelar la reversión del coche
     *
     * @param reservationID id retornado de reserveCar
     * @param name Nombre del cliente
     * @return cancellationConfirmationID
     */
    String cancelCar(String reservationID, String name);

}
