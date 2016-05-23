package messaging;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import messaging.messages.Message;

@Retention(RetentionPolicy.RUNTIME)
public @interface Publisher {
	Class<? extends Message> type();
}
