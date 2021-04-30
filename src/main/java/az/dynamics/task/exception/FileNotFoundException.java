package az.dynamics.task.exception;

/**
 * @author Kanan
 */
public class FileNotFoundException extends RuntimeException {
    public FileNotFoundException(String path) {
        super(path + " not found");
    }
}
