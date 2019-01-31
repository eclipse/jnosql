![Eclipse JNoSQL Artemis Project](https://github.com/JNOSQL/diana-site/blob/master/images/duke-artemis.png)
# Eclipse JNoSQL Artemis
 Eclipse JNoSQL Artemis is a flexible and extensible OxM to connect NoSQL databases. It brings an easy interface to support key-value, column family, document oriented and graph databases using as base Diana in CDI based.

* CDI Based
* Diana Based
* Events to insert, delete, update
* Supports to Bean Validation
* OxM Annotation Based
* Configurable and extensible

The API's focus is on simplicity and ease of use. Developers should only have to know a minimal set of artifacts to work with the solution. The API is built on latest Java 8 features and therefore fit perfectly with the functional features of Java 8. 

The project has five modules:

* *The **artemis-core**: The Eclipse JNoSQL mapping, Artemis, commons project.
* The **artemis-configuration**: The Eclipse JNoSQL reader to Artemis project.
* The **artemis-column**: The Eclipse JNoSQL mapping, Artemis, to column NoSQL database.
* The **artemis-document**: The Eclipse JNoSQL mapping, Artemis, to document NoSQL database.
* The **artemis-key-value**: The Eclipse JNoSQL mapping, Artemis, to key-value NoSQL database.
* The **artemis-validation**: The Eclipse JNoSQL mapping, Artemis, that offers support to Bean Validation


The graph API is an extension because the communication layer to the graph is Apache TinkerPop.

## Eclipse JNoSQL Artemis Core


The Eclipse JNoSQL Artemis Core is the commons project in the mapping project. It has the commons annotations.

### Entities Mapping

* `@Entity`: Specifies that the class is an entity. This annotation is applied to the entity class.
* `@Column`: Specifies a mapped column for a persistent property or field.
* `@Id`: Defines the field is the key of the Entity
* `@Embeddable`:Defines a class whose instances are stored as an intrinsic part of an owning entity and share the identity of the entity.
* `@MappedSuperclass`: Designates a class whose mapping information is applied to the entities that inherit from it. A mapped superclass has no separate table defined for it.
* `@Convert` This annotation enables the converter resource.

E.g.:

```java
@Entity
public class Person {

    @Id
    private long id;

    @Column
    private String name;

    @Column
    private int age;

    @Column
    private List<String> phones;
}    
```


#### Convert annotation

```java
public class MoneyConverter implements AttributeConverter<Money, String>{

    @Override
    public String convertToDatabaseColumn(Money attribute) {
        return attribute.toString();
    }

    @Override
    public Money convertToEntityAttribute(String dbData) {
        return Money.parse(dbData);
    }
}

@Entity
public class Worker {

    @Column
    private String name;

    @Column
    private Job job;

    @Column("money")
    @Convert(MoneyConverter.class)//converts to String on the database
    private Money salary;
}
```

## @ConfigurationUnit

Expresses a dependency to a configuration and its associated persistence unit.

```java

@ConfigurationUnit
private ColumnFamilyManagerFactory<DatabaseImplementation> factory;
   
@ConfigurationUnit(fileName = "column.json", name = "name")
private ColumnFamilyManagerFactory<?> factoryB;
    
```


### @Database

The database qualifier used on Eclipse JNoSQL Artemis such as defines which interpreter will be used on Repository.

```java
public interface PersonRepository extends Repository<Person, Long> {
}


    @Inject
    @Database(value = DatabaseType.COLUMN)
    private PersonRepository repository;

    @Inject
    @Database(value = DatabaseType.DOCUMENT)
    private PersonRepository repository;

    @Inject
    @Database(value = DatabaseType.KEY_VALUE)
    private PersonRepository repository;        
    
```