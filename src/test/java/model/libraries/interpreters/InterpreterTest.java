package model.libraries.interpreters;

import model.libraries.Library;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class InterpreterTest {
    @Test(groups = "fast")
    public void testGettingLibraryObject() {
        Interpreter interpreter = new Interpreter() {
            @Override
            public Library getQuery() {
                return null;
            }
        };
        Library library = interpreter.getLibraryInstace();
        assertThat(library).isNotNull();
    }
}
