## <font color="#A52A2A">Socket - Java and C# Communication</font>
Socket Server Address: 127.0.0.1:5656 (default port: 5656)

Sending String to the Socket Server, there are 2 command String. 
Other String will be ignored.

#### <font color="#FF1493">Commands:</font>
1. register type.
```
{
	"type": value_str
}
```
- value_str : value_str can be "machine", "agent", "rfid"

2. send message to right place.
```
{
	"to": dest_str,
	"message": msg			
}
```
- dest_str : dest_str means the place your message will go. 
             It can be "machine", "agent", "rfid".
- msg : msg is the information you want to send.

#### <font color="#FF1493">Examples:</font>
1. register as Agent
```
{
	"type": "agent"
}
```

2. register as Machine
```
{
	"type": "machine"
}
```

3. Machine sends a message to Agent
```
{
	"to": "agent", 
	"message": {"size1":100, "size2":210}  
}
```

#### <font color="#FF1493">Warnning:</font>
1. Make sure your message contains a "\n" at the end. 
   Otherwise, your message wont be accepted until you send a "\n".
1. When your connect to the socket server, the first thing is to register the type. 
   Otherwise, all information will be ignored when the destination is not register yet.
1. Do not disconnect the server until your close your program.
1. All letters are lower case.
1. The message from Socket Server will be end with "\n" . 







