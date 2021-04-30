package az.dynamics.task.exception;

/**
 * @author Kanan
 */
public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(Integer id) {
        super("Task " + id + " not found");
    }
}
