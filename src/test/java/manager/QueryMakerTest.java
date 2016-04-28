package manager;

import channels.TaskChannel;
import databaseManager.DBRW;
import libraries.LibrariesList;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import util.QueryInitializationException;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class QueryMakerTest {

    private TaskChannel mockedChannel;
    private LibrariesList.Categories category;
    private QueryMaker maker;

    @BeforeMethod(groups = "fast")
    public void prepareConstants() throws Exception {
        category = LibrariesList.Categories.ROMANCE;
    }

    @BeforeMethod(groups = "fast")
    public void setUp() throws Exception {
        maker = new QueryMaker();

        DBRW mockedDBRW = mock(DBRW.class);
        maker.dbrw = mockedDBRW;

        LibrariesList mockedList = mock(LibrariesList.class);
        List<String> urls = new ArrayList<>();
        urls.add("bookrix");
        urls.add("bookrix");
        urls.add("bookrix");
        when(mockedList.getAddresses(category)).thenReturn(urls);
        maker.list = mockedList;

        mockedChannel = mock(TaskChannel.class);
        maker.channel = mockedChannel;
    }

    @Test(groups = "fast", expectedExceptions = QueryInitializationException.class)
    public void testWrongCaseScenarios() throws Exception {
        //given
        QueryMaker.QueryInitializer mockedInitializer = mock(QueryMaker.QueryInitializer.class);
        when(mockedInitializer.initQueries()).thenReturn(false);
        maker.initializator = mockedInitializer;
        //when
        maker.startQuery(category);
        //then
    }

    @Test(groups = "fast")
    public void checkThatQueryManagerCanHandleFinishingMessagesProperly() throws Exception {
        //when
        maker.startQuery(category);
        //then
        verify(mockedChannel, times(3)).putTask(any());
    }
}
