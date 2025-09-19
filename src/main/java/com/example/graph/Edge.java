package com.example.graph;

import java.io.Serializable;

public class Edge implements Serializable {
    double start_x, start_y;
    double end_x, end_y;
    Node from;
    Node to;
    transient EdgeUI edgeUI;

    public Edge(double start_x, double start_y, double end_x, double end_y, Node from, Node to) {
        this.start_x = start_x;
        this.start_y = start_y;
        this.end_x = end_x;
        this.end_y = end_y;
        this.from = from;
        this.to = to;
    }

    public Edge(Node to, Node from) {
        this.to = to;
        this.from = from;
        start_x = from.x + AppSettings.nodeRadius;
        start_y = from.y + AppSettings.nodeRadius;
        end_x = to.x + AppSettings.nodeRadius;
        end_y = to.y + AppSettings.nodeRadius;
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

    public EdgeUI getEdgeUI() {
        return edgeUI;
    }

    @Override
    public String toString() {
        return "Edge{" +
                ", start=(" + start_x + ", " + start_y + ")" +
                ", end=(" + end_x + ", " + end_y + ")" +
                '}';
    }

    public boolean isEqualTo(Edge edge) {
        return (this.from == edge.from && this.to == edge.to) || (this.from == edge.to && this.to == edge.from);
    }
}
