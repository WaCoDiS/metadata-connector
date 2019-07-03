/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.wacodis.metadataconnector.update;

import de.wacodis.metadataconnector.model.AbstractDataEnvelope;
import de.wacodis.metadataconnector.model.WacodisProductDataEnvelope;
import org.springframework.stereotype.Component;

/**
 * MetadataUpdater for {@link WacodisProductDataEnvelope} instances
 *
 * @author <a href="mailto:m.rieke@52north.org">Matthes Rieke</a>
 */
@Component
public class WacodisProductMetadataUpdater extends AbstractMetadataUpdater<WacodisProductDataEnvelope> {

    @Override
    public boolean supportsDataEnvelope(AbstractDataEnvelope dataEnvelope) {
        return dataEnvelope instanceof WacodisProductDataEnvelope;
    }

    @Override
    public AbstractDataEnvelope updateDataEnvelope(WacodisProductDataEnvelope existingDataEnvelope, WacodisProductDataEnvelope newDataEnvelope) {
        return existingDataEnvelope;
    }

}
