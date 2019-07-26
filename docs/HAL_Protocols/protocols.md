# Protocols for Hardware Adapt Layer

Using socket to communication.

## Framework of Commands
The basic framework of this protocols.

*Agent Sends*:
```
{
  "task_no": task_no, 
  "cmd": cmd_str,      
  "extra": cmd_info    
}
```
- task_no : Integer : This identifies a task
- cmd_str : String : cmd_str depends on the specific type of machines.
            You will see it in the details of each type in section [commands](#commands).
- cmd_info : String : cmd_info is extra parameters for cmd. 
             It depends on specific cmd_str. (Default:"")


*Machine Immediately Response*:
```
{
  "task_no": task_no,  
  "result": rst_str,
  "extra": extra_info         
}
```
- task_no : Integer : The same as Agent gives
- rst_str : String : rst_str tells the action result. 
            It should be "success" if everything is ok.
            Or "failed" if the command is unknown.
- extra_info : String : For failure, it can be the reason why it fails.
               For rst_str = "success", it should be "" . 

*After the Task is Done or Failed*:
```
{
  "task_no": task_no,  
  "result": task_result,
  "extra": extra_info         
}
```
- task_no : Integer : The same as Agent gives
- task_result : String
  - "success" if the task is finished, 
  - "failed" if the task is failed. 

- extra_info : String
  - if task_state = "success", extra_info can be the return value from the task,
  - if task_state = "failed", extra_info can ben the reason why it failed.
  - or "" for other case. 

## Commands

This section contains each type of machines. It tells the cmd_str of each machine.
And shows some examples to run the action.
1. [Warehouse](./warehouse.md)
2. [ArmRobot](./armrobot.md)
3. [RFID](./rfid.md)
4. [AGV](./agv.md)
5. [Lathe](./lathe.md)
6. [Miller](mill.md)
7. [Vision Detector](./vision.md)