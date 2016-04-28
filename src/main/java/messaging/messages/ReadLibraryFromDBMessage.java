package messaging.messages;

import libraries.Library;

import java.util.List;

@MessageType
public class ReadLibraryFromDBMessage implements Message {
    List<Library> libraries;

    public ReadLibraryFromDBMessage(List<Library> libraries) {
        this.libraries = libraries;
    }

    public List<Library> getLibraries() {
        return libraries;
    }
}
