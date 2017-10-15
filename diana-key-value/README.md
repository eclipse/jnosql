![Diana Key-value](https://github.com/JNOSQL/diana-site/blob/master/images/duke-diana.png)

# Diana Key-value


The JNoSQL communication API layer to key-value database.

## NoSQL key-value type

 Key-value (KV) stores use the associative array (also known as a map or dictionary) as their fundamental data model.
In this model, data is represented as a collection of key-value pairs, such that each possible key appears at most
once in the collection. The key-value model is one of the simplest non-trivial data models, and richer data models are often implemented as an extension of it.
The key-value model can be extended to a discretely ordered model that maintains keys in lexicographic order.
This extension is computationally powerful, in that it can efficiently retrieve selective key ranges.
Key-value stores can use consistency models ranging from eventual consistency to serializability.
Some databases support ordering of keys. There are various hardware implementations, and some users maintain
data in memory (RAM), while others employ solid-state drives or rotating disks.
 
 ## Code structure
 
 The Key-value API has the following structure:

* **KeyValueConfiguration**: This interface represents the configuration whose a database has. These settings such as password, user, clients are storage and use to create a manager factory.
* **BucketManagerFactory**: This interface represents the factory whose creates an entity manager.
* **BucketManager**: The entity manager, that class that interacts with the KeyValueEntity, to do a CRUD Operation. This interface might be extended to capture particular behavior in a NoSQL database.
* **KeyValueEntity**: The key and it respective value.

```java


    public static void main(String[] args) {

        KeyValueConfiguration<?> configuration = new HazelCastKeyValueConfiguration();
        try (BucketManagerFactory<?> managerFactory = configuration.get()) {
            BucketManager bucket = managerFactory.getBucketManager("bucket");
            List<String> list = managerFactory.getList("bucketList", String.class);
            Set<String> set = managerFactory.getSet("bucketSet", String.class);
            Map<String, Integer> map = managerFactory.getMap("bucketList", String.class, Integer.class);
            Queue<String> queue = managerFactory.getQueue("queueList", String.class);
            bucket.put("key", "value");
            Optional<Value> value = bucket.get("key");
        }


    }
```