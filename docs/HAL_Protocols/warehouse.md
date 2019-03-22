### Protocols between Warehouse Agent and Hardware

Using socket to communication.

#### Commands

Warehouse Contains Following commands:
- [move_item](#1move-item)
- [import_item](#2control-conveyor)
- [export_item](#2control-conveyor)
- [read_rfid](#31-read-rfid)
- [write_rfid](#32-write-rfid)
- [query_task](#4query-task)

##### 1.Move item 
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

##### 2.Control conveyor

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


##### 3.Control RFID
###### 3.1 Read RFID
Agent:
```json5
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
  "extra": rfid_info_str       
}
```
- rfid_info_str : the info read from rfid chips   

###### 3.2 Write RFID
Agent:
```
{
  "task_no": 4,  
  "cmd": "write_rfid",  
  "extra": info_str  
}
```
- info_str : the info you wanna write into rfid chips 

Machine:
```json5
{
  "task_no": 4,  
  "result": "success",   
  "extra":  ""   
}
```

#### 4.Query task
Agent:
```
{
  "task_no": 5,  
  "cmd": "query_task",  
  "extra": query_task_no 
}
```
- query_task_no : which you want to query if this task is done or failed. 

Machine:
```
{
  "task_no": 5,  
  "result": "success",   
  "extra":  task_state       
}
```
- task_state :
  - "done" if the task is finished, 
  - "processing" if the task is still doing,
  - "failed" if the task is failed.   
