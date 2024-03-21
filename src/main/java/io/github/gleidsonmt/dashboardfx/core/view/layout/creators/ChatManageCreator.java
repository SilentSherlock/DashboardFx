package io.github.gleidsonmt.dashboardfx.core.view.layout.creators;

import io.github.gleidsonmt.dashboardfx.core.Context;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

/**
 * Date: 2024/3/21
 * Author: SilentSherlock
 * Description: telegram chat manager view, base class
 */
public class ChatManageCreator implements BuildCreator{

    private HBox body;
    // items add in the view tree
    protected ObservableList<Node> items;
    // application context
    protected final Context context;
    // root of nodes
    protected final StackPane root;

    public ChatManageCreator(Context context) {
        this.context = context;
        this.root = new StackPane();
    }

    /**
     * @return
     */
    @Override
    public BuildCreator build() {
        return null;
    }

    /**
     * @return
     */
    @Override
    public Node getRoot() {
        return null;
    }
}
