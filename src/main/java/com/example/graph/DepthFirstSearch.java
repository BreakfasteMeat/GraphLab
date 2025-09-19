package com.example.graph;

import java.util.*;

public class DepthFirstSearch extends Search {

    @Override
    public List<Tuple<Edge, Node>> search(Node origin) {
        List<Tuple<Edge,Node>> list = new ArrayList<>();
        list.add(new Tuple<>(null, origin));
        Stack<Node> stack = new Stack<>();
        Set<Node> visited = new HashSet<>();
        Set<Edge> visited_edges = new HashSet<>();
        stack.push(origin);
        visited.add(origin);
        while(!stack.isEmpty()){
            Node current = stack.pop();
            List<Edge> edges = new ArrayList<>();
            edges.addAll(current.fromEdges);
            edges.addAll(current.toEdges);
            for(Edge edge: edges){
                if(visited_edges.contains(edge)){
                    continue;
                }
                if(!visited.contains(edge.to)){
                    stack.push(edge.to);
                    visited.add(edge.to);
                    visited_edges.add(edge);
                    list.add(new Tuple<>(edge, edge.to));
                }
                if(!visited.contains(edge.from)){
                    stack.push(edge.from);
                    visited.add(edge.from);
                    visited_edges.add(edge);
                    list.add(new Tuple<>(edge, edge.from));
                }
            }
        }
        return list;
    }
}
