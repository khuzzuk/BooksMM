package xmlParsing;

import databaseManager.DBRW;
import libraries.Library;

public aspect XMLParsingAspect {
	before(): call(* manager.QueryMaker.QueryInitializer.initQueries(..)){
		DBRW.initializeDB();
		XMLDataParser.getInstance().initialize();
	}

	after(Library library): call(* databaseManager.DBRW.write(Library))
	&& args(library) {
		XMLDataParser.addLibrary(library);
	}
}
