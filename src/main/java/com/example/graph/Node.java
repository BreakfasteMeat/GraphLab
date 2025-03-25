package com.example.graph;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Node implements Serializable {
    double x,y;
    char ch;
    NodeUI nodeUI;
    List<Edge> fromEdges;
    List<Edge> toEdges;


    public Node() {
        x = (Math.random() * 500);
        y = (Math.random() * 300);
        ch = ((char) ((Math.random()*26) + 'A'));
        fromEdges = new ArrayList<>();
        toEdges = new ArrayList<>();
    }
    public void setCoords(double x, double y){
        this.x = x;
        this.y = y;
    }
    public void addFromEdge(Edge e){
        System.out.println("I am now having an edge" + e.edgeUI);
        fromEdges.add(e);
    }
    public void addToEdge(Edge e){
        toEdges.add(e);
    }

    public void setNodeUI(NodeUI nodeUI) {
        this.nodeUI = nodeUI;
    }
    public void setNodeUIEdges(){
        for(Edge e: fromEdges){
            System.out.println("In a loop, adding an edge: " + e.edgeUI);
            nodeUI.addEdgesStart(e.edgeUI);
        }
        for(Edge e: toEdges){
            System.out.println(e);
            nodeUI.addEdgesEnd(e.edgeUI);
        }
    }
    public void setEdgesEndpoints(double x, double y){
        for(Edge e: fromEdges){
            e.setStartCoord(x,y);
        }
        for(Edge e: toEdges){
            e.setEndCoord(x,y);
        }
    }

    public void setCh(char ch) {
        this.ch = ch;
    }
}
