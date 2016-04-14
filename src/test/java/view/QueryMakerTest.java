package view;

import channels.TaskChannel;
import model.databaseManager.DBRW;
import model.libraries.LibrariesList;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import util.QueryInitializationException;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class QueryMakerTest {

    private DBRW mockedDBRW;
    private LibrariesList mockedList;
    private TaskChannel mockedChannel;
    private LibrariesList.Categories category;
    private QueryMaker maker;

    @BeforeMethod
    public void prepareConstants() throws Exception {
        category = LibrariesList.Categories.ROMANCE;
    }

    @BeforeMethod
    public void setUp() throws Exception {
        maker = new QueryMaker();

        mockedDBRW = mock(DBRW.class);
        maker.dbrw = mockedDBRW;

        mockedList = mock(LibrariesList.class);
        List<String> urls = new ArrayList<>();
        urls.add("quer1");
        urls.add("quer2");
        urls.add("quer3");
        when(mockedList.getAddresses(category)).thenReturn(urls);
        maker.list = mockedList;

        mockedChannel = mock(TaskChannel.class);
        maker.channel = mockedChannel;
    }

    @Test(groups = "fast", expectedExceptions = QueryInitializationException.class)
    public void testFailedInitialization() throws Exception {
        //given
        QueryMaker.QueryInitializer mockedInitializer = mock(QueryMaker.QueryInitializer.class);
        when(mockedInitializer.initQueries()).thenReturn(false);
        maker.initializator = mockedInitializer;
        //when
        maker.startQuery(category);
        //then
    }

    @Test(groups = "fast")
    public void testThreeQueriesReceiver() throws Exception {
        //given
        //when
        maker.startQuery(category);
        //then
        verify(mockedChannel, times(3)).putTask(any());
    }
}
