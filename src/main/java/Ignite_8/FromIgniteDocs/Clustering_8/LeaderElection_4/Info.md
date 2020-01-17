# Leader (as defined by Apache Ignite)
a node that is guaranteed to be selected every time it is requested, regardless of 
topological changes. 
- In this sense, it is acting as a guaranteed COORDINATOR for some tasks. 

## NOTE ON DATA
Usually, cluster leaders is tied to data consistency, so it is handled via elections and
quorom from cluster members. However, data consistency in Apache Ignite is handled by DATA
GRID AFFINITY FUNCTION (i.e. Rendezvous Hashing) , this means we don't have to pick leaders
in the same fashion. 


## METHOD 1: Use Service Grid
- This is the recommended method for Leader or Singleton-Type use cases. 
- Service Grid allows automatic deployment of Cluster Singleton services that are easier to
use. 

... but like anything, we have to know plan B and plan C. 

## METHOD 2: Use the Oldest Node
PROS
- the oldest node remains constant when there are a lot of nodes being added, but not 
removed. 
- the only time the oldest node changes is if it leaves or dies.

CONS
- if a cluster has many ephemeral instances, the oldest node will be in constant flux.


## METHOD 3: Use the Youngest Node
- highly unpredictable. 