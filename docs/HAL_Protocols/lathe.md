## <font color="#A52A2A">Protocols between Lathe Agent and Hardware</font>


- [<font color="#FF1493">grab item</font>](#grab-item)
- [<font color="#FF1493">release item</font>](#release-item)
- [<font color="#FF1493">evaluate</font>](#evaluate)
- [<font color="#FF1493">process</font>](#process)
- [<font color="#FF1493">query task</font>](#query-task)


#### <font color="#FF1493">grab item</font>

Agent:
```json5
{
  "task_no": 1,  
  "cmd": "grab_item",      
  "extra": ""    
}
```

Machine:
```json5
{
  "task_no": 1,  
  "result": "success", 
  "extra": ""           
}
```

#### <font color="#FF1493">release item</font>

Agent:
```json5
{
  "task_no": 2,  
  "cmd": "release_item",      
  "extra": ""    
}
```

Machine:
```json5
{
  "task_no": 2,  
  "result": "success", 
  "extra": ""           
}
```

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




