/*
   * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.lucasward.grails.plugin

import junit.framework.TestCase;

import org.hibernate.envers.query.AuditQuery

import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin;
import grails.test.mixin.support.GrailsUnitTestMixin;

import org.junit.Before

import spock.lang.Specification;

@TestFor(Customer)
@TestMixin(GrailsUnitTestMixin)
class PaginationHandlerTests extends TestCase{

    PaginationHandler handler

    void setUp() {
        handler = new PaginationHandler()
    }

    void testAddMax() {
        AuditQuery mockQuery = [setMaxResults: {int i -> assert i == 10}] as AuditQuery;
        /*AuditQuery mockQuery = Mock(AuditQuery)
        mockQuery.demand.setMaxResults(1) */
        handler.addPagination(mockQuery, [max: 10])
    }

    void testCallWithoutMax() {
      AuditQuery mockQuery = [] as AuditQuery;
        //AuditQuery mockQuery = Mock(AuditQuery)
      handler.addPagination(mockQuery, [:])
    }

    void testAddOffset() {
      AuditQuery mockQuery = [setFirstResult: {int i -> assert i == 10}] as AuditQuery;
        //AuditQuery mockQuery = Mock(AuditQuery)
        //mockQuery.demand.setFirstResult(1) {int i -> assert i == 10}

        handler.addPagination(mockQuery, [offset: 10])
    }

    void testCallWithoutOffset() {
      AuditQuery mockQuery = [] as AuditQuery;
        //AuditQuery mockQuery = Mock(AuditQuery)

        handler.addPagination(mockQuery, [:])

        //mockQuery.verify()
    }
}
