# Eclipse JNoSQL

![Eclipse JNOSQL logo](http://www.jnosql.org/images/home_logo.png)

Eclipse JNoSQL is a Java framework that streamlines the integration of Java applications with NoSQL databases. It defines a set of APIs and provides a standard implementation for most NoSQL databases. This clearly helps to achieve very low coupling with the underlying NoSQL technologies used in applications. The project has two layers:

The project has two layers:

1. **Communication Layer**: A set of APIs that defines communication with NoSQL databases. Compared with traditional the RDBMS world, they are like the JDBC API. It contains four modules, one for each NoSQL database type: Key-Value, Column Family, Document, and Graph.

1. **Mapping Layer**: These APIs help developers to integrate their Java application with the NoSQL database. This layer is annotation-driven and uses technologies like CDI and Bean Validation, making it simple for developers to use. In the traditional RDBMS world, this layer can be compared to the Java Persistence API or object-relational mapping frameworks such as Hibernate.


![Layers](https://www.eclipse.org/community/eclipse_newsletter/2018/april/images/jnosql_map.png)

## One Mapping API, multiples databases

Eclipse NoSQL has one API for each NoSQL database type. However, it uses the same annotations to map Java objects. Therefore, with just these annotations that look like JPA, there is support for more than twenty NoSQL databases.

```java
@Entity
public class God {
 
    @Id
    private String id;
    @Column
    private String name;
    @Column
    private String power;
 //... 
}

```


Another example can be found in an article that demonstrates the same annotated entity used across different NoSQL databases: Redis, Cassandra, Couchbase, and Neo4J. The approach is "stick to the API": the developer can replace Redis with Hazelcast, as both implement the Key-Value API, thus avoiding vendor lock-in with one of these databases.

Vendor lock-in is one of the things any Java project needs to consider when choosing NoSQL databases. If there's a need for a switch, other considerations include: time spent on the change, the learning curve of a new API to use with this database, the code that will be lost, the persistence layer that needs to be replaced, etc. Eclipse JNoSQL avoids most of these issues through the Communication APIs. It also has template classes that apply the design pattern 'template method’ to databases operations. And the Repository interface allows Java developers to create and extend interfaces, with implementation automatically provided by Eclipse JNoSQL: support method queries built by developers will automatically be implemented for them.


```java
public interface GodRepository extends Repository<God, String> {
 
    Optional<God> findByName(String name);
 
}
 
GodRepository repository = ...;
God diana = God.builder().withId("diana").withName("Diana").withPower("hunt").builder();
repository.save(diana);
Optional idResult = repository.findById("diana");
Optional nameResult = repository.findByName("Diana");
```


## Beyond JPA

JPA is a good API for object-relationship mapping and it's already a standard in the Java world defined in JSRs. It would be great to use the same API for both SQL and NoSQL, but there are behaviors in NoSQL that SQL does not cover, such as time to live and asynchronous operations. JPA was simply not made to handle those features.


```java
ColumnTemplateAsync templateAsync = …;
ColumnTemplate template = …;
God diana = God.builder().withId("diana").withName("Diana").withPower("hunt").builder();
Consumer<God> callback = g -> System.out.println("Insert completed to: " + g);
templateAsync.insert(diana, callback);
Duration ttl = Duration.ofSeconds(1);
template.insert(diana, Duration.ofSeconds(1));
```


## A Fluent API

Eclipse JNoSQL is a fluent API that makes it easier for Java developers create queries that either retrieve or delete information in a Document type, for example.


## Let's not reinvent the wheel: Graph

The Communication Layer defines three new APIs: Key-Value, Document and Column Family. It does not have new Graph API, because a very good one already exists. Apache TinkerPop is a graph computing framework for both graph databases (OLTP) and graph analytic systems (OLAP). Using Apache TinkerPop as Communication API for Graph databases, the Mapping API has a tight integration with it.



## Particular behavior matters in NoSQL database

Particular behavior matters. Even within the same type, each NoSQL database has a unique feature that is a considerable factor when choosing a database over another. This ‘’feature’’ might make it easier to develop, make it more scaleable or consistent from a configuration standpoint, have the desired consistency level or search engine, etc. Some examples are Cassandra and its Cassandra Query Language and consistency level, OrientDB with live queries, ArangoDB and its Arango Query Language, Couchbase with N1QL - the list goes on. Each NoSQL has a specific behavior and this behavior matters, so JNoSQL is extensible enough to capture this substantiality different feature elements.


Find out more information and get involved!

* Website: http://www.jnosql.org/
* Twitter: https://twitter.com/jnosql
* GitHub Repo: https://github.com/eclipse?q=Jnosql
* Mailing List: https://accounts.eclipse.org/mailing-list/jnosql-dev
