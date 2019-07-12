package machines.real.lathe;

import commons.AgentTemplate;
import commons.Buffer;
import commons.WorkpieceInfo;
import commons.tools.DFServiceType;
import commons.tools.IniLoader;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import machines.real.lathe.behaviours.cycle.LatheContractNetResponder;

import java.util.Map;

/**
 * 车床Agent.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */


public class LatheAgent extends AgentTemplate {
    private Buffer[] buffer;
    private LatheHal hal = new LatheHal();

    public LatheHal getHal() {
        return hal;
    }

    @Override
    protected void setup() {
        super.setup();
        registerDF(DFServiceType.LATHE);

        // 独立线程启动车削服务应标行为
        ThreadedBehaviourFactory tbf = new ThreadedBehaviourFactory();
        addContractNetResponder(tbf);

    }

    @Override
    protected void loadINI() {
        // to do
        Map<String, String> setting = IniLoader.load(IniLoader.SECTION_LATHE);

        String SETTING_BUFFER_SIZE = "buffer_size";
        String SETTING_BUFFER_INDEX = "buffer_index";

        int bufferSize = Integer.parseInt(setting.get(SETTING_BUFFER_SIZE));
        String bufferStr = setting.get(SETTING_BUFFER_INDEX);
        buffer = new Buffer[bufferSize];
        String[] split = bufferStr.split(",");
        if(split.length != buffer.length) {
            throw new IllegalArgumentException("Buffer size mismatch.");
        }
        for(int i = 0; i < buffer.length; i++) {
            buffer[i] = new Buffer(
                    Integer.parseInt(
                            split[i].trim()
                    )
            );
        }
    }

    /**
     * 检查buffer是否已满
     * @return true 已满, false 未满
     */
    public boolean isBufferFull() {
        for (Buffer buffer1 : buffer) {
            if(buffer1.getWpInfo() == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获得一个空的buffer位置
     * @return 该机床buffer的下标, from 0 to bufferSize - 1
     */
    public int getEmptyBuffer() {
        if(isBufferFull()) {
            return -1;
        }
        for (int i = 0; i < buffer.length; i++) {
            if(buffer[i].getWpInfo() == null) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 向buffer中放入物品
     * @param index buffer下标
     * @param wpInfo 物品
     */
    public void putBuffer(int index, WorkpieceInfo wpInfo) {
        buffer[index].setWpInfo(wpInfo);
    }

    public Buffer getBufferByIndex(int index) {
        return buffer[index];
    }


    private void addContractNetResponder(ThreadedBehaviourFactory tbf) {

        MessageTemplate mt = MessageTemplate.and(
                MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET),
                MessageTemplate.MatchPerformative(ACLMessage.CFP)
        );
        Behaviour b = new LatheContractNetResponder(this, mt);
        addBehaviour(tbf.wrap(b));
    }

}
