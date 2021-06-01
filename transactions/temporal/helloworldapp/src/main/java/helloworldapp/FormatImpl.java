package helloworldapp;

/**
 * Implementacion de la Actividad
 */
public class FormatImpl implements Format {

    @Override
    public String composeGreeting(String name) {
        return "Hello " + name + "!";
    }
}
