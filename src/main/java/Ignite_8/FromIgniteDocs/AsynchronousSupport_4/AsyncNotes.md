# Supported Interfaces


    IgniteCompute
    IgniteCache
    Transaction
    IgniteServices
    IgniteMessaging
    IgniteEvents
    
    
## Methods

- get()
    - this waits for result of async task in NON-BLOCKING fashion
    - IF AND ONLY IF the CLOSURE was registered using 
        - IgniteFuture.listen() or
        - IgniteFuture.chain()
        
This is important. The get() method will act SYNCHRONOUSLY if you don't register
a closure. 

### NOTE

If an async task has been COMPLETED by the time the closure is passed to 
listen() or chain()
- closure is executed SYNCHRONOUSLY by calling thread. 

DO NOT call synchronous cache/compute operations from closure implementation, because
it will result in deadlock/starvation
- closures are processed from 
    - system pool (async cache operations)
    - public pool (async compute operations) 
- this is a use case for Custom Thread Pools

### NOTE ON CLOSURES
- We're just talking about the lambda/method expressions in this case. This replaces
the use of anonymous inner classes. 
- A CLOSURE is a code structure that can be accessed and passed w/ access to the data from 
the enclosing scope. 