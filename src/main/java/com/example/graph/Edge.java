package com.example.graph;

import java.io.Serializable;

public class Edge implements Serializable {
    double start_x, start_y;
    double end_x, end_y;
    Node from;
    Node to;
    EdgeUI edgeUI;

    public Edge(double start_x, double start_y, double end_x, double end_y, Node from, Node to) {
        this.start_x = start_x;
        this.start_y = start_y;
        this.end_x = end_x;
        this.end_y = end_y;
        this.from = from;
        this.to = to;
    }

    public void setEdgeUI(EdgeUI edgeUI) {
        this.edgeUI = edgeUI;
    }

    public void setStartCoord(double start_x, double start_y) {
        this.start_x = start_x;
        this.start_y = start_y;
    }
    public void setEndCoord(double end_x, double end_y) {
        this.end_x = end_x;
        this.end_y = end_y;
    }
}
