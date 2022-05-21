package com.github.tden27.bookingService.model;

import java.util.Arrays;
import java.util.List;

public enum Resource {
    DOCTOR("Doctor"),
    MFC("MFC"),
    MAIL_POST("Mail post"),
    CONFERENCE_HALL("Conference hall");

    private final String resourceValue;

    Resource(String resourceValue) {
        this.resourceValue = resourceValue;
    }

    public String getResourceValue() {
        return resourceValue;
    }

    public static List<Resource> getResources() {
        return Arrays.asList(values());
    }
}
