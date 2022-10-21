package com.personia.supervisorassignment.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Vertex {
    private String value;
    private boolean beingVisited;
    private boolean visited;
    private List<Vertex> adjacencyList;

    public Vertex(String value) {
        this.value = value;
        this.adjacencyList = new ArrayList<>();
    }

    public void addNeighbor(Vertex adjacent) {
        this.adjacencyList.add(adjacent);
    }
}
