package az.dynamics.task.exception;

/**
 * @author Kanan
 */
public class ProcessNotFoundException extends RuntimeException  {
    public ProcessNotFoundException(Integer id) {
        super("Process" + id + " not found");
    }
}
