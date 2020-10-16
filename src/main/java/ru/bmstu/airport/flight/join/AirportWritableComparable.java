package ru.bmstu.airport.flight.join;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class AirportWritableComparable implements WritableComparable {
    int airportID;
    int indicator;

    void setAirportID(int newAirportId) {
        this.airportID = newAirportId;
    }

    void setIndicator(int newIndicator) {
        this.indicator = newIndicator;
    }

    int getAirportID() {
        return this.airportID;
    }

    int getIndicator() {
        return this.indicator;
    }

    @Override
    public int compareTo(Object o) {
        AirportWritableComparable compareAirport = (AirportWritableComparable) o;

        if (this.airportID > compareAirport.airportID) {
            return 1;
        }

        if (this.airportID < compareAirport.airportID) {
            return -1;
        }

        if (this.indicator > compareAirport.indicator) {
            return 1;
        }

        if (this.indicator < compareAirport.indicator) {
            return -1;
        }

        return 0;
    }

    @Override
    public int hashCode() {
        return this.airportID;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(airportID);
        dataOutput.writeInt(indicator);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.airportID =  dataInput.readInt();
        this.indicator = dataInput.readInt();
    }
}
