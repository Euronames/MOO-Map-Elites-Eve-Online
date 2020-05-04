package EveOnline;

import MapElite.IElitesController;

/**
 * The IShipBuilder is the facade interface for the EveOnline layer.
 * Extends the IElitesController which interfaces with the MapELite layer.
 * <p>
 * The idea is that this interface might evolve on the basis need of an UI controller for fetching ship data.
 */
public interface IShipBuilder extends IElitesController {
}
