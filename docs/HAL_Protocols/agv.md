## <font color="#A52A2A">Protocols between AGV Agent and Hardware</font>


- [<font color="#FF1493">import item</font>](#import-item)
- [<font color="#FF1493">export item</font>](#export-item)
- [<font color="#FF1493">move</font>](#move)



#### <font color="#FF1493">import item</font>

Agent:
```json5
{
  "task_no": 1,  
  "cmd": "import_item",      
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

#### <font color="#FF1493">export item</font>

Agent:
```json5
{
  "task_no": 2,  
  "cmd": "export_item",      
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

#### <font color="#FF1493">move</font>

Agent:
```
{
  "task_no": 3,  
  "cmd": "move",      
  "extra": {
      "destination": map_location
  }    
}
```
- map_location : A point defined in factory machine location map.

Machine:
```json5
{
  "task_no": 3,  
  "result": "success", 
  "extra": ""           
}
```




