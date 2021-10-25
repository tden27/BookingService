package com.github.tden27.bookingService.model;

import java.util.Arrays;
import java.util.List;

public enum Resource {
    DOCTOR("Doctor"),
    MFC("MFC"),
    MAIL_POST("Mail post"),
    CONFERENCE_HALL("Conference hall");

    private final String resource;

    Resource(String resource) {
        this.resource = resource;
    }

    public String getResource() {
        return resource;
    }

    public static List<Resource> getResources() {
        return Arrays.asList(values());
    }
}
