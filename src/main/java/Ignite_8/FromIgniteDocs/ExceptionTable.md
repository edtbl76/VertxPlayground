| Exception | Description | Action | Default? | 
| --- | --- | --- | --- |
| IgniteException | error condition in Grid | Task Failed. Exit From Method | YES | 
| IgniteClientDisconnectedException | client is disconnected from cluster | Wait on FUTURE. Retry | YES |
| IgniteAuthenticationException | node/security auth failure | Task Failed. Exit from Method | NO |
| IgniteClientException | thrown from Cache Operations | Check exception message for action | YES | 
| IgniteDeploymentException | (Compute Grid API) fails to deploy job/task on a node | Task Failed. Exit from Method | YES |
| IgniteInterruptedException | wrap standard InterruptedException into Ignite hierarchy | Retry after clearing standard flag | YES |
| IgniteSpiException | thrown by Ignite Service Provider Interfaces | Task Failed. Exit From Method | YES | 
| IgniteSQLException | thrown for SQL query errors (offers pass through for query specific error codes) | Task Failed. Exit From Method | YES |
| IgniteAccessControlException | authN/authZ failures | Task Failed. Exit from Method | NO |
| IgniteCacheRestartingException | thrown if cache is restarting | Wait on FUTURE. Retry | YES |
| IgniteFutureTimeoutException | occurs when FUTURE computation times out | Increase Timeout Limit or Exit from method  | YES |
| IgniteFutureCancelledException | FUTURE computation can't be gotten because it was cancelled | Retry | YES | 
| IgniteIllegalStateException | Ignite instance current state doesn't support requested task | Task Failed. Exit From Method | YES |
| IgniteNeedReconnectException | node has to reconnect to cluster. | Retry | NO | 
| IgnteDataIntegrity