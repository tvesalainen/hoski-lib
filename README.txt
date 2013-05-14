
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
