## <font color="#A52A2A">Protocols between Vision Agent and Hardware</font>



- [<font color="#FF1493">check</font>](#check)
- [<font color="#FF1493">query task</font>](#query-task)



#### <font color="#FF1493">check</font>

Agent:
```
{
  "task_no": 3,  
  "cmd": "check",      
  "extra": {
    "goodsid": goodsid,
    "size_name": size_name,
    "size": size
  }
}
```
- goodsid : type of goods. see it [here](../Orders/definition.md/#goodsid)
- size_name : waiting for check. see it [here](../Orders/definition.md/#detail-size)
- size : design size compared with real size.
                   
Machine:
```json5
{
  "task_no": 3,  
  "result": "success", 
  "extra": ""           
}
```

#### <font color="#FF1493">query task</font>

Agent:
```
{
  "task_no": 4,  
  "cmd": "query_task",      
  "extra": 3
}
```
                   
Machine:
```
{
  "task_no": 4,  
  "result": "success", 
  "extra":  {
      "state": task_state,
      "extra": extra_info
    }           
}
```
- extra_info : if task_state = "done", then "qualified" or "unaccepted".
