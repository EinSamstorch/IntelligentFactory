### Protocols for Hardware Adapt Layer

Using socket to communication.

#### Commands
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
            You will see it in the details of each type.in section [details](#details).
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
  - For RFID, it can be the return value from read RFID action.
  - For failure, it can be the reason why it fails.


#### Details
This section contains each type of machines. It tells the cmd_str of each machine.
And shows some examples to run the action.
1. [Warehouse](./warehouse.md)
2. [ArmRobot](./armrobot.md)