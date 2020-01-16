# Common Configuration Options

| System Property |  Description | Default | 
| --- | --- | --- |
| IGNITE_LOG_INSTANCE_NAME | If present, ignite includes its instance name in the log message | Not Set |
| IGNITE_QUIET | toggles verbose mode | True (non-verbose) |
| IGNITE_LOG_DIR | sets directory for Ignite Logs | ${IGNITE_HOME}/work/log |
| IGNITE_DUMP_THREADS_ON_FAILURE | enables output thread dumps to logs when critical error is encountered | true |

## DEFAULTS
- Java Logger
    - java.util.logger.Logger (JUL)
    - can be reconfigured in runtime via LoggingMXBean
- Log Configuration
    - $IGNITE_HOME/config/java.util.logging.properties
- Log Directory
    - ${IGNITE_HOME}/work/log
        - usually /tmp/ignite/work/log
    - overriden by IGNITE_LOG_DIR System Property

## JUL-TO-SL4J Bridge
- You must pay close attention to the way the log level is configured when using this bridge
    - org.apache may be set to DEBUG, but final logger is set to INFO (EX)
    - the reason this matters is that Ignite will spend 10x as much overhead generating logs that are shitcanned 
    once they cross the JUL-TO-SL4J bridge. 
    
    
## Log4J, Log4J2, JCL
We don't use this... so I don't care. 

## SLF4J (Simple Logging Framework for Java) 



