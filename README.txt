
This library contains classes that are used both in HoskiAdmin and HoskiWebapp applications.

Datastore
---------

Applications use Google App Engine High Replication Datastore. This datastore is more
hierarchical than relational. Read more at appengine.google.com.

All entities have common Root(1) ancestor key. Transactions are limited to entities
with common ancestor keys.

Annual data has Year(yyyy) key. Example Root(1)/Year(2013)/....

Year key is used in automatic cleanup (at 1st of january). Exceptions are Year and
Attachment kinds, which are not removed automatically.

See Google App Engine documents at https://developers.google.com/appengine

See dependencies at pom.xml.

Compiling
---------
mvn install

Deployment
----------

Commit to git
-------------

Change <version>1.0.3</version> 

in pom.xml

Deploy to Maven Central Repository
----------------------------------

See http://central.sonatype.org/ how to create environment for deployment

Run:

mvn clean:clean javadoc:jar source:jar deploy
 
