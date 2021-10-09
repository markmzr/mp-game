package realestateempire.graphics.text;

import java.io.File;

public enum Font {

    REGULAR("Text/Regular/"),
    BOLD("Text/Bold/");

    private final String fontDir;
    private final String[] charFilenames;

    Font(String fontDir) {
        this.fontDir = fontDir;
        charFilenames = initCharacterFilenames();
    }

    public String getFontDir() {
        return fontDir;
    }

    public String[] getCharFilenames() {
        return charFilenames;
    }

    private String[] initCharacterFilenames() {
        File characterDir = new File("./Textures/" + fontDir);
        return characterDir.list();
    }
}
