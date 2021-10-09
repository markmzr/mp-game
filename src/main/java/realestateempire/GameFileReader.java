package realestateempire;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class GameFileReader {

    public static ArrayList<String[]> readFile(String filename) {
        ArrayList<String[]> fileLines = new ArrayList<>();
        try {
            File file = new File(filename);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                fileLines.add(line.split(","));
            }
            reader.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return fileLines;
    }
}
