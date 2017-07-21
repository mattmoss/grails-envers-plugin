package envers

import grails.plugins.*
import net.lucasward.grails.plugin.EnversPluginSupport
import net.lucasward.grails.plugin.RevisionsOfEntityQueryMethod
import grails.core.GrailsApplication
import grails.core.GrailsDomainClass
import org.hibernate.SessionFactory

class EnversGrailsPlugin extends Plugin {
    def version = "2.4.1"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "3.0.0 > *"
    
    // the other plugins this plugin depends on
    def observe = ['hibernate']
    def loadAfter = ['hibernate']
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
        "grails-app/views/error.gsp",
        "grails-app/domain/**",
         "src/main/groovy/net/lucasward/grails/plugin/StubSpringSecurityService.groovy",
         "src/main/groovy/net/lucasward/grails/plugin/SpringSecurityRevisionListener.groovy",
         "src/main/groovy/net/lucasward/grails/plugin/SpringSecurityServiceHolder.groovy",
         "src/test/groovy/net/lucasward/grails/plugin/Book.java",
         "src/test/groovyjava/net/lucasward/grails/plugin/UserRevisionEntity.java",
         "grails-app/conf/hibernate/hibernate.cfg.xml",
         "web-app/**"
    ]

    // TODO Fill in these fields
    def title = 'Grails Envers Plugin' // Headline display name of the plugin
    def author = 'Lucas Ward, Jay Hogan, Colin Harrington'
    def authorEmail = ""
    def description = '''\
Plugin to integrate grails with Hibernate Envers
'''

    // URL to the plugin's documentation
    def documentation = "http://grails.org/plugin/envers"

    // Extra (optional) plugin metadata

    // License: one of 'APACHE', 'GPL2', 'GPL3'
//    def license = "APACHE"

    // Details of company behind the plugin (if there is one)
//    def organization = [ name: "My Company", url: "http://www.my-company.com/" ]

    // Any additional developers beyond the author specified above.
//    def developers = [ [ name: "Joe Bloggs", email: "joe@bloggs.net" ]]

    // Location of the plugin's issue tracker.
//    def issueManagement = [ system: "JIRA", url: "http://jira.grails.org/browse/GPMYPLUGIN" ]

    // Online location of the plugin's browseable source code.
//    def scm = [ url: "http://svn.codehaus.org/grails-plugins/" ]

    Closure doWithSpring() { {->
            // TODO Implement runtime spring config (optional)
        }
    }

    void doWithDynamicMethods() { //TODO find the context
        // TODO Implement registering dynamic methods to classes (optional)
      for (entry in applicationContext.getBeansOfType(SessionFactory)) {
        SessionFactory sessionFactory = entry.value
        registerDomainMethods(grailsApplication, sessionFactory)
      }
    }
    
    
    
        private void registerDomainMethods(GrailsApplication application, SessionFactory sessionFactory) {
            application.domainClasses.each { GrailsDomainClass gc ->
                def getAllRevisions = new RevisionsOfEntityQueryMethod(sessionFactory, gc.clazz)
                if (EnversPluginSupport.isAudited(gc)) {
                    MetaClass mc = gc.getMetaClass()
    
                    mc.static.findAllRevisions = {
                        getAllRevisions.query(null, null, [:])
                    }
    
                    mc.static.findAllRevisions = { Map parameters ->
                        getAllRevisions.query(null, null, parameters)
                    }
    
                    EnversPluginSupport.generateFindAllMethods(gc, sessionFactory)
                    EnversPluginSupport.generateAuditReaderMethods(gc, sessionFactory)
                }
            }
        }

    void doWithApplicationContext() {
        // TODO Implement post initialization spring config (optional)
    }

    void onChange(Map<String, Object> event) {
        // TODO Implement code that is executed when any artefact that this plugin is
        // watching is modified and reloaded. The event contains: event.source,
        // event.application, event.manager, event.ctx, and event.plugin.
    }

    void onConfigChange(Map<String, Object> event) {
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }

    void onShutdown(Map<String, Object> event) {
        // TODO Implement code that is executed when the application shuts down (optional)
    }
}
