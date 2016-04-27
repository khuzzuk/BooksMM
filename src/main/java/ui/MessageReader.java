package ui;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MessageReader {
    private String emailDialog;
    public String getEmailDialog(){
        if (emailDialog==null) readEmailDialog();
        return emailDialog;
    }
    private void readEmailDialog(){
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(MessageReader.class.getResourceAsStream("/Messages.txt")))){
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line=reader.readLine())!=null) {
                builder.append(line);
                builder.append("\n");
            }
            emailDialog = builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
