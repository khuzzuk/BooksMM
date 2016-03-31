import org.testng.annotations.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class MyClassTest {
    @Test
    public void testIfWillCreateFile() {
        MyClass myClass = new MyClass();
        MyClass.MyClassDelegator mockedDelegator = mock(MyClass.MyClassDelegator.class);
        File mockedFile = mock(File.class);
        when(mockedDelegator.createFile("name")).thenReturn(mockedFile);
        when(mockedFile.exists()).thenReturn(true);
        myClass.delegator = mockedDelegator;
        boolean isFileCreated = myClass.doStuff("name");
        verify(mockedDelegator).createFile("name");
        assertThat(isFileCreated).isTrue();
    }
}
