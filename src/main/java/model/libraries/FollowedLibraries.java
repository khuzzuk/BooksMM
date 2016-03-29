package model.libraries;

import org.apache.log4j.Logger;

import java.util.ArrayList;

/**
 * Created by mmaczka on 21.03.16.
 */
public class FollowedLibraries {
    public ArrayList<BookFinder> listOfLibraries =new ArrayList<BookFinder>();
    public ArrayList<String> stringListForGUI=new ArrayList<String>();
    private Logger logger = Logger.getLogger(FollowedLibraries.class);




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
        try {
            stringListForGUI.add(library.toString());
            listOfLibraries.add(library);
        }catch (Exception e){
            logger.error("tried to extract library from null");
        }
    }
}
