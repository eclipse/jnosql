/*
 * Copyright (c) 2022 Contributors to the Eclipse Foundation
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the Eclipse
 * Public License v. 2.0 are satisfied: GNU General Public License v2.0
 * w/Classpath exception which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */
package jakarta.nosql.tck.entities;

import jakarta.nosql.mapping.Column;
import jakarta.nosql.mapping.Entity;
import jakarta.nosql.mapping.Id;

@Entity("download")
public class Download {

    @Id
    private Long id;

    @Column
    private byte[] contents;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getContents() {
        if(contents != null) {
            byte[] copiedArray = new byte[contents.length];
            System.arraycopy(contents, 0, copiedArray, 0, contents.length);
            return copiedArray;
        }
        return new byte[0];
    }

    public void setContents(byte[] contents) {
        if(contents != null) {
            this.contents = new byte[contents.length];
            System.arraycopy(contents, 0, this.contents, 0, contents.length);

        }
    }
}
