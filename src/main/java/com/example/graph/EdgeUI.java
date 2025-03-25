package com.example.graph;

import javafx.scene.shape.Line;

public class EdgeUI extends Line {
    Edge edge;

    public EdgeUI(Edge edge) {
        this.edge = edge;
        setStartX(edge.start_x);
        setStartY(edge.start_y);
        setEndX(edge.end_x);
        setEndY(edge.end_y);
    }
}
