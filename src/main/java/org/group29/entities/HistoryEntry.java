package org.group29.entities;

import java.sql.Date;

public class HistoryEntry implements Comparable<HistoryEntry>{
    private final Date eventDate;
    private final int vehicleId;
    private final int clientId;
    private final int duration;

    public HistoryEntry(Date eventDate, int vehicleId, int clientId, int duration) {
        this.eventDate = eventDate;
        this.vehicleId = vehicleId;
        this.clientId = clientId;
        this.duration = duration;
    }

    @Override
    public String toString() {
        if(duration == 0)
            return String.format("Operator returned vehicle #%d from client #%d on %s", vehicleId, clientId, eventDate.toString());
        else
            return String.format("Operator rented vehicle #%d to client #%d on %s for %d days", vehicleId, clientId, eventDate.toString(), duration);
    }

    @Override
    public int compareTo(HistoryEntry o) {
        return eventDate.compareTo(o.eventDate);
    }
}
