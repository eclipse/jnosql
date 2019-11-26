package org.eclipse.jnosql.artemis.graph;

import java.util.List;

public interface EntityTree {

    <T> T getData();

    List<EntityTree> getChildren();

    boolean isLeaf();
}
