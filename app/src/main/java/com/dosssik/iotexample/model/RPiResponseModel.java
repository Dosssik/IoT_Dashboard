package com.dosssik.iotexample.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.cloudant.sync.datastore.DocumentRevision;

import java.io.Serializable;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by dosssik on 12/3/16.
 */

@Getter
@Setter
public class RPiResponseModel implements Serializable {

    public enum EventType {
        Home,
        HW
    }
    public static final String COLUMN_EVENT_TYPE = "eventType";
    public static final String COLUMN_DEVICE_ID = "deviceId";
    public static final String COLUMN_DEVICE_TYPE = "deviceType";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String COLUMN_DATA = "data";
    public static final String COLUMN_HUMIDITY = "Humidity";
    public static final String COLUMN_PRESSURE = "Pressure";
    public static final String COLUMN_TEMPERATURE = "Temperature";
    public static final String COLUMN_CPU_USAGE = "CPU USED %";
    public static final String COLUMN_CPU_TEMP = "CPU TEMP";
    public static final String COLUMN_HOME = "HOME";

    private EventType eventType;

    private String deviceId;

    private String deviceType;

    private String timestamp;

    private double humidity;

    private double pressure;

    private double temperature;

    private double cpuUsage;

    private double cpuTemperature;

    public static RPiResponseModel fromRevision(DocumentRevision rev) {
        RPiResponseModel result = new RPiResponseModel();

        Map<String, Object> map = rev.asMap();

        if (map.get(COLUMN_EVENT_TYPE) != null) {
            String rawEventType = (String) map.get(COLUMN_EVENT_TYPE);
            result.setEventType(rawEventType.equals(COLUMN_HOME)
            ? EventType.Home
            : EventType.HW);
        }
        if (map.get(COLUMN_DEVICE_ID) != null) {
            result.setDeviceId((String) map.get(COLUMN_DEVICE_ID));
        }
        if (map.get(COLUMN_DEVICE_TYPE) != null) {
            result.setDeviceType((String) map.get(COLUMN_DEVICE_TYPE));
        }
        if (map.get(COLUMN_TIMESTAMP) != null) {
            result.setTimestamp((String) map.get(COLUMN_TIMESTAMP));
        }
        if (map.get(COLUMN_DATA) != null) {
            Map<String, Object> body = (Map<String, Object>) map.get(COLUMN_DATA);
            if (body.get(COLUMN_HUMIDITY) != null) {
                result.setHumidity((Double) body.get(COLUMN_HUMIDITY));
            }
            if (body.get(COLUMN_PRESSURE) != null) {
                result.setPressure((Double) body.get(COLUMN_PRESSURE));
            }
            if (body.get(COLUMN_TEMPERATURE) != null) {
                result.setTemperature((Double) body.get(COLUMN_TEMPERATURE));
            }
            if (body.get(COLUMN_CPU_USAGE) != null) {
                Object value = body.get(COLUMN_CPU_USAGE);
                if (value instanceof String) {
                    result.setCpuUsage(Double.valueOf((String) value));
                } else {
                    result.setCpuUsage((Double) body.get(COLUMN_CPU_USAGE));
                }
            }
            if (body.get(COLUMN_CPU_TEMP) != null) {
                Object value = body.get(COLUMN_CPU_TEMP);
                if (value instanceof String) {
                    result.setCpuTemperature(Double.valueOf((String) value));
                } else {
                    result.setCpuTemperature((Double) body.get(COLUMN_CPU_TEMP));
                }
            }
        }
        return result;
    }
}
