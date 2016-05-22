package messaging;

import java.util.List;

import channels.MessageChannel;
import libraries.Library;
import messaging.messages.FinishedTaskMessage;
import messaging.messages.Message;
import messaging.messages.ReadLibraryFromDBMessage;
import messaging.messages.WriteToDBMessage;

public aspect MessagePublisher {
	after() returning(Library library): call(* channels.Task.getLibrary(..))
	&& within (channels.TaskChannel.TaskWorker.Worker){
		publish(new WriteToDBMessage(library));
	}
	after(): execution(* databaseManager.DBRW.write(..)){
		publish(new FinishedTaskMessage());
	}
	after() returning(List<Library> libraries): execution(List<Library> databaseManager.DBRW.getLibraryByName(..)) {
		publish(new ReadLibraryFromDBMessage(libraries));
	}
	private void publish(Message message){
		MessageChannel.publish(message);
	}
}
