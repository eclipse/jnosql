![Diana document](https://github.com/JNOSQL/diana-site/blob/master/images/duke-diana.png)

# Diana Document


The JNoSQL communication API layer to document database.

## NoSQL document type

A document-oriented database, or document store,is a computer program designed for storing, retrieving, and managing document-oriented information,also known as semi-structured data.
 Document-oriented databases are one of the main categories of NoSQL databases,and the popularity of the term "document-oriented database" has grown with the use of the term NoSQL itself.XML databases are a subclass of document-oriented databases that are optimized to work with XML documents.Graph databases are similar, but add another layer, the relationship, which allows them to link documentsfor rapid traversal.Document-oriented databases are inherently a subclass of the key-value store, another NoSQL database concept.The difference lies in the way the data is processed; in a key-value store the data is considered to be inherentlyopaque to the database, whereas a document-oriented system relies on internal structure in the documentin order to extractmetadata that the database engine uses for further optimization. Although the difference is often moot due to toolsin the systems,conceptually the document-store is designed to offer a richer experience with modern programming techniques.Document databases contrast strongly with the traditional relational database (RDB).Relational databases generally store data in separate tables that are defined by the programmer, and a single objectmay be spread across several tables.Document databases store all information for a given object in a single instance in the database, and every storedobject can be different from every other.This makes mapping objects into the database a simple task, normally eliminating anything similar to anobject-relational mapping. This makes document stores attractivefor programming web applications, which are subject to continual change in place, and where speed of deploymentis an important issue.
 
 
 ## Code structure
 
 The Document API has the following structure:

* **DocumentConfiguration**: This interface represents the configuration whose a database has. These settings such as password, user, clients are storage and use to create a manager factory.
* **DocumentCollectionManagerFactory**: This interface represents the factory whose creates an entity manager.
* **DocumentCollectionManager**: The entity manager, that class that interacts with the DocumentEntity, to do a CRUD Operation. This interface might be extended to capture particular behavior in a NoSQL database.
* **DocumentEntity**: The document entity, this interface represents a unit element in a document collection. This interface has the document collection whose the unit belongs and also its documents.
* **Document**: The document is an element in _DocumentEntity_; it`s a tuple that has key-value whose the key is the name and value is the information.


```java

  public static void main(String[] args) {

        ColumnConfiguration condition = //configuration instance

        try(ColumnFamilyManagerFactory managerFactory = condition.get()) {
            ColumnFamilyManager entityManager = managerFactory.get("keyspace");
            ColumnEntity entity = ColumnEntity.of("column family");
            Column id = Column.of("id", 10L);
            entity.add(id);
            entity.add(Column.of("version", 0.001));
            entity.add(Column.of("name", "Diana"));
            entity.add(Column.of("options", Arrays.asList(1, 2, 3)));

            entityManager.insert(entity);

            ColumnQuery query = select().from("column family").where(eq(id)).build();

            Optional<ColumnEntity> result = entityManager.singleResult(query);
            System.out.println(result);

        }
```