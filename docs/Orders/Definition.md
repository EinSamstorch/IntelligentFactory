## <font color="#A52A2A">Definition of Workpiece</font>


#### <font color="#FF1493">Workpiece Info</font>

|   Attribute   |   Format    |    Example     |          Explain           |
| :-----------: | :---------: | :------------: | :------------------------: |
|  detail_size  | JSON String | Depend on type |                            |
| pre_ownner_id |   String    |      che1      |      who processed it      |
| cur_ownner_id |   String    |      xi2       |    who will process  it    |
|   last_agv    |   String    |      agv1      |      who delivered it      |
|  buffer_pos   |   Integer   |       1        | current position in buffer |


#### <font color="#FF1493">Deliver Request</font>
|   Attribute    |   Format    |              Example              |            Explain             |
| :------------: | :---------: | :-------------------------------: | :----------------------------: |
| workpiece_info | JSON String | [see definition](#workpiece-info) |                                |
|    from_pos    |   Integer   |                25                 | Defined in Factory Machine Map |
|     to_pos     |   Integer   |                 1                 | Defined in Factory Machine Map |

#### <font color="#FF1493">GoodsID</font>
| goodsid | description | extra |
|:--------|:------------|:------|
| 001     | flange      |       |
| 002     | axis        |       |
| 003     | plate       |       |