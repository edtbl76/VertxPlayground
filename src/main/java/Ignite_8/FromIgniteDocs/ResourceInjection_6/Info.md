# Dependency Injection
By annotating resources, they will be "properly injected" into 
- task
- job
- closure
- SPI

before they are initialized.

## FIELD-BASED
- ignite sets the value of the field at injection time. 
    - IGNORES ACCESS MODIFIER

## METHOD-BASED
- methods that are annotated should accept an input parameter
of the type corresponding to an injected resource. 
    - the method is invoked at injection time w/ the proper resource
    passed in as the input argument
    
## SUPPORTED PRE-DEFINED IGNITE RESOURCES
| Resource | Description | 
| --- | --- |
| CacheNameResource | injects Grid Cache name provided by CacheConfiguration.getName() |
| CacheStoreSessionResource | injects CacheStoreSession instance |
| IgniteInstanceResource | (most common) injects current instance of Ignite API | 
| JobContextResource |  injects a ComputeJobContext (holds useful info about a particular job execution. EX: you can get the name of the cache containing the entry for which a job was COLLOCATED | 
| LoadBalancerResource | injects an instance of ComputeLoadBalancer that can be used by a task for load balancing |
| ServiceResource | injects Ignite service by specified service name |
| SpringApplicationContextResource | no one cares, we don't use Spring |
| SpringResource | still... no one cares |
| TaskContinuousMapperResource | injects instance of ComputeTaskContinuousMapper. (Allows emitting of jobs from task at any point, even after the initial mapping phase) |
| TaskSession Resource | injects instance of ComputeTaskSession resrc which defines distributed session for a parcitular task execution |



