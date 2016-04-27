package ui;

import java.io.IOException;
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
        try {
            emailDialog = new String(Files.readAllBytes(Paths.get(ClassLoader.getSystemResource("Messages.txt").toURI())));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
