package machines.real.mill.behaviours.cycle;

import commons.Buffer;
import commons.tools.LoggerUtil;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import machines.real.mill.MillAgent;

/**
 * 维护buffer状态
 * 取走工件后, 清空buffer
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class MaintainBufferBehaviour extends CyclicBehaviour {
    private MillAgent magent;
    private Buffer[] buffers;

    public MaintainBufferBehaviour(MillAgent magent) {
        super(magent);
        this.magent = magent;
        this.buffers = magent.getBuffers();
    }

    @Override
    public void action() {
        MessageTemplate mt = MessageTemplate.and(
                MessageTemplate.MatchPerformative(ACLMessage.INFORM),
                MessageTemplate.MatchLanguage("BUFFER_INDEX")
        );
        ACLMessage receive = magent.receive(mt);
        if(receive != null) {
            int bufferIndex = new Integer(receive.getContent());
            for (Buffer buffer : buffers) {
                if(buffer.getIndex() == bufferIndex) {
                    LoggerUtil.hal.info(String.format("Item removed from buffer %d", bufferIndex));
                    buffer.reset();
                }
            }
        } else {
            block();
        }
    }
}
