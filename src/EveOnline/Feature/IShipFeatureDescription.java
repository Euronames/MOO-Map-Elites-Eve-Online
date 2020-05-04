package EveOnline.Feature;

import EveOnline.Ship.IShip;
import MapElite.IFeatureDescriptor;

/**
 * The IShipFeatureDescription interface is used as a layer specific interface which defines the public access into a
 * ship feature descriptor.
 * <p>
 * Note that the IShipFeatureDescription extends the IFeatureDescriptor from the MapElite layer.
 */
public interface IShipFeatureDescription extends IFeatureDescriptor {

    /**
     * Update the ship feature description based on the ship details.
     *
     * @param ship a Ship which is used to update the feature description.
     */
    void updateShipFeatureDescriptions(IShip ship);
}
