package net.nowtryz.datastorage.gui;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.swing.mxGraphComponent;
import net.nowtryz.datastorage.graph.SimpleWeightedGraph;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultWeightedEdge;

import javax.swing.*;
import java.awt.*;

/**
 * Object responsible from displaying the graph in a window
 * @param <V>
 */
public class GraphWindow<V> extends JApplet {
    private static final Dimension DEFAULT_SIZE = new Dimension(1000, 600);

    /**
     * Initialize a graph window
     */
    public GraphWindow(){
        this.setPreferredSize(DEFAULT_SIZE);
        this.resize(DEFAULT_SIZE);
    }

    /**
     * Initialize a graph window perilled with a graph
     * @param graph the graph to be display
     */
    public GraphWindow(SimpleWeightedGraph<V> graph) {
        this();
        this.setGraph(graph);
    }

    /**
     * Display the graph on the screen
     */
    public void display(String name) {
        JFrame frame = new JFrame();
        frame.getContentPane().add(this);
        if (name == null) frame.setTitle("JGraphT Data storage project");
        else frame.setTitle("JGraphT Data storage project - " + name);

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Set the graph to be display
     * @param graph the graph to display
     */
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
