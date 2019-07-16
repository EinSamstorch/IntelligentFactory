package machines.real.mill;

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
import machines.real.mill.behaviours.cycle.LoadItemBehaviour;
import machines.real.mill.behaviours.cycle.MaintainBufferBehaviour;
import machines.real.mill.behaviours.cycle.MillContractNetResponder;

import java.util.Map;

/**
 * .
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class MillAgent extends AgentTemplate {
    private Buffer[] buffers;
    private MillHal hal;
    private Boolean busy = false;
    private String armPwd;

    @Override
    protected void setup() {
        super.setup();
        registerDF(DFServiceType.MILL);

        hal = new MillHal(halPort);

        ThreadedBehaviourFactory tbf = new ThreadedBehaviourFactory();
        addContractNetResponder(tbf);

        Behaviour b = new LoadItemBehaviour(this);
        addBehaviour(tbf.wrap(b));

        b = new MaintainBufferBehaviour(this);
        addBehaviour(tbf.wrap(b));
    }

    @Override
    protected void loadINI() {
        Map<String, String> setting = IniLoader.load(IniLoader.SECTION_LATHE);

        String SETTING_BUFFER_SIZE = "buffer_size";
        String SETTING_BUFFER_INDEX = "buffer_index";

        int bufferSize = Integer.parseInt(setting.get(SETTING_BUFFER_SIZE));
        String bufferStr = setting.get(SETTING_BUFFER_INDEX);
        buffers = new Buffer[bufferSize];
        String[] split = bufferStr.split(",");
        if(split.length != buffers.length) {
            throw new IllegalArgumentException("Buffer size mismatch.");
        }
        for(int i = 0; i < buffers.length; i++) {
            buffers[i] = new Buffer(
                    Integer.parseInt(
                            split[i].trim()
                    )
            );
        }

        setting = IniLoader.load(IniLoader.SECTION_COMMON);

        final String SETTING_ARM_PASSWORD = "arm_password";
        armPwd = setting.get(SETTING_ARM_PASSWORD);
    }


    /**
     * 检查buffer是否已满
     * @return true 已满, false 未满
     */
    public boolean isBufferFull() {
        for (Buffer buffer1 : buffers) {
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
        for (int i = 0; i < buffers.length; i++) {
            if(buffers[i].getWpInfo() == null) {
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
        buffers[index].setWpInfo(wpInfo);
    }

    public Buffer getBufferByIndex(int index) {
        return buffers[index];
    }

    public MillHal getHal() {
        return hal;
    }

    public Buffer[] getBuffers() {
        return buffers;
    }

    private void addContractNetResponder(ThreadedBehaviourFactory tbf) {

        MessageTemplate mt = MessageTemplate.and(
                MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET),
                MessageTemplate.MatchPerformative(ACLMessage.CFP)
        );
        Behaviour b = new MillContractNetResponder(this, mt);
        addBehaviour(tbf.wrap(b));
    }

    public Boolean isBusy() {
        return busy;
    }

    public void setBusy(Boolean busy) {
        this.busy = busy;
    }

    public String getArmPwd() {
        return armPwd;
    }
}
