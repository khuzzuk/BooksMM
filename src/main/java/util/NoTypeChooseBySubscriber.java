package util;

public class NoTypeChooseBySubscriber extends RuntimeException {
    public NoTypeChooseBySubscriber() {
        super();
    }

    public NoTypeChooseBySubscriber(String message) {
        super(message);
    }
}
