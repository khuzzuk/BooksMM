package messaging.subscribers;

import messaging.messages.Message;

import java.lang.annotation.*;

@Documented
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
@Inherited
public @interface SubscriptionType {
    Class<? extends Message> type() default Message.class;
}
