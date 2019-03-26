## <font color="#A52A2A">Protocols for Hardware Adapt Layer</font>

Using socket to communication.

#### <font color="#FF1493">Framework of Commands</font>
The basic framework of this protocols.

Agent Sends:
```
{
  "task_no": task_no, 
  "cmd": cmd_str,      
  "extra": cmd_info    
}
```
- task_no : This identifies a task
- cmd_str : cmd_str depends on the specific type of machines.
            You will see it in the details of each type.in section [commands](#commands).
- cmd_info : cmd_info is extra parameters for cmd. 
             It depends on specific cmd_str. (Default:"")


Machine Responses:
```
{
  "task_no": task_no,  
  "result": rst_str,
  "extra": extra_info         
}
```
- task_no : The same as Agent gives
- rst_str : rst_str tells the action result. 
            It should be "success" if everything is ok.
- extra_info : extra_info is that the action returns some info to Agent. 
               For failure, it can be the reason why it fails.



#### <font color="#FF1493">Commands</font>
##### <font color="#FFD700">1.Common Commands</font>
###### <font color="#90EE90">query task</font>
Agent:
```
{
  "task_no": 1,  
  "cmd": "query_task",  
  "extra": query_task_no 
}
```
- query_task_no : which you want to query if this task is done or failed. 

Machine:
```
{
  "task_no": 1,  
  "result": "success",   
  "extra":  {
    "state": task_state,
    "extra": extra_info
  }       
}
```
- task_state :
  - "done" if the task is finished, 
  - "processing" if the task is still doing,
  - "failed" if the task is failed.   

- extra_info :
  - if task_state = "done", extra_info can be the return value from the task,
  - if task_state = "failed", extra_info can ben the reason why it failed.
  - or "" for other case.

##### <font color="#FFD700">2.Detail Commands</font>
This section contains each type of machines. It tells the cmd_str of each machine.
And shows some examples to run the action.
1. [Warehouse](./warehouse.md)
1. [ArmRobot](./armrobot.md)
1. [RFID](./rfid.md)
1. [AGV](./agv.md)
1. [Lathe](./lathe.md)
1. [Miller](./miller.md)
1. [Vision Detector](./vision.md)