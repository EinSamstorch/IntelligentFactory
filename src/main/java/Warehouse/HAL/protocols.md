### Protocols between Warehouse Agent and Hardware

Using socket to communication.

#### Commands
The basic framework of this protocols.

Agent Sends:
```
{
  "task_no": task_no,  // This identifies a task
  "cmd": cmd_str,      // cmd_str should be one of followings: "move_item", "import_item", 
                       // "export_item", "read_rfid", "write_rfid", "query_task"
  "extra": cmd_info    // cmd_info is extra parameters for cmd
}
```

Machine Responses:
```
{
  "task_no": task_no,  // The same as Agent gives
  "result": rst_str,   // rst_str tells the action result. It should be "success" if everything is ok.
  "extra": extra_info  // extra_info is that the action returns some info to Agent
                       // for RFID, it can be the return value from read RFID action.
                       // for failure, it can be the reason why it fails.
}
```

##### 1.Move item
Agent:
```
{
  "task_no": 1,  
  "cmd": "move_item",      
  "extra": {
                "from": from_port, // where the item exists now
                "to": to_port      // where the item will go
            }    
}
```

Machine:
```
{
  "task_no": 1,  
  "result": "success",   
  "extra": ""           
}
```

##### 2. Control conveyor

Agent:
```
{
  "task_no": 2,  
  "cmd": "import_item",  // or "export_item"    
  "extra": "" 
}
```


Machine:
```
{
  "task_no": 2,  
  "result": "success",   
  "extra": ""           
}
```


##### 3. Control RFID
###### 3.1 read RFID
Agent:
```
{
  "task_no": 3,  
  "cmd": "read_rfid",  
  "extra": "" 
}
```

Machine:
```
{
  "task_no": 3,  
  "result": "success",   
  "extra": rfid_info_str  // the info read from rfid chips           
}
```

###### 3.2 write RFID
Agent:
```
{
  "task_no": 4,  
  "cmd": "write_rfid",  
  "extra": info_str     // the info you wanna write into rfid chips 
}
```

Machine:
```
{
  "task_no": 4,  
  "result": "success",   
  "extra":  ""   
}
```

#### 4 query task
Agent:
```
{
  "task_no": 5,  
  "cmd": "query_task",  
  "extra": query_task_no  // which you want to query if this task is done or failed. 
}
```

Machine:
```
{
  "task_no": 5,  
  "result": "success",   
  "extra":  "done"       // "done" if the task is finished, "processing" if the task is still doing,
                         // "failed" if the task is failed.   
}
```
