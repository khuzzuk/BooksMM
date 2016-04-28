package messaging.messages;

/**
 * Marker interface. This is required in checking in generics type. Also note, that classes implementing this
 * interface should be noted with {@link MessageType} annotation.
 */
@MessageType
public interface Message {
}
