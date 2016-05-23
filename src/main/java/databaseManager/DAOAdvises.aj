package databaseManager;

import databaseManager.DBRW.QueryFromDBChanneller;
import messaging.messages.QueryFromDBMessage;
import messaging.subscribers.DBWriter;

public aspect DAOAdvises {
	after(): execution(* databaseManager.DBRW.initializeDB()){
		new DBWriter();
	}
	after(): execution(databaseManager.DBRW.new()){
		new QueryFromDBChanneller();
	}
	after(QueryFromDBMessage message): 
		execution(* messaging.subscribers.QueryFromDBChanneller.receive(..))
		&& args (message){
		DBRW.QueryLibraries(message.getLibraryName());
	}
}
