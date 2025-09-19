package com.example.graph;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Node implements Serializable, Comparable<Node> {
    double x,y;
    String name;
    transient NodeUI nodeUI;
    List<Edge> fromEdges;
    List<Edge> toEdges;


    public boolean hasEdge(Edge e){
        for(Edge e1 : fromEdges){
            if(e1.isEqualTo(e)){
                return true;
            }
        }
        for(Edge e2 : toEdges){
            if(e2.isEqualTo(e)){
                return true;
            }
        }
        return false;
    }
    public Node() {
        x = (Math.random() * 500);
        y = (Math.random() * 300);
        name = ((char) ((Math.random()*26) + 'A')) + "" + ((char) ((Math.random()*26) + 'A'));
        fromEdges = new LinkedList<>();
        toEdges = new LinkedList<>();
    }


    public void setCoords(double x, double y){
        this.x = x;
        this.y = y;
    }
    public void addFromEdge(Edge e){
        fromEdges.add(e);
    }
    public void addToEdge(Edge e){
        toEdges.add(e);
    }

    public void setEdgesSerialized(){
        for(Edge e: fromEdges){
            nodeUI.addEdgesStart(e.edgeUI);
        }
        for(Edge e: toEdges){
            nodeUI.addEdgesEnd(e.edgeUI);
        }
    }

    public void setNodeUI(NodeUI nodeUI) {
        this.nodeUI = nodeUI;
    }
    public void setNodeUIEdges(){
        for(Edge e: fromEdges){
            nodeUI.addEdgesStart(e.edgeUI);
        }
        for(Edge e: toEdges){
            nodeUI.addEdgesEnd(e.edgeUI);
        }
    }
    public void printUIEdges() {
        for (Edge e : fromEdges) {

            System.out.println(e.edgeUI);
        }
        for (Edge e : toEdges) {
            System.out.println(e.edgeUI);

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

    public NodeUI getNodeUI() {
        return nodeUI;
    }

    public void setLists(){
        if(fromEdges == null){
            fromEdges = new ArrayList<>();
        }
        if(toEdges == null){
            toEdges = new ArrayList<>();
        }
    }

    @Override
    public int compareTo(Node o) {
        return this.name.compareTo(o.name);
    }
}
