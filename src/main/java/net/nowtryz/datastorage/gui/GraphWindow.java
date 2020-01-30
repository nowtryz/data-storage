package net.nowtryz.datastorage.gui;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.swing.mxGraphComponent;
import net.nowtryz.datastorage.graphs.SimpleWeightedGraph;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultWeightedEdge;

import javax.swing.*;
import java.awt.*;

public class GraphWindow<V> extends JApplet {
    private static final Dimension DEFAULT_SIZE = new Dimension(1000, 600);

    public GraphWindow(){
        this.setPreferredSize(DEFAULT_SIZE);
        this.resize(DEFAULT_SIZE);
    }

    public GraphWindow(SimpleWeightedGraph<V> graph) {
        this();
        this.setGraph(graph);
    }

    public void display() {
        JFrame frame = new JFrame();
        frame.getContentPane().add(this);
        frame.setTitle("JGraphT Adapter to JGraphX Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public void setGraph(SimpleWeightedGraph<V> graph) {
        JGraphXAdapter<V, DefaultWeightedEdge> jgxAdapter = new JGraphXAdapter<>(graph);
        mxGraphComponent component = new mxGraphComponent(jgxAdapter);
        component.setConnectable(false);
        component.getGraph().setAllowDanglingEdges(false);
        this.getContentPane().add(component);

        // positioning via jgraphx layouts
        mxCircleLayout layout = new mxCircleLayout(jgxAdapter);

        // center the circle
        int radius = 100;
        layout.setX0((DEFAULT_SIZE.width / 2.0) - radius);
        layout.setY0((DEFAULT_SIZE.height / 2.0) - radius);
        layout.setRadius(radius);
        layout.setMoveCircle(true);

        layout.execute(jgxAdapter.getDefaultParent());
    }
}
