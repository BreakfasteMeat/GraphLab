package com.example.graph;

import java.util.List;

public abstract class Search {
    List<Node> nodes;
    public abstract List<Tuple<Edge,Node>> search(Node origin);
}
