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



![Eclipse JNoSQL Aphrodite Project](https://github.com/JNOSQL/jnosql.github.io/blob/master/images/duke-aphrodite.png)

## Communication Query

Eclipse JNoSQL Aphrodite is the project that contains the syntax query to JNoSQL API.

The general concepts about the statements:

* All instructions end with a break like `\n`
* It is case sensitive
* All keywords must be in lowercase
* The goal is to look like SQL, however simpler
* The scope of this API is to key-value, column and document type, once Apache Tinkerpop already have one to Graph.
* Even with this query, a specific implementation may not support an operation, condition, so on. E.g: Column family may not support query with equals operator in a different field that is not the key field.
* The goal of the API is not about forgotten the specific behavior that there is in a particular database. These features matter, that's why there's an extensible API.

## Column and Document 

### Select

The select statement reads one or more fields for one or more entities. It returns a result-set of the entities matching the request, where each entity contains the fields for corresponding to the query.

```sql
select_statement ::=  SELECT ( select_clause | '*' )
                      FROM entity_name
                      [ WHERE where_clause ]
                      [ SKIP (integer) ]
                      [ LIMIT (integer) ]
                      [ ORDER BY ordering_clause ]
```

##### Sample

```sql
select * from God
select  name, age ,adress.age from God order by name desc age desc
select  * from God where birthday between "01-09-1988" and "01-09-1988" and salary = 12
select  name, age ,adress.age from God skip 20 limit 10 order by name desc age desc
```

### Insert

Inserting data for an entity is done using an INSERT statement:

```sql

insert_statement ::=  INSERT entity_name (name = value, (`,` name = value) *) [ TTL ]
```

##### Sample:


```sql
insert God (name = "Diana", age = 10)
insert God (name = "Diana", age = 10, power = {"sun", "god"})
insert God (name = "Diana", age = 10, power = {"sun", "god"}) 1 day

```

### Update

Updating an entity is done using an **UPDATE** statement:


```sql

insert_statement ::=  UPDATE entity_name (name = value, (`,` name = value) *)
```

##### Sample:


```sql
update God (name = "Diana", age = 10)
update God (name = "Diana", age = 10, power = {"hunt", "moon"})
```

### Delete

Deleting either an entity or fields uses the **DELETE** statement

```sql
delete_statement ::=  DELETE [ simple_selection ( ',' simple_selection ) ]
                      FROM entity_name
                      WHERE where_clause
```

##### Sample:


```sql
delete from God
delete  name, age ,adress.age from God
```

## Key-value 

### GET

Retrieving data for an entity is done using an **GET** statement:

```sql

get_statement ::=  GET ID (',' ID)*
```


##### Sample:


```sql
get "Diana" "Artemis"
get "Apollo"
```

### PUT

To either insert or overrides values from a key-value database use the **PUT** statement.
```sql

put_statement ::=  PUT {KEY, VALUE, [TTL]} (',' {KEY, VALUE [TTL]})*
```


##### Sample:


```sql
put {"Diana" ,  "The goddess of hunt", 10 second}
put {"Diana" ,  "The goddess of hunt"}
put {"Diana" ,  "The goddess of hunt", 10 second}, {"Aphrodite" ,  "The goddess of love"}

```

### REMOVE

To delete one or more entities use the **REMOVE** statement

```sql

del_statement ::=  GET ID (',' ID)*
```

##### Sample:

```sql

remove "Diana" "Artemis"
remove "Apollo"
```


#### WHERE

The WHERE clause specifies a filter to the result. These filters are booleans operations that are composed of one or more conditions appended with the and (**AND**) and or (**OR**) operators.


##### Conditions

Condition performs different computations or actions depending on whether a boolean query condition evaluates to **true** or **false**.
The conditions are composed of three elements:

 1. **Name**, the data source or target, to apply the operator
 1. **Operator**, that defines comparing process between the name and the value.
 1. **Value**, that data that receives the operation.

##### Operators

The Operators are:


| Operator | Description |
| ------------- | ------------- |
| **=**         | Equal to |
| **>**         | Greater than|
| **<**         | Less than |
| **>=**        | Greater than or equal to |
| **<=**        | Less than or equal to |
| **BETWEEN**   | TRUE if the operand is within the range of comparisons |
| **NOT**       | Displays a record if the condition(s) is NOT TRUE	|
| **AND**       | TRUE if all the conditions separated by AND is TRUE|
| **OR**        | TRUE if any of the conditions separated by OR is TRUE|
| **LIKE**      |TRUE if the operand matches a pattern	|
| **IN**        |TRUE if the operand is equal to one of a list of expressions	|

#### The value

The value is the last element in a condition, and it defines what it 'll go to be used, with an operator, in a field target.


There are six types:

* number is a mathematical object used to count, measure and also label where if it is a decimal, will become double, otherwise, long. E.g.: `age = 20`, `salary = 12.12`
* string one or more characters:  among two double quotes `"`. E.g.: name = "Ada Lovelace"
* Convert: convert is a function where given the first value parameter, as number or string, it will convert to the class type of the second one. E.g.: `birthday = convert("03-01-1988", java.time.LocalDate)`
* parameter: the parameter is a dynamic value, which means, it does not define the query, it'll replace in the execution time. The parameter is at `@` followed by a name. E.g.: `age = @age`
* array: A sequence of elements that can be either number or string that is between braces ` {` `}`. E.g.: `power = {"Sun", "hunt"}`
* json: JavaScript Object Notation is a lightweight data-interchange format. E.g.: `siblings = {"apollo": "brother", "zeus": "father"}`

#### SKIP

The **SKIP** option to a **SELECT** statement defines where the query should start,

#### LIMIT

The **LIMIT** option to a **SELECT** statement limits the number of rows returned by a query, 

#### ORDER BY

The ORDER BY clause allows selecting the order of the returned results. It takes as argument a list of column names along with the order for the column (**ASC** for ascendant and **DESC** for the descendant, omitting the order being equivalent to **ASC**). 

#### TTL

Both the **INSERT** and **PUT** commands support setting a time for data in an entity to expire. It defines the time to live of an object that is composed of the integer value and then the unit that might be `day`, `hour`, `minute`, `second`, `millisecond`, `nanosecond`. E.g.: `ttl 10 second`