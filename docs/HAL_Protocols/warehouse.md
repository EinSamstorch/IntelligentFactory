## <font color="#A52A2A">Protocols between Warehouse Agent and Hardware</font>


- [<font color="#FF1493">move_item</font>](#move_item)
- [<font color="#FF1493">control conveyor</font>](#control-conveyor)



#### <font color="#FF1493">move_item</font>

Agent:
```json5
{
  "task_no": 1,  
  "cmd": "move_item",      
  "extra": {
                "from": 1,
                "to": 15 
            }    
}
```
- from : where the item exists now
- to : where the item will go

Machine:
```json5
{
  "task_no": 1,  
  "result": "success", 
  "extra": ""           
}
```

#### <font color="#FF1493">control conveyor</font>

Agent:
```json5
{
  "task_no": 2,  
  "cmd": "import_item",  // or "export_item"    
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


