package libraries.interpreters;

import libraries.Library;

import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

class FreebookshubInterpreter extends Interpreter {
    private static final String pattern = getPattern();
    final String url;

    public FreebookshubInterpreter(String url) {
        super();
        this.url = url;
        name = "freebookshub";
    }

    private static String getPattern() {
        try (Scanner scanner = new Scanner(new InputStreamReader(GoodreadsInterpreter.class.getResourceAsStream("/LIBS"), "UTF-8"))) {
            String line;
            while ((line = scanner.nextLine()) != null) {
                if (line.equals("http://www.freebookshub.com/")) {
                    return scanner.nextLine();
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Library getQuery() {
        return getQuery(url, pattern);
    }
}
