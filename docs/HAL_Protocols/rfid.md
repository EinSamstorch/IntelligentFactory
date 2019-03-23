## <font color="#A52A2A">Protocols between Agent and RFID</font>

- [<font color="#FF1493">read rfid</font>](#read-rfid)
- [<font color="#FF1493">write rfid</font>](#write-rfid)
- [<font color="#FF1493">query task</font>](#query-task)

#### <font color="#FF1493">read rfid</font>
Agent:
```json5
{
  "task_no": 1,  
  "cmd": "read_rfid",  
  "extra": "" 
}
```

RFID:
```json5
{
  "task_no": 1,  
  "result": "success",   
  "extra": ""       
}
``` 

#### <font color="#FF1493">write rfid</font>
Agent:
```
{
  "task_no": 2,  
  "cmd": "write_rfid",  
  "extra": info_str  
}
```
- info_str : the info you wanna write into rfid chips 

RFID:
```json5
{
  "task_no": 2,  
  "result": "success",   
  "extra":  ""   
}
```

#### <font color="#FF1493">query task</font>
For read rfid commands.

RFID:
```
{
  "task_no": 3,  
  "result": "success",   
  "extra":  {
    "state": task_state,
    "extra": extra_info
  }       
}
```
- extra_info : the info read from rfid chips  