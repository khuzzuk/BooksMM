package databaseManager;

import libraries.Library;
import libraries.interpreters.Interpreter;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.*;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import util.WrongLibraryException;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class DBRWTest {

    @Mock
    private File testFile;
    @Mock
    private DBRW.Writer writer;
    @Mock
    private DBRW.DAOWriter daoWriter;
    //@Mock
    //private DBRW.MessageSender sender;
    @Mock
    private DBRW.DAOReader daoReader;
    @InjectMocks
    private DBRW dbrw = DBRW.DBRW;

    /**
     * @return Examples of proper {@link Library} objects to test.
     */
    @DataProvider
    public Object[][] validLibraries(){
        Library library1 = new Library("MyLibrary", "1999/1/1");
        library1.add("1");
        Library library2 = new Library("MyOtherLibrary", "2015/12/1");
        library2.add("2");
        Library library3 = new Library("AnotherLibrary", "2015/12/31");
        library3.add("3");
        GregorianCalendar c = new GregorianCalendar();
        Library library4 = new Library("AnotherLibrary", c.get(Calendar.YEAR)+"/"+(c.get(Calendar.MONTH)+1)+"/"+c.get(Calendar.DAY_OF_MONTH));
        library4.add("4");
        return new Object[][]{{library1},{library2},{library3},{library4}};
    }

    /**
     * @return Examples of invalid {@link Library} objects to test.
     */
    @DataProvider
    public Object[][] invalidLibraries(){
        Library library1 = null;
        Library library2 = new Library();
        Library library3 = new Library(null, "1");
        Library library4 = new Library("1", null);
        Library library5 = new Library("1", "1");
        Library library6 = new Library("AnotherLibrary", "31/12/2215");
        library6.add("6");
        Library library7 = new Library("AnotherLibrary", "31/12/1");
        library6.add("7");
        Library library8 = new Library("AnotherLibrary", "31.12.1");
        library6.add("8");
        Library library9 = new Library("AnotherLibrary", "1");
        library6.add("9");
        Library library10 = new Library("AnotherLibrary", "//1");
        library6.add("10");
        return new Object[][]{{library1},{library2},{library3},{library4},{library5},{library6},{library7},{library8},{library9},{library10}};
    }

    @BeforeMethod(groups = "fast")
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        dbrw.DBRW.libraries = new ArrayList<>();
    }

    /**
     * Here are covered scenarios with {@link WrongLibraryException}.
     * @param library invalid objects.
     * @throws Exception {@link WrongLibraryException}
     */
    @Test(groups = "fast", dataProvider = "invalidLibraries", expectedExceptions = WrongLibraryException.class)
    public void testIfWrongLibraryThrowAnException(Library library) throws Exception {
        DBRW.isLibraryValid(library);
    }

    @Test(groups = "fast", dataProvider = "validLibraries")
    public void testIfWriteToDBValidLibraryObjectIsWorkingCorrectly(Library library) {
        DBRW.write(library);
        int librariesInDB = DBRW.getLibraryByName(library.getName()).size();
        int expectedNumber = 1;
        assertThat(librariesInDB).isEqualTo(expectedNumber);
        verify(writer, times(1)).updateDBFile(testFile, DBRW.DB);
        //verify(daoWriter, times(1)).commitTransaction(library);
    }

    @Test(groups = "fast")
    public void checkIfHarvestingObjectFromDAOWorks() {
        DBRW.getLibrariesFromDB();
        verify(daoReader, times(1)).getLibraries();
    }
}
