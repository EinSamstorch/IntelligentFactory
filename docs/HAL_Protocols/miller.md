## <font color="#A52A2A">Protocols between Miller Agent and Hardware</font>



- [<font color="#FF1493">evaluate</font>](#evaluate)
- [<font color="#FF1493">process</font>](#process)
- [<font color="#FF1493">query task</font>](#query-task)



#### <font color="#FF1493">evaluate</font>

Agent:
```
{
  "task_no": 3,  
  "cmd": "evaluate",      
  "extra": workpiece_info
}
```
- workpiece_info : A json string contains detail information of workpiece.
                   See it [here](../Orders/definition.md/#workpiece-info)
                   
Machine:
```json5
{
  "task_no": 3,  
  "result": "success", 
  "extra": ""           
}
```

#### <font color="#FF1493">process</font>

Agent:
```
{
  "task_no": 4,  
  "cmd": "process",      
  "extra": workpiece_info
}
```
- workpiece_info : A json string contains detail information of workpiece.
                   See it [here](../Orders/definition.md/#workpiece-info)

Machine:
```json5
{
  "task_no": 4,  
  "result": "success", 
  "extra": ""           
}
```

#### <font color="#FF1493">query task</font>
For evaluate command.


Machine:
```
{
  "task_no": 5,  
  "result": "success", 
  "extra": {
    "state": task_state,
    "extra": evaluate_time
  }           
}
```
- evaluate_time: estimate processing time




