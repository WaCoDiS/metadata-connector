/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
