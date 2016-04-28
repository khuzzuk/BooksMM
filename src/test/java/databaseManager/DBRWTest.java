package databaseManager;

import libraries.Library;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.*;
import util.WrongLibraryException;

import java.io.File;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class DBRWTest {

    @Mock
    private File testFile;
    @Mock
    private DBRW.Writer writer;
    @Mock
    private DBRW.DAOWriter daoWriter;
    @Mock
    private DBRW.MessageSender sender;
    @InjectMocks
    private DBRW dbrw = DBRW.DBRW;

    /**
     * Example proper {@link Library} objects to test.
     * @return Array of {@link Library} objects;
     */
    @DataProvider
    public Object[][] validLibraries(){
        Library library1 = new Library("MyLibrary", "1/1/1999");
        library1.add("1");
        Library library2 = new Library("MyOtherLibrary", "1/12/2015");
        library2.add("2");
        Library library3 = new Library("AnotherLibrary", "31/12/2015");
        library3.add("3");
        return new Object[][]{{library1},{library2},{library3}};
    }

    /**
     * Examples of invalid {@link Library} objects to test.
     * @return Array of {@link Library} objects;
     */
    @DataProvider
    public Object[][] invalidLibraries(){
        Library library1 = null;
        Library library2 = new Library();
        Library library3 = new Library(null, "1");
        Library library4 = new Library("1", null);
        Library library5 = new Library("1", "1");
        return new Object[][]{{library1},{library2},{library3},{library4},{library5}};
    }

    @BeforeMethod(groups = "fast")
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        DBRW.libraries = new ArrayList<>();
    }

    /**
     * Here are covered scenarios with {@link WrongLibraryException}.
     * @param library invalid objects.
     * @throws Exception {@link WrongLibraryException}
     */
    @Test(groups = "fast", dataProvider = "invalidLibraries")
    public void shouldThrowWLException(Library library) throws Exception {
        //given
        //when
        boolean operationStatusCorrectness = DBRW.write(library);
        //then
        assertThat(operationStatusCorrectness).isFalse();
    }

    /**
     * Here are covered scenarios with valid {@link Library} objects.
     * @param library valid {@link Library}.
     */
    @Test(groups = "fast", dataProvider = "validLibraries")
    public void testWriteToNewXml(Library library) {
        DBRW.write(library);
        int librariesInDB = DBRW.getLibraryByName(library.getName()).size();
        int expectedNumber = 1;
        assertThat(librariesInDB).isEqualTo(expectedNumber);
        verify(writer, times(1)).updateDBFile(testFile, DBRW.DB);
        verify(daoWriter, times(1)).commitTransaction(library);
    }
}
