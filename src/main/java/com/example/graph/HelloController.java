package com.example.graph;

import javafx.animation.KeyFrame;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.*;
import java.util.*;
import java.util.function.Consumer;

public class HelloController {
    public TextField tfNodeName;
    public Button btnSetName;
    public Button btnAddEdge;
    public Button btnRemoveNode;
    public Button btnRemoveEdge;
    public Button btnAddEdgeAll;
    public ComboBox<String> originNodeComboBox;
    private Scene scene;
    List<Node> nodes = new LinkedList<>();
    List<NodeUI> nodeUIList = new LinkedList<>();
    List<EdgeUI> edgeUIList = new LinkedList<>();

    private Node originNode;


    double mouse_x, mouse_y;

    private boolean isDragging = false;
    private boolean isAddingEdge = false;
    private Line currentLine;
    Node selectedNode = null;
    Edge selectedEdge = null;

    Search search;


    public AnchorPane apPane;

    public void onEdgeClicked(MouseEvent e){
        if(isDragging){
            isDragging = false;
            return;
        }
        Edge edge = ((EdgeUI) e.getSource()).edge;
        System.out.println("Clicked: " + edge);
        if(!isAddingEdge){
            selectEdge(edge);
        }
        updateNodesUI();
    }

    private void selectEdge(Edge edge){
        if(edge == selectedEdge){
            selectedEdge = null;
            btnRemoveEdge.setDisable(true);
        } else {
            selectedNode = null;
            selectedEdge = edge;
            btnRemoveEdge.setDisable(false);
        }
        updateUIElements();
        updateNodesUI();
    }

    public void onVertexClicked(MouseEvent e){
        if(isDragging){
            isDragging = false;
            return;
        }
        Node n = ((NodeUI)e.getSource()).n;

        if(!isAddingEdge){
            selectNode(n);
            tfNodeName.setText(n.name + "");
        } else {

            currentLine.setEndX(n.x + AppSettings.nodeRadius);
            currentLine.setEndY(n.y + AppSettings.nodeRadius);
            Edge edge = new Edge(n,selectedNode);

            //selectedNode.addFromEdge(edge);
            //n.addToEdge(edge);
            addEdge(selectedNode,n);

            isAddingEdge = false;
            currentLine = null;
            selectedNode = null;
        }

        updateUIElements();
        updateNodesUI();
    }
    private void addEdge(Node nodeFrom, Node nodeTo){

        Edge edge = new Edge(nodeTo,nodeFrom);
        if(nodeFrom.hasEdge(edge)){
            System.out.println("Already has edge");
            return;
        }
        nodeFrom.addFromEdge(edge);
        nodeTo.addToEdge(edge);

    }
    private void selectNode(Node n){
        if(n == selectedNode){
            selectedNode = null;
        } else {
            selectedEdge = null;
            selectedNode = n;
        }
    }

    private void disableUIElements(){
        tfNodeName.setDisable(true);
        btnSetName.setDisable(true);
        btnAddEdge.setDisable(true);
        btnRemoveNode.setDisable(true);
        btnAddEdgeAll.setDisable(true);
        tfNodeName.clear();
    }

    public void onOriginNodeSelected(ActionEvent e){
        originNode = nodes.get(((ComboBox)e.getSource()).getSelectionModel().getSelectedIndex());
    }
    private void enableUIElements(){
        tfNodeName.setDisable(false);
        btnSetName.setDisable(false);
        btnAddEdge.setDisable(false);
        btnRemoveNode.setDisable(false);
        btnAddEdgeAll.setDisable(false);
    }
    private void updateUIElements(){
        if(selectedNode != null){
            enableUIElements();
        } else {
            disableUIElements();
        }
        if(selectedEdge == null){
            btnRemoveEdge.setDisable(true);
        }
        if(!nodes.isEmpty() && originNodeComboBox.isDisabled()){
            originNodeComboBox.getItems().clear();
            originNodeComboBox.setDisable(false);
            originNodeComboBox.getItems().setAll(
                    nodes
                            .stream()
                            .map(n -> n.name)
                            .toList()

            );

        } else if(nodes.isEmpty()) {
            originNodeComboBox.setDisable(true);
        }
    }

    public void onBFSClicked(){
        search = new BreadthFirstSearch();
        performSearch();
    }
    public void onDFSClicked(){
        search = new DepthFirstSearch();
        performSearch();
    }

    private void performSearch(){
        if(originNode == null){
            throw new RuntimeException("Origin node is null");
        }
        List<Tuple<Edge,Node>> list = search.search(originNode);
        SequentialTransition st = new SequentialTransition();
        for(Tuple<Edge,Node> t: list){
            Timeline visit_edge = new Timeline(
                    new KeyFrame(
                            Duration.seconds(0.3),
                            e -> {
                                if(t.first() != null){
                                    t.first().edgeUI.setVisitedColor();
                                }
                            }
                    )
            );
            Timeline visit = new Timeline(

                    new KeyFrame(
                            Duration.seconds(0.3),
                            e -> {
                                t.second().nodeUI.setVisitedColor();
                            }
                    )
            );
            st.getChildren().add(visit_edge);
            st.getChildren().add(visit);
        }
        Timeline x = new Timeline(
                new KeyFrame(Duration.seconds(0.3), e -> updateNodesUI())
        );
        st.getChildren().add(x);
        st.play();
    }

