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

import junit.framework.TestCase
import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import org.grails.core.DefaultGrailsDomainClass

import grails.core.GrailsDomainClass

import org.hibernate.SessionFactory
import org.hibernate.envers.DefaultRevisionEntity
import org.hibernate.envers.RevisionType

/**
 * TODO figure out how to use spock-mocking api without the unit-test syntax overhead.
 * Create a spock helper
 * @author Labi Oyapero
 *
 */
@TestFor(Customer)
@TestMixin(GrailsUnitTestMixin)
class EnversPluginSupportTests extends TestCase{

    public void testIsAudited() {
        def gc = new DefaultGrailsDomainClass(Address)
        assert EnversPluginSupport.isAudited(gc) == true
    }

    public void testIsNotAudited() {
        def gc = new DefaultGrailsDomainClass(State)
        assert EnversPluginSupport.isAudited(gc) == false
    }

    public void testIsAuditedAtFieldLevelOnly() {
        def gc = new DefaultGrailsDomainClass(Userr)
        assert EnversPluginSupport.isAudited(gc) == true
    }

    public void testCollapseRevisions() {
        Customer user = new Customer(name: 'collapseTest');
        DefaultRevisionEntity revisionEntity = new DefaultRevisionEntity(id: 1)
        RevisionType revType = RevisionType.ADD
        def revision = [user, revisionEntity, revType]
        def collapsed = EnversPluginSupport.collapseRevision(revision)

        assert collapsed instanceof Customer
        assert collapsed.name == user.name
        assert collapsed.revisionEntity == revisionEntity
        assert collapsed.revisionType == RevisionType.ADD
    }

    public void testCollapseRevisionsWithTooSmallArray() {
        shouldFail {
            EnversPluginSupport.collapseRevision([])
        }
    }

    public void testCollapseRevisionsWithTooLargeArray() {
        shouldFail {
            EnversPluginSupport.collapseRevision([1, 2, 3, 4])
        }
    }

    public void testGenerateFindAllMethods() {
        SessionFactory sessionFactory = [] as SessionFactory;
        GrailsDomainClass gdc = new DefaultGrailsDomainClass(Customer.class)
        EnversPluginSupport.generateFindAllMethods(gdc, sessionFactory)//.createMock()

        assert Customer.metaClass.getStaticMetaMethod("findAllRevisionsByEmail", ["Email"]) != null
        assert Customer.metaClass.getStaticMetaMethod("findAllRevisionsByName", ["Email"]) != null
        assert Customer.metaClass.getStaticMetaMethod("findAllRevisionsByAddress", ["Email"]) != null
        assert Customer.metaClass.getStaticMetaMethod("findAllRevisionsById", ["Email"]) != null
    }

    public void testGenerateAuditReaderMethods() {
        SessionFactory sessionFactory = [] as SessionFactory;
        GrailsDomainClass gdc = new DefaultGrailsDomainClass(Customer.class)
        EnversPluginSupport.generateAuditReaderMethods(gdc, sessionFactory)//sessionFactory.createMock()

        assert Customer.metaClass.getStaticMetaMethod("getCurrentRevision", []) != null
        assert Customer.metaClass.getMetaMethod("retrieveRevisions", []) != null
        assert Customer.metaClass.getMetaMethod("findAtRevision", [3]) != null
    }

}
