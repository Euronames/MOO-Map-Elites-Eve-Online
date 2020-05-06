package JsonParser;

import java.util.List;

public class Reader {

    private Parser parser = new Parser();

    public void readShipList(List<String> featureList) {
        String filename = "illuminatedFeatureSpace.json";
        parser.findSector(parser.read(filename), featureList);
    }
}
