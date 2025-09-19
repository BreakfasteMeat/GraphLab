package com.example.graph;

import java.util.*;

public class BreadthFirstSearch extends Search{


    @Override
    public List<Tuple<Edge,Node>> search(Node origin) {
        List<Tuple<Edge,Node>> list = new ArrayList<>();
        Tuple<Edge,Node> start = new Tuple<>(null, origin);

        Queue<Tuple<Edge,Node>> queue = new LinkedList<>();

        Set<Node> visited = new HashSet<>();
        Set<Edge> visited_edges = new HashSet<>();
        queue.add(start);
        visited.add(origin);
        while(!queue.isEmpty()){
            Tuple<Edge,Node> current = queue.remove();
            list.add(current);

            List<Edge> edges = new ArrayList<>();


            edges.addAll(current.second().fromEdges);
            edges.addAll(current.second().toEdges);
            for(Edge edge: edges){
                if(visited_edges.contains(edge)){
                    continue;
                }
                if(!visited.contains(edge.to)){
                    visited.add(edge.to);
                    visited_edges.add(edge);
                    Tuple<Edge, Node> new_visit = new Tuple<>(edge, edge.to);
                    queue.add(new_visit);
                }
                if(!visited.contains(edge.from)){

                    visited.add(edge.from);
                    visited_edges.add(edge);
                    Tuple<Edge, Node> new_visit = new Tuple<>(edge, edge.to);
                    queue.add(new_visit);
                }
            }
        }
        return list;
    }
}
