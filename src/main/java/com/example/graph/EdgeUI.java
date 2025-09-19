package com.example.graph;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;

import java.io.Serializable;

public class EdgeUI extends Line{
    Edge edge;

    public EdgeUI(Edge edge) {
        this.edge = edge;
        setStartX(edge.start_x);
        setStartY(edge.start_y);
        setEndX(edge.end_x);
        setEndY(edge.end_y);
        setStrokeWidth(AppSettings.lineWidth);
//        setStroke(Paint.valueOf("#FF00FF"));
    }
    public void setColor(String color) {
        setFill(Paint.valueOf(color));
        setStroke(Paint.valueOf(color));
    }

    public void setVisitedColor() {
        this.setStroke(Paint.valueOf("#e86969"));
    }
    public void setNormalColor(){
    }
}
