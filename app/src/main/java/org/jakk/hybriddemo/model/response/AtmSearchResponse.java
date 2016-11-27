package org.jakk.hybriddemo.model.response;

import org.jakk.hybriddemo.model.Location;

import java.util.List;

public class AtmSearchResponse {

    private List<Location> items;

    public AtmSearchResponse(List<Location> items) {
        this.items = items;
    }

    public List<Location> getItems() {
        return items;
    }

    public void setItems(List<Location> items) {
        this.items = items;
    }
}
