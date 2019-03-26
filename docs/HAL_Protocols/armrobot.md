## <font color="#A52A2A">Protocols between ArmRobot Robot Agent and Hardware</font>

- [<font color="#FF1493">pick item from buffer</font>](#pick-item-from-buffer)
- [<font color="#FF1493">put item to buffer</font>](#put-item-to-buffer)
- [<font color="#FF1493">pick item from machine</font>](#pick-item-from-machine)
- [<font color="#FF1493">grab item from machine</font>](#grab-item-from-machine)
- [<font color="#FF1493">put item to machine</font>](#put-item-to-machine)
- [<font color="#FF1493">release item to machine</font>](#release-item-to-machine)


#### <font color="#FF1493">pick item from buffer</font>
Agent:
```
{
	"task_no": 1,
	"cmd": "pick_item_buffer",
	"extra": {
		"buffer_no": buffer_no,
		"goodsid": goodsid
	}
}
```
- buffer_no : the number order of buffer
- goodsid : see it [here](../Orders/Goodsid.md)

Machine:
```json5
{
  "task_no": 1,  
  "result": "success", 
  "extra": ""           
}
```

#### <font color="#FF1493">put item to buffer</font>
Agent:
```
{
  "task_no": 2,  
  "cmd": "put_item_buffer",      
  "extra": {
  		"buffer_no": buffer_no,
  		"goodsid": goodsid
  	}
}
```
- buffer_no : the number order of buffer

Machine:
```json5
{
  "task_no": 2,  
  "result": "success", 
  "extra": ""           
}
```

#### <font color="#FF1493">pick item from machine</font>
Agent:
```
{
	"task_no": 2,
	"cmd": "pick_item_machine",
	"extra": {
		"machine_id": machine_id,
		"goodsid": goodsid
		"direction" : "front"
	}
}
```
- machine_id : the same in arm robot configuration file, to identify which machine it is.
- goodsid : see it [here](../Orders/Goodsid.md)
- direction : it can be "front" or "back". 
              This value is only used for lathe now.
              
Machine:
```json5
{
  "task_no": 2,  
  "result": "success", 
  "extra": ""           
}
```

#### <font color="#FF1493">grab item from machine</font>
Agent:
```
{
	"task_no": 3,
	"cmd": "grab_item_machine",
	"extra": {
		"machine_id": machine_id,
		"goodsid": goodsid
	}
}
```
- machine_id : the same in arm robot configuration file, to identify which machine it is.
- goodsid : see it [here](../Orders/definition.md/#goodsid)

Machine:
```json5
{
  "task_no": 3,  
  "result": "success", 
  "extra": ""           
}
```

#### <font color="#FF1493">put item to machine</font>
Agent:
```
{
	"task_no": 4,
	"cmd": "grab_item_machine",
	"extra": {
		"machine_id": machine_id,
		"goodsid": goodsid
		"direction" : "front"
	}
}
```
- machine_id : the same in arm robot configuration file, to identify which machine it is.
- goodsid : see it [here](../Orders/Goodsid.md)
- direction : it can be "front" or "back". 
              This value is only used for lathe now.
              
Machine:
```json5
{
  "task_no": 4,  
  "result": "success", 
  "extra": ""           
}
```

#### <font color="#FF1493">release item to machine</font>
Agent:
```
{
	"task_no": 5,
	"cmd": "grab_item_machine",
	"extra": {
		"machine_id": machine_id,
		"goodsid": goodsid
	}
}
```
- machine_id : the same in arm robot configuration file, to identify which machine it is.
- goodsid : see it [here](../Orders/Goodsid.md)

Machine:
```json5
{
  "task_no": 5,  
  "result": "success", 
  "extra": ""           
}
```
