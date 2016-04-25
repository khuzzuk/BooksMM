package util;

/**
 * Signalize that validation of {@link libraries.Library} object ended with failure.
 */
public class WrongLibraryException extends Exception {
    public WrongLibraryException() {
    }

    public WrongLibraryException(String message) {
        super(message);
    }
}
