package com.example.graph;


import java.io.Serializable;

public class Node implements Serializable {
    int x,y;
    char ch;

    public Node() {
        x = (int)(Math.random() * 50);
        y = (int)(Math.random() * 30);
        ch = ((char) ((Math.random()*26) + 'A'));
    }
    public void setCoords(int x, int y){
        this.x = x;
        this.y = y;
    }



    public void setCh(char ch) {
        this.ch = ch;
    }
}
