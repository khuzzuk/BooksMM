package channels;

import libraries.Library;
import libraries.interpreters.Interpreter;

/**
 * Object that can be the element of a {@link TaskChannel} queue. It may be processed with
 * proprietary worker in another thread. In order to process a task you must provide it
 * with an {@link Interpreter} instance that then will be processed. It can't be null and will
 * throw {@link NullPointerException} as you instantiate this object.
 */
public class Task {
    private Interpreter interpreter;

    /**
     * Constructor supports fail fast with null arguments. The {@link Interpreter} instance shouldn't be
     * null and if it is, constructor will throw {@link NullPointerException}. Then you must put this task to
     * the current {@link TaskChannel} object.
     * @param interpreter object that implements {@link Interpreter} interface. Can't be null.
     */
    public Task(Interpreter interpreter) {
        if (interpreter==null) throw new NullPointerException();
        this.interpreter = interpreter;
    }

    Interpreter getInterpreter() {
        return interpreter;
    }

    Library getLibrary() {
        return interpreter.getQuery();
    }
}