    @FXML
    void initialize(){


        try{
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("nodes.txt"));
            nodes = (ArrayList<Node>) (ois.readObject());
            ois.close();
        } catch (IOException | ClassNotFoundException e){
            System.out.println(e.getClass());

        }
        for (Node node : nodes) {
            System.out.println("Node: " + node.name + " UI: " + node.getNodeUI());
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


        btnRemoveNode.setOnAction(this::onRemoveNodeClicked);
        btnSetName.setOnAction(this::onSetNameClicked);
        btnAddEdge.setOnAction(this::onAddEdgeClicked);
        btnAddEdge.setOnMouseClicked(this::onAddEdgeMouseClick);
        btnRemoveEdge.setDisable(true);

        originNodeComboBox.setDisable(true);

        disableUIElements();
        updateNodesUI();
    }
    private void onRemoveNodeClicked(ActionEvent e){
        nodes.remove(selectedNode);
        selectedNode = null;
        updateUIElements();
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
        selectedNode.name = newName;
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
        line.setStrokeWidth(AppSettings.lineWidth);
        line.setStartX(selectedNode.x + AppSettings.nodeRadius);
        line.setStartY(selectedNode.y + AppSettings.nodeRadius);

        apPane.getChildren().add(0,line);
        line.toBack();

        currentLine = line;
    }
    public void onAddEdgeAllClicked(ActionEvent e){
        Node n = selectedNode;
        for(Node node : nodes){
            if(node != n){
                addEdge(n,node);
            }
        }
        updateNodesUI();
    }

    public void onAddClicked(ActionEvent event) {
        Node newNode = new Node();
        nodes.add(newNode);
        updateNodesUI();
        updateUIElements();
    }
    public void onClearClicked(ActionEvent event){
        nodes.clear();
        apPane.getChildren().clear();
        selectedEdge = null;
        selectedNode = null;
        updateUIElements();
        updateNodesUI();
    }
    private void updateNodesUI(){
        nodeUIList.clear();
        edgeUIList.clear();


        apPane.getChildren().clear();


        for (Node node : nodes) {

            NodeUI nodeui = new NodeUI(node);
            Iterator i = node.fromEdges.iterator();

            while(i.hasNext()){
                Edge edge = (Edge) i.next();
                if(!nodes.contains(edge.to)){
                    i.remove();
                    continue;
                }

                EdgeUI edgeui = new EdgeUI(edge);
                edgeui.setOnMouseClicked(this::onEdgeClicked);
                setEdgeDesign(edge,edgeui);
                edgeUIList.add(edgeui);
                edgeui.setStartX(edge.start_x);
                edgeui.setStartY(edge.start_y);
                edgeui.setEndX(edge.end_x);
                edgeui.setEndY(edge.end_y);
                apPane.getChildren().add(edgeui);
                edgeui.toBack();
                edge.setEdgeUI(edgeui);
                //System.out.println("Rendered edge connected from node " + edge.from.ch + " to node " + edge.to.ch);
            }
            nodeUIList.add(nodeui);
            setVertexDesign(nodeui,node);
            nodeui.setLayoutX(node.x);
            nodeui.setLayoutY(node.y);
            apPane.getChildren().add(nodeui);
            setVertexListeners(nodeui);
            node.setLists();
            node.setNodeUI(nodeui);
            node.setNodeUIEdges();
        }


    }
    private void setVertexDesign(NodeUI nodeui, Node node){
        if(selectedNode == node){
            nodeui.setColor("#e8bf68");
        }
    }

    private void setEdgeDesign(Edge edge, EdgeUI edgeui){
        if(edge == selectedEdge){
            edgeui.setColor("#e8bf68");
        }
    }
    public void onDeleteEdgeClicked(ActionEvent e){
        Edge edge = selectedEdge;
        edge.to.toEdges.remove(edge);
        edge.from.fromEdges.remove(edge);
        btnRemoveEdge.setDisable(true);
        updateNodesUI();
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

        if(newX > apPane.getWidth() - (AppSettings.nodeRadius * 2)) newX = apPane.getWidth() - (AppSettings.nodeRadius * 2);
        if(newY > apPane.getHeight() - (AppSettings.nodeRadius * 2)) newY = apPane.getHeight() - (AppSettings.nodeRadius * 2);

        if(newX < 0) newX = 0;
        if(newY < 0) newY = 0;


        nodeUI.n.setCoords(newX, newY);


        nodeUI.setLayoutX(nodeUI.n.x);
        nodeUI.setLayoutY(nodeUI.n.y);



        for(Edge edge : nodeUI.n.fromEdges){
            edge.setStartCoord(nodeUI.n.x + AppSettings.nodeRadius,nodeUI.n.y + AppSettings.nodeRadius);
            edge.edgeUI.setStartX(edge.start_x);
            edge.edgeUI.setStartY(edge.start_y);
        }
        for(Edge edge : nodeUI.n.toEdges){
            edge.setEndCoord(nodeUI.n.x + AppSettings.nodeRadius,nodeUI.n.y + AppSettings.nodeRadius);
            edge.edgeUI.setEndX(edge.end_x);
            edge.edgeUI.setEndY(edge.end_y);
        }
        mouse_x = e.getSceneX();
        mouse_y = e.getSceneY();

    }

    private void onVertexMousePressed(MouseEvent mouseEvent) {
        mouse_x = mouseEvent.getSceneX();
        mouse_y = mouseEvent.getSceneY();
    }
}