package EveOnline.DataFetcher;

import EveOnline.Feature.Features;

public class FeatureStringFactory implements IFeatureStringFactory {

    @Override
    public String fetchFeatureData() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Features feature : Features.values()) {
            for (String featureDescription : feature.getFeatureDescriptions()) {
                if (Features.values()[0].equals(feature) && feature.getFeatureDescriptions()[0].equals(featureDescription)) {
                    stringBuilder.append("WHERE attributes.attributeName LIKE '").append(featureDescription).append("' ");
                } else {
                    stringBuilder.append("OR attributes.attributeName LIKE '").append(featureDescription).append("' ");
                }
            }
        }
        return stringBuilder.toString();
    }
}
