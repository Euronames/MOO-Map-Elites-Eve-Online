package JsonParser;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

class Parser {

    private Gson gson;


    Parser() {
        gson = new Gson().newBuilder().enableComplexMapKeySerialization().setLenient().setPrettyPrinting().create();
    }

    String parse(Object object) {
        return gson.toJson(object);
    }


    JsonArray read(String path) {
        String everything = "";
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            everything = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return gson.fromJson(everything, JsonArray.class);
    }


    void findSector(JsonArray jsonArray, List<String> features) {

        JsonArray elements;
        int solutionLocation = 1;
        int sectorLocation = 0;
        JsonArray bestSolutions = null;
        JsonObject bestSector = null;


        for (int i = 0; i < jsonArray.size(); i++) {
            elements = (JsonArray) jsonArray.remove(i);

            JsonObject solution = (JsonObject) elements.get(solutionLocation);
            JsonArray paretofront = solution.getAsJsonArray("paretoFront");

            // Are there any solutions?
            if (paretofront.size() > 0) {

                JsonObject span = (JsonObject) elements.get(sectorLocation);
                JsonObject sectorSpan = span.getAsJsonObject("sectorSpans");

                if (bestSector == null) {
                    bestSector = sectorSpan;
                } else {
                    boolean isContesterBetter = true;

                    for (String feature : features) {
                        JsonObject bestFeatureObject = bestSector.getAsJsonObject(feature.toUpperCase());
                        JsonObject contestingFeatureObject = sectorSpan.getAsJsonObject(feature.toUpperCase());

                        if (bestFeatureObject.get("max").getAsDouble() > contestingFeatureObject.get("max").getAsDouble()) {
                            isContesterBetter = false;
                        }
                    }

                    if (isContesterBetter) {
                        bestSector = sectorSpan;
                        bestSolutions = paretofront;
                    }
                }
            }
        }


        if (bestSolutions != null) {
            for (int i = 0; i < bestSolutions.size(); i++) {
                System.out.println(bestSolutions.get(i));
            }
        } else {
            System.out.println("We did not seem to find a best solution... This seems not so good...");
        }
    }
}
