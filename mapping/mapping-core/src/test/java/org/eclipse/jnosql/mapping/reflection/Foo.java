/*
 *  Copyright (c) 2018 Otávio Santana and others
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
package org.eclipse.jnosql.mapping.reflection;

 public class Foo {

     private String bar;

     private String bar2 = "bar2";

     private String bar3 = "bar3";

     public String getBar() {
         return bar;
     }

     public void setBar(String bar) {
         this.bar = bar;
     }

     String getBar2() {
         return bar2;
     }

     void setBar2(String bar2) {
         this.bar2 = bar2;
     }

     String getBar3() {
         return bar3;
     }

     void setBar3(String bar3) {
         this.bar3 = bar3;
     }
 }
