# Protocols between Vision Agent and Hardware



- [check](#check)




## check

Agent:
```
{
  "task_no": 3,  
  "cmd": "check",      
  "extra": {
    "goodsid": goodsid,
  }
}
```
- goodsid : type of goods. see it [here](./definition.md/#goodsid)
- size_name : waiting for check. see it [here](./definition.md/#detail-size)
- size : design size compared with real size.
                   
Machine:
```json5
{
  "task_no": 3,  
  "result": "success", 
  "extra": ""           
}
```

After checking:
```
{
  "task_no": 3,  
  "result": "success", 
  "extra": value           
}
```
- value : JSON String : all sizes of workpiece.