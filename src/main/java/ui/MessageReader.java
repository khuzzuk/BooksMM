package ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MessageReader {
    private String emailDialog;
    public String getEmailDialog(){
        if (emailDialog==null) readEmailDialog();
        return emailDialog;
    }
    private void readEmailDialog(){
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(MessageReader.class.getResourceAsStream("/Messages.txt"), "UTF-8"))){
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
