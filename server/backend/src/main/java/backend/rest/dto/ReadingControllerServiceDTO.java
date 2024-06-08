package backend.rest.dto;

import backend.domain.Reading;

import java.sql.Timestamp;

public class ReadingControllerServiceDTO {
    private String value;
    private Timestamp timestamp;

    public ReadingControllerServiceDTO(Reading reading){
        this.value = reading.getReadingValue();
        this.timestamp = reading.getReadingTimestamp();
    }

    public String getValue() {
        return value;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
}
