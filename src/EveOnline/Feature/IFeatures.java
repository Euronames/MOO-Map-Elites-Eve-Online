package EveOnline.Feature;

import MapElite.IFeature;

/**
 * The IFeatures interface provides a general interface for handling Features within the Eve Online layer.
 * <p>
 * The IFeatures extends IFeature as the features implementing is used within the MapElites layer.
 */
public interface IFeatures extends IFeature {
    /**
     * Check whether an feature description in contained by this feature.
     *
     * @param description A String description which is a unique definition of a feature.
     * @return true if the feature contains the provided description, otherwise false.
     */
    boolean contains(String description);
}
