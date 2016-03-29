package model.libraries;

import model.databaseManager.LibrariesLoader;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;

public class AvailableLibraries {
    public ArrayList<BookFinder> listOfLibraries =new ArrayList<BookFinder>();
    public ArrayList <String>stringListForGUI=new ArrayList<String>();
    Logger logger = Logger.getLogger(AvailableLibraries.class);

    public AvailableLibraries() {



        try {
            listOfLibraries = new LibrariesLoader("src/main/resources/LIBS").loadLibraries();
        } catch (IOException e) {
            logger.fatal("failed to load libraries");
        }


        stringExtractor();

    }
    public AvailableLibraries(LibrariesLoader loader){
        listOfLibraries = loader.loadLibraries();
        stringExtractor();
    }

     public void stringExtractor(){
try {
        for (BookFinder library: listOfLibraries) {
            if(library.toString().isEmpty()) listOfLibraries.remove(library);
           if(!library.toString().isEmpty()) stringListForGUI.add(library.toString());
        }} catch (Exception e){

    logger.error(e);
}
    }

    private BookFinder libraryExtractor(String nameOfLibrary){
        try {
        for (BookFinder library: listOfLibraries) {
            if (nameOfLibrary.equals(library.toString())) return library;
        }
      } catch (Exception e){

            logger.error("tried to extract library from null");
        }
        return null;
    }

    public BookFinder removeAction(String library){
        BookFinder selectedLibrary=libraryExtractor(library);
        listOfLibraries.remove(selectedLibrary);
        stringListForGUI.remove(library);
        return selectedLibrary;

    }

    public void addAction(BookFinder library){
        stringListForGUI.add(library.toString());
        listOfLibraries.add(library);
    }
    public void deleteLibrary(String nameOfLibrary){
        stringListForGUI.remove(nameOfLibrary);
        BookFinder selectedLibrary=libraryExtractor(nameOfLibrary);
        listOfLibraries.remove(selectedLibrary);
    }


}
