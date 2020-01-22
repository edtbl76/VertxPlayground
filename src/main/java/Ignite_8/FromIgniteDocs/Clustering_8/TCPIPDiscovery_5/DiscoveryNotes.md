# Ignite Discovery

## TCP/IP (Default Discovery)
cluster nodes are organized into a Ring-Topology. 
- PROS

- CONS
    - message traversal is very slow w/ large topologies. 
        - results in poor performance for simple operations such as new node join/failure detection. 
        
        
## Zookeeper 
- specifically designed for "Massive Ignite Deployments" that need to preserve ease of scalability/linear performance. 
- Ignite uses ZK as a "single point of synchronization" to organize an Ignite Cluster into a STAR-SHAPED topology. 
    - ZK cluster sits in the center
    - Ignite nodes exchange discovery events through ZK. 
- NOTE: 
    - Peer-to-peer communication is still performed using the Communication SPI.


# Multiple Clusters On The Same Machine. 
(Look at the code examples)
- The only variance between the two sets of code are the port ranges supplied to the
TcpCommunicationSpi and the discoverySpi. 


## SUPPORTED OPTIONS
- Apache jclouds
- Amazon S3
- Amazon ELB
- Google Cloud Storage 
- JDBC (i.e. IPs are stored in database)
- Shared File System
- Kubernetes
- Zookeeper

## Failure Detection Timeout
- NOTE
    - failureDetectionTimeout automatically controls config params 
        - socket timeout, message ack, etc. 
    - setting those config params explicitly DISABLES failureDetectionTimeout
    
    
Configuration Table

| Setter | Description | Default Value | 
| --- | --- | --- | 
| setIpFinder(TcpDiscoveryIpFinder) | finder used to share info about node IPs | TcpDiscoveryMulticastIpFinder |
| setLocalAddress(String) | sets local host IP address | java.net.InetAddress.getLocalHost() |
| setLocalPort(int) | port that SPI listens to | 47500 |
| setLocalPortRange(int) | # of ports SPI will try to bind to AFTER the initial setLocalPort() | 100 |
| setReconnectCount(int) | # of attempts to restablish connect to another node | 2 |
| setNetworkTimeout(long) | (Network Operations) max TO in millis | 5k |
| setSocketTimeout(long) | (Socket Operations) used to limit connection time and write-to-socket time | 2k |
| setAckTimeout(long) | TO for recv sent msgs. If not recv'd in this window, send is considered failed, and msg send will be retried | 2k |
| setJoinTimeout(long) | if non-shared IP finder is used, and node fails to connect to entire port range and all addresses, node will try again in this timeout | 0 (Infinite) |
| setThreadPriority(int) | thread priority for threads exec'd by SPI | 0 |
| setStatisticsPrintFrequency(int) | print freq in millis. 0 = no print. if value > 0, and log isn't quiet, then stats are printed at INFO once a period | true |