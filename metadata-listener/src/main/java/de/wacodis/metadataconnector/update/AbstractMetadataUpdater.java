/*
 * Copyright 2019-2022 52°North Spatial Information Research GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.wacodis.metadataconnector.update;

import de.wacodis.metadataconnector.model.AbstractDataEnvelope;
import de.wacodis.metadataconnector.model.AbstractDataEnvelopeAreaOfInterest;
import de.wacodis.metadataconnector.model.AbstractDataEnvelopeTimeFrame;
import java.util.Arrays;
import org.joda.time.DateTime;

/**
 *
 * @author <a href="mailto:s.drost@52north.org">Sebastian Drost</a>
 */
public abstract class AbstractMetadataUpdater<T extends AbstractDataEnvelope> {

    /**
     * Checks if the updater supports a certain {@link AbstractDataEnvelope} implementation
     *
     * @param dataEnvelope {@link AbstractDataEnvelope} that should be updated
     * @return true if the updater supports a certain {@link AbstractDataEnvelope} implementation
     */
    public abstract boolean supportsDataEnvelope(AbstractDataEnvelope dataEnvelope);

    /**
     * Updates a certain {@link AbstractDataEnvelope} resource if already existent
     *
     * @param existingDataEnvelope the existing resource
     * @param newDataEnvelope new resource
     * @return the updated {@link AbstractDataEnvelope} resource
     */
    public abstract AbstractDataEnvelope updateDataEnvelope(T existingDataEnvelope, T newDataEnvelope);

    /**
     * Merges two instances of {@link AbstractDataEnvelopeAreaOfInterest}
     * together
     *
     * @param aoi01 first {@link AbstractDataEnvelopeAreaOfInterest}
     * @param aoi02 second {@link AbstractDataEnvelopeAreaOfInterest}
     * @return new merged instance of {@link AbstractDataEnvelopeAreaOfInterest}
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
     * Merges two {@link AbstractDataEnvelopeTimeFrame} instances together
     *
     * @param timeFrame01 first {@link AbstractDataEnvelopeTimeFrame}
     * @param timeFrame02 second {@link AbstractDataEnvelopeTimeFrame}
     * @return the new merged instance of {@link AbstractDataEnvelopeTimeFrame}
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
