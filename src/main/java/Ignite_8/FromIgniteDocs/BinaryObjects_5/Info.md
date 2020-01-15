# BinaryObjects
(As of Ignite 1.5) Ignite began storing data in caches called BinaryObjects

## Advantages
- read fields from serialized object without having to deserialize the entire
object
    - server doesn't need to know about cache key/value classes

- supports DYNAMIC change to object structure
    - supports multiple clients of different versions
    - allows CRUD operations on fields from objects of same type
    
- supports creation of new objects based on type name w/o requiring the class
defs.

- automatically calculates HashCodes/Equals
    - Note, this is only true if your objects can be serialized into binary form. If they can't, you gotta do it
    manually or farm it out to Apache Commons. 

## Restrictions
- fields/types must be unique (non-case specific, i.e. "HASH".equals("hash") == true)
- same reason, doesn't support identical field names at different levels of class hierarchy
- if class implements 'Externalizable' interface it uses 
    - OptimizedMarshaller (rather than BinaryMarshaller)
        - uses writeExternal()/readExternal() to serialize/deserialize objects
        
        
## BinaryObject and CacheStore
Just a basic note about using "withKeepBinary()"
- This setting doesn't affect the way USER objects are passed to the cache. 
    - cachestore (by default) works w/ BinaryObjects or deserialized classes. 
- This can be controlled using the CacheConfiguration.storeKeepBinary() flag. 
    - TRUE ==> uses BinaryObjects
    - FALSE ==> deserialized classes.
    
    
## NameMapper and IDMapper
Ignite never uses FULL strings for field/type names. 
- uses hash codes for performance reasons. 
- per GridGain/Apache, collisions are "virtually non-existent"
- By default, you shouldn't have to configure anything if you are using Java. 

### BinaryNameMapper
maps type/class and field names to different names

### BinryIdMapper
maps given from BinaryNameMapper type and filed name to ID that will be used by Ignite in internals.


