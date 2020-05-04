package EveOnline.Feature;

import EveOnline.Component.IComponent;
import EveOnline.Component.IQuality;
import EveOnline.Ship.IShip;
import MapElite.IFeature;
import MapElite.IFeatureDescriptor;
import MapElite.IPerformance;

import java.util.HashMap;
import java.util.Map;

/**
 * The ShipFeatureDescriptor keeps track of the performance of the ship by mapping a ship feature with a ship performance.
 * Each mapping shows the ships performance for each feature belonging to the ship.
 */
public class ShipFeatureDescriptor implements IShipFeatureDescription {
    private Map<IFeatures, IShipPerformance> shipFeatureSpace;

    /**
     * Initialize a new empty ShipFeatureDescriptor.
     */
    public ShipFeatureDescriptor() {
        this.shipFeatureSpace = new HashMap<>();
    }

    @Override
    public Map<IFeature, IPerformance> getFeatureDescriptions() {
        return new HashMap<>(shipFeatureSpace);
    }

    @Override
    public void updateShipFeatureDescriptions(IShip ship) {
        shipFeatureSpace.clear();
        for (IQuality quality : ship.getQualities()) {
            if (quality != null) {
                updatePerformance(quality);
            }
        }
        for (IComponent rig : ship.getRigs()) {
            if (rig != null) {
                for (IQuality quality : rig.getQualities()) {
                    updatePerformance(quality);
                }
            }
        }
        for (IComponent highModule : ship.getHighModules()) {
            if (highModule != null) {
                for (IQuality quality : highModule.getQualities()) {
                    if (quality != null) {
                        updatePerformance(quality);
                    }
                }
            }
        }
        for (IComponent mediumModule : ship.getMediumModules()) {
            if (mediumModule != null) {
                for (IQuality quality : mediumModule.getQualities()) {
                    if (quality != null) {
                        updatePerformance(quality);
                    }
                }
            }
        }
        for (IComponent lowModule : ship.getLowModules()) {
            if (lowModule != null) {
                for (IQuality quality : lowModule.getQualities()) {
                    updatePerformance(quality);
                }
            }
        }
    }

    /**
     * Update the performance of the feature descriptor for the quality provided.
     *
     * @param quality a IQuality which is used for updating the performance.
     */
    private void updatePerformance(IQuality quality) {
        if (quality != null) {
            for (IFeatures shipFeature : Features.values()) {
                if (shipFeature.contains(quality.getAttributeName())) {
                    if (shipFeatureSpace.containsKey(shipFeature)) {
                        shipFeatureSpace.get(shipFeature).addToScore(quality.getScore());
                    } else {
                        shipFeatureSpace.put(shipFeature, new ShipPerformance(quality.getScore()));
                    }
                }
            }
        }
    }

    @Override
    public int compareTo(IFeatureDescriptor o) {
        int larger = 0;
        int smaller = 0;

        for (IFeatures feature : shipFeatureSpace.keySet()) {
            if (shipFeatureSpace.get(feature).compareTo(o.getFeatureDescriptions().get(feature)) > 0) {
                larger++;
            } else if (shipFeatureSpace.get(feature).compareTo(o.getFeatureDescriptions().get(feature)) < 0) {
                smaller++;
            }
        }

        if (larger > smaller) {
            return 1;
        } else if (smaller > larger) {
            return -1;
        }

        return 0;
    }
}
