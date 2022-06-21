/*
 *  Copyright (c) 2020 Otávio Santana and others
 *   All rights reserved. This program and the accompanying materials
 *   are made available under the terms of the Eclipse Public License v1.0
 *   and Apache License v2.0 which accompanies this distribution.
 *   The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *   and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *
 *   You may elect to redistribute this code under either of these licenses.
 *
 *   Contributors:
 *
 *   Otavio Santana
 */
open module org.eclipse.jnosql.mapping.core {
    requires jakarta.nosql.mapping.core;
    requires microprofile.config.api;
    requires java.logging;
    requires jakarta.nosql.communication.core;
    requires jakarta.enterprise.cdi.api;
    requires javax.inject;

    exports org.eclipse.jnosql.mapping.spi;
    exports org.eclipse.jnosql.mapping;
    exports org.eclipse.jnosql.mapping.reflection;

    uses org.eclipse.jnosql.mapping.repository.RepositoryReturn;
    uses org.eclipse.jnosql.mapping.reflection.CollectionSupplier;
    uses org.eclipse.jnosql.mapping.reflection.ClassOperation;

    provides jakarta.nosql.mapping.Pagination.PaginationBuilderProvider with org.eclipse.jnosql.mapping.DefaultPaginationBuilderProvider;
    provides jakarta.nosql.mapping.Sorts.SortsProvider with org.eclipse.jnosql.mapping.DefaultSortsProvider;
    provides javax.enterprise.inject.spi.Extension with org.eclipse.jnosql.mapping.reflection.ClassMappingExtension;
    provides org.eclipse.jnosql.mapping.reflection.CollectionSupplier with
            org.eclipse.jnosql.mapping.reflection.collection.DequeSupplier,
            org.eclipse.jnosql.mapping.reflection.collection.ListSupplier,
            org.eclipse.jnosql.mapping.reflection.collection.SetSupplier,
            org.eclipse.jnosql.mapping.reflection.collection.TreeSetSupplier;
    provides org.eclipse.jnosql.mapping.repository.RepositoryReturn with
            org.eclipse.jnosql.mapping.repository.returns.InstanceRepositoryReturn,
            org.eclipse.jnosql.mapping.repository.returns.ListRepositoryReturn,
            org.eclipse.jnosql.mapping.repository.returns.OptionalRepositoryReturn,
            org.eclipse.jnosql.mapping.repository.returns.PageRepositoryReturn,
            org.eclipse.jnosql.mapping.repository.returns.QueueRepositoryReturn,
            org.eclipse.jnosql.mapping.repository.returns.SetRepositoryReturn,
            org.eclipse.jnosql.mapping.repository.returns.SortedSetRepositoryReturn,
            org.eclipse.jnosql.mapping.repository.returns.StreamRepositoryReturn;
    provides org.eclipse.microprofile.config.spi.Converter with org.eclipse.jnosql.mapping.configuration.SettingsConverter;
}