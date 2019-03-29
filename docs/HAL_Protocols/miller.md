# Protocols between Miller Agent and Hardware

- [evaluate](#evaluate)
- [process](#process)



## evaluate

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

After evaluation.
{
  "task_no": 3,  
  "result": "success", 
  "extra": value           
}
- value : String :depends on algorithm. Currently, the estimate time of processing the workpiece.

## process

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



