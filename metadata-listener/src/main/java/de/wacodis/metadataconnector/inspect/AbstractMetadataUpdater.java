/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.wacodis.metadataconnector.inspect;

import de.wacodis.metadataconnector.model.AbstractDataEnvelope;
import de.wacodis.metadataconnector.model.AbstractDataEnvelopeAreaOfInterest;
import de.wacodis.metadataconnector.model.AbstractDataEnvelopeTimeFrame;
import java.util.Arrays;
import org.joda.time.DateTime;

/**
 *
 * @author <a href="mailto:s.drost@52north.org">Sebastian Drost</a>
 */
public abstract class MetadataUpdater<T extends AbstractDataEnvelope> {

    public abstract boolean supportsDataEnvelope(AbstractDataEnvelope dataEnvelop);

    public abstract AbstractDataEnvelope updateDataEnvelope(T existingDataEnvelope, T newDataEnvelope);

    /**
     * Merges two areas of interest together
     *
     * @param aoi01 first area of interest
     * @param aoi02 second area of interest
     * @return merged area of interest
     */
    public AbstractDataEnvelopeAreaOfInterest mergeAreaOfInterest(
            AbstractDataEnvelopeAreaOfInterest aoi01,
            AbstractDataEnvelopeAreaOfInterest aoi02) {
        AbstractDataEnvelopeAreaOfInterest mergedAoi = new AbstractDataEnvelopeAreaOfInterest();
        if (aoi01 != null && aoi02 != null) {
            mergedAoi.setExtent(Arrays.asList(
                    Math.min(aoi01.getExtent().get(0), aoi02.getExtent().get(0)),
                    Math.min(aoi01.getExtent().get(1), aoi02.getExtent().get(1)),
                    Math.max(aoi01.getExtent().get(2), aoi02.getExtent().get(2)),
                    Math.max(aoi01.getExtent().get(3), aoi02.getExtent().get(3))));
        } else if (aoi01 != null && aoi02 == null) {
            mergedAoi.setExtent(aoi01.getExtent());
        } else if (aoi01 == null && aoi02 != null) {
            mergedAoi.setExtent(aoi02.getExtent());;
        }
        return mergedAoi;
    }

    /**
     * Merges two creation dates by resolving the older one
     *
     * @param created01 first creation date
     * @param created02 second creation date
     * @return the older date
     */
    public DateTime mergeCreated(DateTime created01, DateTime created02) {
        if (created01 != null && created02 != null) {
            return created01.isBefore(created02) ? created01 : created02;
        } else if (created01 != null && created02 == null) {
            return created01;
        } else if (created01 == null && created02 != null) {
            return created02;
        } else {
            return null;
        }
    }

    /**
     * Merges two modification dates by resolving the newest one
     *
     * @param modified01 first modification date
     * @param modified02 second modification date
     * @return the newer date
     */
    public DateTime mergeModified(DateTime modified01, DateTime modified02) {
        if (modified01 != null && modified02 != null) {
            return modified01.isAfter(modified02) ? modified01 : modified02;
        } else if (modified01 != null && modified02 == null) {
            return modified01;
        } else if (modified01 == null && modified02 != null) {
            return modified02;
        } else {
            return null;
        }
    }

    /**
     * Merges two timeframes togetger
     *
     * @param timeFrame01 first timeframe
     * @param timeFrame02 second timeframe
     * @return the merged timeframe
     */
    public AbstractDataEnvelopeTimeFrame mergeTimeFrame(AbstractDataEnvelopeTimeFrame timeFrame01, AbstractDataEnvelopeTimeFrame timeFrame02) {
        AbstractDataEnvelopeTimeFrame mergedTimeFrame = new AbstractDataEnvelopeTimeFrame();
        if (timeFrame01 != null && timeFrame02 != null) {
            mergedTimeFrame.setStartTime(
                    timeFrame01.getStartTime().isBefore(timeFrame02.getStartTime())
                    ? timeFrame01.getStartTime()
                    : timeFrame02.getStartTime()
            );
            mergedTimeFrame.setEndTime(
                    timeFrame01.getEndTime().isAfter(timeFrame02.getEndTime())
                    ? timeFrame01.getEndTime()
                    : timeFrame02.getEndTime()
            );
        } else if (timeFrame01 != null && timeFrame02 == null) {
            mergedTimeFrame = timeFrame01;
        } else if (timeFrame01 == null && timeFrame02 != null) {
            mergedTimeFrame = timeFrame02;
        } else {
            mergedTimeFrame = null;
        }
        return mergedTimeFrame;
    }

}
