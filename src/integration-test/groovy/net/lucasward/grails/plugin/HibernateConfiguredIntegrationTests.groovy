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

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import spock.lang.Specification

/**
 * Tests to make sure that Hibernate Configured classes still work with this plugin
 */
@Integration
@Rollback
class HibernateConfiguredIntegrationTests extends Specification {

    void "testBasicModification"() {
        when:
        Address address = new Address(city:"New York", zip: "76051")
        address.save()
        Address myAddress = Address.findByCity("New York")
        def results = Address.findAllRevisionsById(myAddress.id)

        then:
        results.size() == 1
    }
}
