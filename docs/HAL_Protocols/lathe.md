# Protocols between Lathe Agent and Hardware

- [grab item](#grab-item)
- [release item](#release-item)
- [evaluate](#evaluate)
- [process](#process)


## grab item

Agent:
```json5
{
  "task_no": 1,  
  "cmd": "grab_item",      
  "extra": ""    
}
```


## release item

Agent:
```json5
{
  "task_no": 2,  
  "cmd": "release_item",      
  "extra": ""    
}
```


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



