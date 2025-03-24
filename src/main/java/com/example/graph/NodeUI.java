package com.example.graph;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class NodeUI extends StackPane{
    Node n;

    public NodeUI(Node n) {
        Circle c = new Circle();
        c.setFill(Paint.valueOf("#68e2e8"));
        c.setStroke(Paint.valueOf("BLACK"));
        c.setRadius(20);

        getChildren().add(c);
        getChildren().add(new Text((n.ch + "")));
        this.n = n;
    }

}
