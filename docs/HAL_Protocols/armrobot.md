# Protocols between ArmRobot Robot Agent and Hardware


- [move item](#move-item)

## move item
Agent:
```
{
	"task_no": 1,
	"cmd": "move_item",
	"extra": {
		"from": from,
		"to": to,
		"goodsid": goodsid
		"step" : step
	}
}
```
- from : String :  where the item stays now.
- to : String : where the item will be put.
> For buffers, they would be a string number, such as "1", "12".

> For machines, they would be the machine id, such as "c1", "x1".
- goodsid : String : see it [here](../Orders/Goodsid.md)
- step : Integer : **Lathe Only** : the step of cooperation with lathe.
 

