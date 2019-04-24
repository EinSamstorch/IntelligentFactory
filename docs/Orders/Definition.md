## <font color="#A52A2A">Definition of Workpiece</font>


#### <font color="#FF1493">Workpiece Info</font>

|   Attribute   |   Format    |    Example     |          Explain           |
| :-----------: | :---------: | :------------: | :------------------------: |
| orderId       |   String    |      001       |      id of orders          |
| workpieceId   |   String    |      001       |      id of workpiece       |
|   goodsid     |   String    |      001       |  see [Definition](#goodsid)|
|  detailSize  | JSON String | Depend on type |        see it [here]()     |
| providerId |   AID    |            |      who offered raw material      |
| preOwnerId |   AID    |            |      who processed it      |
| curOwnerId |   AID    |             |    who will process  it    |
|   last_agv    |   AID    |            |      who delivered it      |
|  buffer_pos   |   Integer   |       1        | current position in buffer |


#### <font color="#FF1493">Deliver Request</font>
|   Attribute    |   Format    |              Example              |            Explain             |
| :------------: | :---------: | :-------------------------------: | :----------------------------: |
| workpiece_info | JSON String | see [definition](#workpiece-info) |                                |
|    from_pos    |   Integer   |                25                 | Defined in Factory Machine Map |
|     to_pos     |   Integer   |                 1                 | Defined in Factory Machine Map |

#### <font color="#FF1493">GoodsID</font>
| goodsid | description | extra |
|:--------|:------------|:------|
| 001     | flange      |       |
| 002     | axis        |       |
| 003     | plate       |       |

#### <font color="#FF1493">Detail Size</font>
