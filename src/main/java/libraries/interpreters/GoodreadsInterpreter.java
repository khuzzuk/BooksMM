package libraries.interpreters;

import libraries.Library;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class GoodreadsInterpreter extends Interpreter {
    private static final String pattern = getPattern();

    private final String url;

    public GoodreadsInterpreter(String url) {
        super();
        this.url = url;
        name = "goodreads";
    }

    @Override
    public Library getQuery() {
        return getQuery(url, pattern);
    }

    private static String getPattern() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(GoodreadsInterpreter.class.getResourceAsStream("/LIBS"), "UTF-8"))) {
            String line;
            while ((line=reader.readLine())!=null){
                if (line.equals("http://www.goodreads.com/ebooks")){
                    return reader.readLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
