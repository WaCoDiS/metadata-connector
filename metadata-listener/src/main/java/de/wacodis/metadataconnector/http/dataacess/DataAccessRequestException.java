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
package de.wacodis.metadataconnector.http.dataacess;

import java.io.IOException;

/**
 *
 * @author <a href="mailto:s.drost@52north.org">Sebastian Drost</a>
 */
public class DataAccessRequestException extends IOException {

    public DataAccessRequestException() {
    }

    public DataAccessRequestException(String message) {
        super(message);
    }

    public DataAccessRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataAccessRequestException(Throwable cause) {
        super(cause);
    }

}
