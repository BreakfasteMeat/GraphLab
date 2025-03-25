package com.example.graph;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class HelloController {
    public TextField tfNodeName;
    public Button btnSetName;
    public Button btnAddEdge;
    private Scene scene;
    List<Node> nodes = new ArrayList<>();
    List<Edge> edges = new ArrayList<>();
    List<NodeUI> nodeUIList = new ArrayList<>();
    List<EdgeUI> edgeUIList = new ArrayList<>();

    double mouse_x, mouse_y;

    private boolean isDragging = false;
    private boolean isAddingEdge = false;
    private Line currentLine;
    Node selectedNode = null;


    public AnchorPane apPane;



    public void onVertexClicked(MouseEvent e){
        if(isDragging){
            isDragging = false;
            return;
        }
        Node n = ((NodeUI)e.getSource()).n;

        if(!isAddingEdge){
            selectNode(n);
            tfNodeName.setText(n.ch + "");
        } else {

            currentLine.setEndX(n.x + 20);
            currentLine.setEndY(n.y + 20);
            Edge edge = new Edge(currentLine.getStartX(),currentLine.getStartY(),currentLine.getEndX(),currentLine.getEndY(),selectedNode,n);
            edges.add(edge);
            selectedNode.addFromEdge(edge);
            n.addToEdge(edge);


            isAddingEdge = false;
            currentLine = null;
            selectedNode = null;
        }

        updateUIElements();
        updateNodesUI();
    }
    private void selectNode(Node n){
        if(n == selectedNode){
            selectedNode = null;
        } else {
            selectedNode = n;
        }
    }

    private void disableUIElements(){
        tfNodeName.setDisable(true);
        btnSetName.setDisable(true);
        btnAddEdge.setDisable(true);
        tfNodeName.clear();
    }
    private void enableUIElements(){
        tfNodeName.setDisable(false);
        btnSetName.setDisable(false);
        btnAddEdge.setDisable(false);

    }
    private void updateUIElements(){
        if(selectedNode != null){
            enableUIElements();
        } else {
            disableUIElements();
        }
    }

    @FXML
    void initialize(){
        try{
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("nodes.txt"));
            nodes = (ArrayList<Node>) (ois.readObject());
            ois.close();
        } catch (IOException | ClassNotFoundException e){
            System.out.println(e.getClass());
            //e.printStackTrace();
        }
        tfNodeName.textProperty().addListener((observable, oldValue, newValue) -> {
           if(newValue.length() > 1){
               tfNodeName.setText(oldValue);
           }
        });
        apPane.sceneProperty().addListener((observable, oldValue, newValue) -> {
           if(newValue != null){
               scene = newValue;
           }
            scene.setOnMouseMoved(this::onMouseMoved);
        });


        btnSetName.setOnAction(this::onSetNameClicked);
        btnAddEdge.setOnAction(this::onAddEdgeClicked);
        btnAddEdge.setOnMouseClicked(this::onAddEdgeMouseClick);
        disableUIElements();
        updateNodesUI();
    }
    private void onMouseMoved(MouseEvent e){
        if(currentLine == null) return;
        mouse_x = e.getSceneX();
        mouse_y = e.getSceneY();
        currentLine.setEndX(mouse_x - apPane.getLayoutX());
        currentLine.setEndY(mouse_y - apPane.getLayoutY());
    }
    private void onSetNameClicked(ActionEvent e){
        String newName = tfNodeName.getText();
        selectedNode.ch = newName.charAt(0);
        updateNodesUI();
    }
    private void onAddEdgeMouseClick(MouseEvent e){
        mouse_x = e.getScreenX();
        mouse_y = e.getScreenY();
    }
    public void onSaveClicked(ActionEvent e){
        try{
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("nodes.txt"));
            oos.writeObject(nodes);
            oos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        updateNodesUI();
    }
    public void onAddEdgeClicked(ActionEvent e){
        isAddingEdge = true;
        Line line = new Line();
        line.setStartX(selectedNode.x + 20);
        line.setStartY(selectedNode.y + 20);

        apPane.getChildren().add(0,line);
        line.toBack();

        currentLine = line;
    }

    public void onAddClicked(ActionEvent event) {
        nodes.add(new Node());
        updateNodesUI();
    }
    public void onClearClicked(ActionEvent event){
        nodes.clear();
        apPane.getChildren().clear();
        updateNodesUI();
    }
    private void updateNodesUI(){
        nodeUIList.clear();
        edgeUIList.clear();

        apPane.getChildren().clear();

        for (Edge edge : edges) {
            EdgeUI edgeUI = new EdgeUI(edge);
            edgeUIList.add(edgeUI);
            apPane.getChildren().add(edgeUI);
            edge.setEdgeUI(edgeUI);
        }
        for (Node node : nodes) {
            System.out.println(node.x + "," + node.y);
            NodeUI nodeui = new NodeUI(node);
            nodeUIList.add(nodeui);
            setVertexDesign(nodeui,node);
            nodeui.setLayoutX(node.x);
            nodeui.setLayoutY(node.y);
            apPane.getChildren().add(nodeui);
            setVertexListeners(nodeui);
            node.setNodeUI(nodeui);
            node.setNodeUIEdges();
        }


    }
    private void setVertexDesign(NodeUI nodeui, Node node){
        if(selectedNode == node){
            nodeui.setColor("#e8bf68");
        }
    }
    private void setVertexListeners(NodeUI nodeUI){
        nodeUI.setOnMouseClicked(this::onVertexClicked);
        nodeUI.setOnMousePressed(this::onVertexMousePressed);
        nodeUI.setOnMouseDragged(this::onVertexDragged);
    }
    public void onVertexDragged(MouseEvent e) {
        isDragging = true;
        NodeUI nodeUI = (NodeUI) e.getSource();

        double newX = nodeUI.getLayoutX() + (e.getSceneX() - mouse_x);
        double newY = nodeUI.getLayoutY() + (e.getSceneY() - mouse_y);

        if(newX > 560) newX = 560;
        if(newY > 360) newY = 360;

        if(newX < 0) newX = 0;
        if(newY < 0) newY = 0;


        nodeUI.n.setCoords(newX, newY);


        nodeUI.setLayoutX(nodeUI.n.x);
        nodeUI.setLayoutY(nodeUI.n.y);


        nodeUI.n.setEdgesEndpoints(newX + 20, newY + 20);
        nodeUI.setLineEndpoints(newX + 20, newY + 20);



        mouse_x = e.getSceneX();
        mouse_y = e.getSceneY();

        System.out.println("Node: (" + nodeUI.n.x +","+ nodeUI.n.y + ")" + " NodeUI: ( " + nodeUI.getLayoutX() +","+ nodeUI.getLayoutY() + ")");
    }

    private void onVertexMousePressed(MouseEvent mouseEvent) {
        mouse_x = mouseEvent.getSceneX();
        mouse_y = mouseEvent.getSceneY();
    }
}