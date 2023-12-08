/*
 *   Copyright (c) 2023 Contributors to the Eclipse Foundation
 *    All rights reserved. This program and the accompanying materials
 *    are made available under the terms of the Eclipse Public License v1.0
 *    and Apache License v2.0 which accompanies this distribution.
 *    The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *    and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *
 *    You may elect to redistribute this code under either of these licenses.
 *
 *    Contributors:
 *
 *    Otavio Santana
 */
package org.eclipse.jnosql.mapping.document.entities;

import java.util.List;

public class WorkflowStepBuilder {
    private String id;
    private String key;
    private String workflowSchemaKey;
    private String stepName;
    private MainStepType mainStepType;
    private Integer stepNo;
    private String componentConfigurationKey;
    private String relationTypeKey;
    private List<Transition> availableTransitions;

    public WorkflowStepBuilder id(String id) {
        this.id = id;
        return this;
    }

    public WorkflowStepBuilder key(String key) {
        this.key = key;
        return this;
    }

    public WorkflowStepBuilder workflowSchemaKey(String workflowSchemaKey) {
        this.workflowSchemaKey = workflowSchemaKey;
        return this;
    }

    public WorkflowStepBuilder stepName(String stepName) {
        this.stepName = stepName;
        return this;
    }

    public WorkflowStepBuilder mainStepType(MainStepType mainStepType) {
        this.mainStepType = mainStepType;
        return this;
    }

    public WorkflowStepBuilder stepNo(Integer stepNo) {
        this.stepNo = stepNo;
        return this;
    }

    public WorkflowStepBuilder componentConfigurationKey(String componentConfigurationKey) {
        this.componentConfigurationKey = componentConfigurationKey;
        return this;
    }

    public WorkflowStepBuilder relationTypeKey(String relationTypeKey) {
        this.relationTypeKey = relationTypeKey;
        return this;
    }

    public WorkflowStepBuilder availableTransitions(List<Transition> availableTransitions) {
        this.availableTransitions = availableTransitions;
        return this;
    }

    public WorkflowStep build() {
        return new WorkflowStep(id, key, workflowSchemaKey, stepName, mainStepType,
                stepNo, componentConfigurationKey, relationTypeKey, availableTransitions);
    }
}