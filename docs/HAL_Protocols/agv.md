# Protocols between AGV Agent and Hardware

- [move](#move)

## move

Agent:
```
{
  "task_no": 3,  
  "cmd": "move",      
  "extra": {
      "from": from,
      "to": to
  }    
}
```
- from : Integer: A point defined in factory machine location map.
- to : Integer : A point defined in factory machine location map.




