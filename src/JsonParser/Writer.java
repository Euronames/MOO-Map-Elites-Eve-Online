package JsonParser;


import java.io.FileWriter;
import java.io.IOException;

public class Writer {
    private Parser parser;

    public Writer() {
        this.parser = new Parser();
    }

    public void writeObjectAsJsonFile(Object object) {
        String json = parser.parse(object);
        String filename = "illuminatedFeatureSpace.json";
        try (FileWriter fileWriter = new FileWriter(filename)) {
            fileWriter.write(json);
            System.out.println("Successfully Copied JSON Object to File...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
