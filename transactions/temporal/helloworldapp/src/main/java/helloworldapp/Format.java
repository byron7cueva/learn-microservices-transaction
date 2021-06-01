package helloworldapp;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

/**
 * Actividad
 */
@ActivityInterface
public interface Format {

    @ActivityMethod
    String composeGreeting(String name);
}
