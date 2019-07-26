# Intelligent Factory

## How to deploy

1. Install JRE8.
1. Download [SocketServer.jar](https://github.com/junfengP/SocketServer/releases) or compile it yourself.
1. Download [IntelligentFactory.jar](https://github.com/junfengP/IntelligentFactory) or compile it yourself.
1. mkdir `{projectRoot}`
1. copy `{SourceCode}\resources` and `SocketServer.jar` `IntelligentFactory.jar` to `{projectRoot}\`
1. modify `{projectRoot}\resources\config\setting.ini` see it in section [Config settings](#Config-settings)
1. run `jade host` see it in section [How to run jade host](#How-to-run-jade-host)
1. run `java -jar SocketServer.jar` see it in section [SocketServer Usage](https://github.com/junfengP/SocketServer)
1. run `machine executor`
1. run `agent` see it in section [How to run agent](#How-to-run-agent)

## How to run jade host
```
java -cp IntelligentFactory.jar jade.Boot -gui -host {HOST_IP} -jade_domain_df_autocleanup true
```
**`HOST_IP` is a very important variable in this system, be careful! 
It is a constant in this system. 
Do not modify it once you set up the whole system.**

## Config settings
In `{projectRoot}\resources\config\setting.ini`, there are a lot of sections.
Each section is named by a specific agent's name. 
Such as `x1` is mill 1's agent local name.


But there are several kinds of agents. 
Mainly two, the real agents and virtual agents.

The virtual agents include `cloud` and `worker`.

For `cloud`, the `website` indicate the web server address, and mysql configs is disabled in current.
```
[cloud]
website = 120.26.53.90:8080
mysql_user = root
mysql_pwd = endlessloop
mysql_db = smartfactory
mysql_ip = localhost
mysql_port = 3306
```

For `worker`, `detect_ratio` range in [0, 100], it is the change of checking a product in `vision`.
```
[worker]
detect_ratio = 100
```

The real agents include `warehouse`, `armrobot`, `agv`, `lathe`, `mill`, `vision` in current system.
For `agv`, `hal_port` is the socket server listening port. It should be the same in `SocketServer` and `machine executor`.
The default value is `5656` if this value is not set.
**This variable is used in all real agents.**
``` 
hal_port = 5656
```

For `warehouse`, `sqlite_path` indicate a database table which store information of workpiece in warehouse. 
`pos_in` and `pos_out` are position of import and export location in warehouse.  
```
[ck]
sqlite_path = resources/db/warehouse.db
pos_in = 1
pos_out = 15
``` 

For `armrobot`, `arm_password` is a string that indicate a password for accessing the armrobot agent.
```
[arm1]
arm_password = xi
```

For `vision`, `lathe`, `mill`. the `buffer_index` indicates buffer's No. which belong to the machine.
`arm_password` is password for accessing specific arm robot.
```
[c1]
buffer_index = 9, 10, 11, 12
arm_password = che
hal_port=5656
```

## How to run agent

**Attention:**
make sure `SocketServer` is already started. And the `hal_port` is set in `setting.ini`, If you are going to run a real agent.

```
java -cp IntelligentFactory.jar jade.Boot -container -host {HOST_IP} {AGENT_LOCALNAME}:{AGENT_CLASSPATH}

// for example
java -cp IntelligentFactory.jar jade.Boot -container -host 192.168.79.16 worker:machines.virtual.worker.WorkerAgent
```

## Agent's Behaviours

see it in [agent](docs/agent.md)

## HAL Protocols

see it in [protocols](docs/HAL_Protocols/protocols.md)

## Order's Definition

see it in [definition](docs/Orders/Definition.md)