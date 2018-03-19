package org.solteh.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Node {
    @Id
    @GeneratedValue
    @Column
    private int id;
    @Column
    private String description;
    @ManyToOne(cascade = CascadeType.ALL)
    private Node parent;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Node> nodes;

    public Node() {
        nodes = new ArrayList<>();
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void addChild(Node nd) {
        nodes.add(nd);
    }
}
