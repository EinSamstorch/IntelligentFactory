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

After checking:
```
{
  "task_no": 3,  
  "result": "success", 
  "extra": value           
}
```
- value : String : not defined yet.