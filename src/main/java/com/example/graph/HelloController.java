package com.example.graph;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
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
    List<Node> nodes = new ArrayList<>();
    List<NodeUI> nodeUIList = new ArrayList<>();
    double mouse_x, mouse_y;

    private boolean isDragging = false;
    private boolean isAddingEdge = false;
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
        } else {
            selectedNode.addNeighbor(n);
            n.addNeighbor(selectedNode);
        }

        updateNodesUI();
    }
    private void selectNode(Node n){
        if(n == selectedNode){
            selectedNode = null;
        } else {
            selectedNode = n;
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

        updateNodesUI();
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
        apPane.getChildren().clear();
        for (Node node : nodes) {
            System.out.println(node.x + "," + node.y);
            NodeUI nodeui = new NodeUI(node);
            nodeUIList.add(nodeui);
            setVertexDesign(nodeui,node);
            nodeui.setLayoutX(node.x);
            nodeui.setLayoutY(node.y);
            apPane.getChildren().add(nodeui);
            setVertexListeners(nodeui);
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

        nodeUI.n.setCoords(newX, newY);


        nodeUI.setLayoutX(nodeUI.n.x);
        nodeUI.setLayoutY(nodeUI.n.y);

        mouse_x = e.getSceneX();
        mouse_y = e.getSceneY();

        System.out.println("Node: (" + nodeUI.n.x +","+ nodeUI.n.y + ")" + " NodeUI: ( " + nodeUI.getLayoutX() +","+ nodeUI.getLayoutY() + ")");
    }

    private void onVertexMousePressed(MouseEvent mouseEvent) {
        mouse_x = mouseEvent.getSceneX();
        mouse_y = mouseEvent.getSceneY();
    }
}