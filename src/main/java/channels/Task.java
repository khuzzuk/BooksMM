package channels;

import model.libraries.Library;
import model.libraries.interpreters.Interpreter;

public class Task {
    private Interpreter interpreter;

    public Task(Interpreter interpreter) {
        this.interpreter = interpreter;
    }

    public Interpreter getInterpreter() {
        return interpreter;
    }

    public Library getLibrary() {
        return interpreter.getQuery();
    }
}
