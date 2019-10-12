package com.blocki.pathfinder.algorithms;

import com.blocki.pathfinder.models.nodes.AlgorithmNode;
import com.blocki.pathfinder.models.nodes.Node;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.util.*;

public class AStarAlgorithm extends DistanceCalculatingAlgorithm {

    private List<List<AlgorithmNode>> nodesList = new LinkedList<>();

    private List<AlgorithmNode> openList = new LinkedList<>();

    private List<AlgorithmNode> closedList = new LinkedList<>();

    private Queue<AlgorithmNode> queue = new PriorityQueue<>(Comparator.comparingDouble(AlgorithmNode::getTotalDistance));

    private Integer totalOperations;

    @Override
    final void prepare() {

        queue.clear();
        nodesList.clear();
        openList.clear();
        closedList.clear();

        totalOperations = 0;
        totalLength = 0;

        int i = 0;
        for (List<Node> nodes : super.getBoard().getBoardNodes()) {

            nodesList.add(new LinkedList<>());

            for (Node node : nodes) {

                AlgorithmNode insideNode = new AlgorithmNode(node.getWidth(), node.getHeight(), node.get_node_type());
                insideNode.setDistanceToStart(Double.MAX_VALUE);
                insideNode.setVisited(false);
                insideNode.setChildren(new LinkedList<>());
                insideNode.setParent(null);
                insideNode.setDistanceToEnd(Double.MAX_VALUE);


                if (insideNode.get_node_type() == Node.NODE_TYPE.START) {

                    insideNode.setDistanceToStart(0.0);
                    insideNode.setDistanceToEnd(super.calculateDistanceToEndNode(insideNode));
                    queue.add(insideNode);
                }

                nodesList.get(i).add(insideNode);
            }
            i++;
        }
    }

    @Override
    public void run(GridPane gridPane, AnchorPane option, Button stopOrPauseButton) throws Exception {

        prepare();
        Platform.runLater(() -> ((Label) (option.lookup("#pathLength"))).setText(String.valueOf(totalLength)));
        Platform.runLater(() -> ((Label) (option.lookup("#operationsAmount"))).setText(String.valueOf(totalOperations)));
        option.getChildren()
                .stream()
                .filter(child -> !(child instanceof Label))
                .forEach(button -> button.setDisable(true));

        while (!queue.isEmpty()) {

            AlgorithmNode currentNode = queue.poll();
            currentNode.setVisited(true);

            totalOperations++;
            if (!super.checkForInterruptions()) {

                break;
            }

            super.addToClosedListUpdateGrid(gridPane, option, totalOperations, currentNode, openList, closedList);

            List<AlgorithmNode> children = super.getChildren(currentNode, nodesList);

            for (AlgorithmNode child : children) {

                double tempDistance = super.calculateDistanceToStartNode(currentNode) +
                        super.calculateDistanceBetween2neighbourNodes(currentNode, child) +
                        super.calculateDistanceToEndNode(child);

                if (!queue.contains(child)) {

                    child.setParent(currentNode);
                    child.setDistanceToStart( super.calculateDistanceToStartNode(currentNode) +
                            super.calculateDistanceBetween2neighbourNodes(currentNode, child));
                    child.setDistanceToEnd( super.calculateDistanceToEndNode(child));
                    queue.add(child);
                    totalOperations++;

                    super.addToOpenListUpdateGrid(gridPane, option, totalOperations, child, openList, closedList);
                }

                else if (tempDistance < child.getTotalDistance()) {

                    child.setDistanceToStart( super.calculateDistanceToStartNode(currentNode) +
                            super.calculateDistanceBetween2neighbourNodes(currentNode, child));

                    child.setDistanceToEnd(super.calculateDistanceToEndNode(child));
                    totalOperations++;
                    child.setParent(currentNode);

                    super.addToOpenListUpdateGrid(gridPane, option, totalOperations, child, openList, closedList);
                }

                if (super.checkIfFound(gridPane, option, stopOrPauseButton, currentNode, child,totalOperations, openList, closedList)) {

                    prepareToReturn(gridPane, option, stopOrPauseButton, openList, closedList);
                    return;
                }

                if (!super.getMenu().isInstantSearch()) {

                    super.waitingTimer();
                }
            }
        }

        super.prepareToReturn(gridPane, option, stopOrPauseButton, openList, closedList);
    }
}