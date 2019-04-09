# Protocols between Agent and RFID

- [read rfid](#read-rfid)
- [write rfid](#write-rfid)

## read rfid
Agent:
```json5
{
  "task_no": 1,  
  "cmd": "read_rfid",  
  "extra": {
    "rfid_no": 1
  } 
}
```

- rfid_no : Integer : the number order of rfid in this machine. 

RFID:
```json5
{
  "task_no": 1,  
  "result": "success",   
  "extra": ""       
}
``` 

After reading action is done. 
RFID Sends:
```
{
  "task_no": 1,  
  "result": "success",   
  "extra": info_str       
}
```
- info_str : String : read from rfid chips.

## write rfid
Agent:
```
{
  "task_no": 2,  
  "cmd": "write_rfid",  
  "extra": {
    "rfid_no": 1,
    "info": info_str
  }  
}
```
- rfid_no : Integer : the number order of rfid in this machine.
- info_str : the info you wanna write into rfid chips 
