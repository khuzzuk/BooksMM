package messaging.messages;

import java.lang.annotation.*;

/**
 * Points specific {@link Class} type which be used as a subscribe type.
 * By default it is {@link Message}, which means subscriber won't get
 * any message at all, since {@link Message} is only an interface.
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE, ElementType.TYPE_PARAMETER, ElementType.TYPE_USE})
@Inherited
public @interface MessageType {
    Class<?> type() default Message.class;
}