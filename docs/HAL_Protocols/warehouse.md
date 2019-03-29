# <font color="#A52A2A">Protocols between Warehouse Agent and Hardware</font>

- [move item](#move-item)
- [control conveyor](#control-conveyor)



## move item

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
- from : Integer : where the item exists now
- to : Integer : where the item will go

## control conveyor

Agent:
```json5
{
  "task_no": 2,  
  "cmd": "import_item",  // or "export_item"    
  "extra": "" 
}
```
