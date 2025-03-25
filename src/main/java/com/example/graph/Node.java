package com.example.graph;


import java.io.Serializable;
import java.util.List;

public class Node implements Serializable {
    double x,y;
    char ch;
    List<Node> neighbors;

    public Node() {
        x = (Math.random() * 500);
        y = (Math.random() * 300);
        ch = ((char) ((Math.random()*26) + 'A'));
    }
    public void setCoords(double x, double y){
        this.x = x;
        this.y = y;
    }
    public void addNeighbor(Node n){
        neighbors.add(n);
    }



    public void setCh(char ch) {
        this.ch = ch;
    }
}
