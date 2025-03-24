package com.example.graph;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.DragEvent;
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
    List<Node> nodes = new ArrayList<>();
    List<NodeUI> nodeUIList = new ArrayList<>();

    public AnchorPane apPane;

    public void onVertexDrag(DragEvent e){
        double x = e.getSceneX();
        double y = e.getSceneY();
        NodeUI nodeUI = (NodeUI)e.getSource();
        nodeUI.n.setCoords((int)x,(int)y);
        updateNodesUI();
    }

    public void onVertexClicked(MouseEvent e){

        char ch = ((char) ((Math.random()*26) + 'A'));
        Alert a = new Alert(Alert.AlertType.CONFIRMATION,"Would you like " + ch + "?", ButtonType.YES, ButtonType.NO);
        a.showAndWait().ifPresent(buttonType -> {
            if (buttonType == ButtonType.YES){
                NodeUI nodeUI = (NodeUI) e.getSource();
                nodeUI.n.ch = ch;
            }
        });
        updateNodesUI();
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
        for(int i = 0;i < nodes.size();i++){
            NodeUI nodeui = new NodeUI(nodes.get(i));
            nodeUIList.add(nodeui);
            AnchorPane.setRightAnchor(nodeUIList.get(i), (double) nodes.get(i).x * 10);
            AnchorPane.setTopAnchor(nodeUIList.get(i), (double) nodes.get(i).y * 10);
            apPane.getChildren().add(nodeUIList.get(i));
            nodeui.setOnMouseClicked(this::onVertexClicked);
            nodeui.setOnDragDone(this::onVertexDrag);
        }

    }
}