package com.example.graph;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class NodeUI extends StackPane{
    Node n;
    Circle c;

    List<EdgeUI> edgesStart;
    List<EdgeUI> edgesEnd;

    public NodeUI(Node n) {
        c = new Circle();
        c.setFill(Paint.valueOf("#68e2e8"));
        c.setStroke(Paint.valueOf("BLACK"));
        c.setRadius(20);
        edgesStart = new ArrayList<>();
        edgesEnd = new ArrayList<>();

        getChildren().add(c);
        getChildren().add(new Text((n.ch + "")));
        this.n = n;


    }
    public void setColor(String color) {
        c.setFill(Paint.valueOf(color));
    }
    public void addEdgesStart(EdgeUI line) {
        if(!edgesStart.contains(line)) edgesStart.add(line);
    }
    public void addEdgesEnd(EdgeUI line) {
        if(!edgesEnd.contains(line)) edgesEnd.add(line);
    }
    public void setLineEndpoints(double x, double y){
        for(Line l : edgesStart){
            l.setStartX(x);
            l.setStartY(y);
        }
        for(Line l : edgesEnd){
            l.setEndX(x);
            l.setEndY(y);
        }
    }

}
