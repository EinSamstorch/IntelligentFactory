# Protocols between AGV Agent and Hardware


- [import item](#import-item)
- [export item](#export-item)
- [move](#move)



## import item

Agent:
```json5
{
  "task_no": 1,  
  "cmd": "import_item",      
  "extra": ""    
}
```

## export item

Agent:
```json5
{
  "task_no": 2,  
  "cmd": "export_item",      
  "extra": ""    
}
```


## move

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






