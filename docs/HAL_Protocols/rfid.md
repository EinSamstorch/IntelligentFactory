## <font color="#A52A2A">Protocols between Agent and RFID</font>

- [<font color="#FF1493">read rfid</font>](#read-rfid)
- [<font color="#FF1493">write rfid</font>](#write-rfid)

#### <font color="#FF1493">read rfid</font>
Agent:
```json5
{
  "task_no": 1,  
  "cmd": "read_rfid",  
  "extra": "" 
}
```

Machine:
```
{
  "task_no": 1,  
  "result": "success",   
  "extra": rfid_info_str       
}
```
- rfid_info_str : the info read from rfid chips   

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

Machine:
```json5
{
  "task_no": 2,  
  "result": "success",   
  "extra":  ""   
}
```
