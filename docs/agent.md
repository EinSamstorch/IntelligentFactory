# Agent's Behaviour Instruction

## Cloud Agent
Cloud agent has three cyclic behaviours.
1. `GetOrder`, this behaviour runs about every 10 seconds to fetch orders' information from web server. 
And stores order information in `agent's order queue`.
1. `DetectUpdateMsg`, this behaviour is currently disabled.
1. `HandleOrders`, this behaviour pull orders from `agent's order queue` and split workpieces from one order. 
And then send workpiece information to `worker agent` to call for propose.

## Worker Agent
Worker agent has two cyclic behaviours.
1. `HandleRequest`, this behaviour receives `ACLMessage.REQUEST` and 
`FIPANames.InteractionProtocol.FIPA_REQUEST`, message content is `WorkpieceInfo`.
And then put the `WorkpieceInfo` into `agent's WorkpieceInfo queue`.
1. `HandleWorkpiece`, this behaviour pull `WorkpieceInfo` from `agent's WorkpieceInfo queue`.
And process cfp due to `WorkpieceInfo's Process Plan`.

## AGV Agent
AGV agent has two cyclic behaviours.
1. `RecvTransportRequestBehaviour`, this behaviour receives `ACLMessage.REQUEST` and 
`FIPANames.InteractionProtocol.FIPA_REQUEST`, message content is `AgvRequest`.
And then put the `ACLMessage` into `agent's request queue`.
1. `TransportRequestBehaviour`, this behaviour pulls `ACLMessage requestMsg` from `agent's request queue`.
And then execute the request.

## Arm Robot Agent
Arm robot agent has two cyclic behaviours.
1. `RecvMoveItemBehaviour`, this behaviour receives `ACLMessage.REQUEST` and 
`FIPANames.InteractionProtocol.FIPA_REQUEST`, message content is `AgvRequest`.
And then put the `ACLMessage` into `agent's request queue`.
1. `MoveItemBehaviour`, this behaviour pulls `ACLMessage requestMsg` from `agent's request queue`.
And then execute the request.

## Lathe Agent
Lathe agent has three cyclic behaviours.
1. `ProcessContractNetResponder`, this behaviour handles cfp from `worker agent`. The offer content is `ContractNetContent`.
The offer price is `time length` from current to this workpiece processed.
1. `MaintainBufferBehaviour`, this behaviours receives `ACLMessage.INFORM` and
`MatchLanguage("BUFFER_INDEX)`, message content is `String(BUFFER_INDEX)`.
When receives this message, it means the workpiece in relating buffer has already removed. 
Then the lathe agent will reset the buffer.
1. `LoadItemBehaviour`, this behaviour will choose a workpiece from machine's buffers to process.

## Mill Agent
Mill agent is similar to `Lathe Agent`.

## Vision Agent
Vision agent is similar to `Lathe Agent`.

## Warehouse Agent
Warehouse agent has four cyclic behaviours.
1. `RawContractNetResponder`, this behaviour handles cfp from `worker agent`.
The offer content is `ContractNetContent`.
The offer price is `Quantity of raw material`. 
1. `ProductContractNetResponder`, this behaviour handles cfp from `worker agent`.
The offer content is `ContractNetContent`.
The offer price is `Quantity of empty position`.
1. `RecvItemExportRequestBehaviour`, this behaviour receives `ACLMessage.REQUEST` and 
`FIPANames.InteractionProtocol.FIPA_REQUEST`, message content is `WarehouseRequest`.
And then put the `ACLMessage` into `agent's request queue`.
1. `ItemExportBehaviour`, this behaviour pulls `ACLMessage requestMsg` from `agent's request queue`.
And then execute the request.