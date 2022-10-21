package com.personia.supervisorassignment.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.personia.supervisorassignment.model.Graph;
import com.personia.supervisorassignment.model.Vertex;

@ExtendWith(MockitoExtension.class)
class GraphTest {

    @Test
    void addVertex() {
        Graph graph = new Graph();
        graph.addVertex(new Vertex("v1"));
        assertThat(graph.getVertices()).hasSize(1);
        assertThat(graph.getVertices().get(0).getValue()).isEqualTo("v1");
    }

    @Test
    void addEdge() {
        Graph graph = new Graph();
        Vertex vertex = new Vertex("v1");
        graph.addVertex(vertex);
        graph.addVertex(new Vertex("v2"));
        graph.addEdge(vertex, new Vertex("v2"));
        assertThat(vertex.getAdjacencyList()).hasSize(1);
        assertThat(vertex.getAdjacencyList().get(0).getValue()).isEqualTo("v2");
    }

    @Test
    void hasCycle_returns_false() {
        Vertex vertexA = new Vertex("Pete");
        Vertex vertexC = new Vertex("Barbara");
        Vertex vertexB = new Vertex("Nick");
        Vertex vertexD = new Vertex("Sophie");
        Vertex vertexF = new Vertex("Jonas");

        Graph graph = new Graph();
        graph.addVertex(vertexA);
        graph.addVertex(vertexB);
        graph.addVertex(vertexC);
        graph.addVertex(vertexD);

        graph.addEdge(vertexA, vertexB);
        graph.addEdge(vertexC, vertexB);
        graph.addEdge(vertexB, vertexD);
        graph.addEdge(vertexD, vertexF);

        assertThat(graph.hasCycle()).isFalse();
    }

    @Test
    void hasCycle_returns_true() {
        Vertex vertexA = new Vertex("Pete");
        Vertex vertexC = new Vertex("Barbara");
        Vertex vertexB = new Vertex("Nick");
        Vertex vertexD = new Vertex("Sophie");

        Graph graph = new Graph();
        graph.addVertex(vertexA);
        graph.addVertex(vertexB);
        graph.addVertex(vertexC);
        graph.addVertex(vertexD);

        graph.addEdge(vertexA, vertexB);
        graph.addEdge(vertexC, vertexB);
        graph.addEdge(vertexB, vertexD);
        graph.addEdge(vertexD, vertexA);

        assertThat(graph.hasCycle()).isTrue();
    }
}