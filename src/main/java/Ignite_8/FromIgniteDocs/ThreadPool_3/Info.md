# WELL KNOWN POOLS

## System Pool
- Processes all cache-related tasks other than SQL and a few exceptions that are 
forwarded to the QUERIES pool

- Responsible for Ignite Compute task cancellation tasks. 

- default pool size = max (8, total # of cores) 
    - Can be set via 
    
        
        IgniteConfiguration.setSystemThreadPoolSize()
    
## Public Pool
- "worker" of Ignite compute-gride
- All "compute" tasks are recvd + procd by this pool. 
- default size is same as System Pool
    - can be set via    

    
        IgniteConfiguration.setPublicThreadPoolSize()
   

## Queries Pool
- handles SQL, Scan and SPI queries
    - (SPI = Service Provider Interface), which is a java-esque way of 
    providing plugins for the purposes of Service Discovery.
- default size is same as system and public pools
    - can be set via
    
        
        IgniteConfiguration.setQueryThreadPoolSize()
        

## Services Pool
- this is a separate thread pool for use with IgniteServices. 
    - the purpose is to allow segregation between Service and ComputeGrids
        - if managed properly this prevents starvation and deadlocks.
- same default size as the rest
    - can be set via
    
        
       IgniteConfiguration.setServiceThreadPoolSize()
       

## Striped Pool
- special pool for accelerating basic cache tasks and transactions by spreading
the task execution across multiple stripes of threads that don't contend w/ each
other.
    - A stripe is basically a set of threads that is passed in to a StripedPool. 
    You can create multiple striped pools to segregate tasks for different caches.
- same default size
    - can be set via
    
    
        IgniteConfiguration.setStripedPoolSize()
        
        
## DataStreamer
- special pool for processing messages and requests coming from "IgniteDataStreamer"
(or other stream adapters that use the IgniteDataStreamer)
- same default pool size
    - can be set via
    
        
        IgniteConfiguration.setDataStreamerThreadPoolSize()
        
        
        
## Customization
- you guessed it. We can make our OWN Thread Pools if we have specific compute tasks
that should be executed asynchronously from others for the purpose of avoiding 
deadlocks. 

 
