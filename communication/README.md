![Eclipse JNoSQL Diana Project](https://github.com/JNOSQL/diana-site/blob/master/images/duke-diana.png)

# The communication layer, Diana


The Eclipse JNoSQL communcation layer, Diana, is a flexible and extensible API to connect NoSQL databases. It brings an easy interface to support key-value, column family, document oriented and graph databases as JDBC does for SQL databases.

The basic building blocks hereby are:

* A simple API to support Column NoSQL Database
* A simple API to support Key-value NoSQL Database
* A simple API to support Graph NoSQL Database
* A simple API to support Document Database
* Convention over configuration
* Support for asynchronous queries
* Support for asynchronous write operations
* An easy API to implement, so that NoSQL vendors can comply with it  and test by themselves.

The API's focus is on simplicity and ease of use. Developers should only have to know a minimal set of artifacts to work with the solution. 
The API is built on latest Java 8 features and therefore fit perfectly with the functional features of Java 8. 


## A different API to a different database kind
   
The Eclipse JNoSQL Diana has one API to each different database, so an API to document, column, key-value and graph, where the graph is an API that already exists. The Apache TinkerPop is the API to Graph database.

## Modules

The JNoSQL communication layer has four modules:

* **diana-core**: The JNoSQL API communication commons to all types. 
* **diana-key-value**: The JNoSQL communication API layer to key-value database.
* **diana-column**: The JNoSQL communication API layer to column database.
* **diana-document**: The JNoSQL communication API layer to document database.


## Structure

* **Configuration**: This interface represents the configuration whose a database has. These settings such as password, user, clients are storage and use to create a manager factory.
* **ManagerFactory**: This interface represents the factory whose creates an entity manager.
* **Manager**: The entity manager, that class that interacts with the entity, to do a CRUD Operation. This interface might be extended to capture particular behavior in a NoSQL database.
* **Entity**: The element to have interaction in a database