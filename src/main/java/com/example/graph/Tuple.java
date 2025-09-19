package com.example.graph;

public record Tuple<A, B>(A first, B second) {
    @Override
    public String toString() {
        return first.toString() + " " + second.toString();
    }
}
