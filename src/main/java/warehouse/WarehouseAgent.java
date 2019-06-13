package warehouse;

import commons.AgentTemplate;
import commons.tools.DFServiceType;
import commons.tools.IniLoader;
import warehouse.behaviours.cycle.HalBehaviour;
import warehouse.behaviours.cycle.RawContractNetResponder;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.Map;


/**
 * 仓库Agent类.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */


public class WarehouseAgent extends AgentTemplate {
    private String sqlitePath;
    private int posIn;
    private int posOut;
    private WarehouseHal hal = new WarehouseHal();

    public WarehouseHal getHal() {
        return hal;
    }

    public String getSqlitePath() {
        return sqlitePath;
    }

    public int getPosIn() {
        return posIn;
    }

    public int getPosOut() {
        return posOut;
    }

    @Override
    protected void setup() {
        super.setup();
        registerDF(DFServiceType.WAREHOUSE);

        ThreadedBehaviourFactory tbf = new ThreadedBehaviourFactory();
        addContractNetResponder(tbf);


        Behaviour b = new HalBehaviour(this);
        addBehaviour(tbf.wrap(b));
    }

    @Override
    protected void loadINI() {
        Map<String, String> setting = IniLoader.load(IniLoader.SECTION_WAREHOUSE);

        String SETTING_SQLITE_PATH = "sqlite_path";
        String SETTING_POS_IN = "pos_in";
        String SETTING_POS_OUT = "pos_out";

        sqlitePath = setting.get(SETTING_SQLITE_PATH);
        posIn = new Integer(setting.get(SETTING_POS_IN));
        posOut = new Integer(setting.get(SETTING_POS_OUT));
    }

    private void addContractNetResponder(ThreadedBehaviourFactory tbf) {

        MessageTemplate mt = MessageTemplate.and(
                MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET),
                MessageTemplate.MatchPerformative(ACLMessage.CFP)
        );
        Behaviour b = new RawContractNetResponder(this, mt);
        addBehaviour(tbf.wrap(b));
    }
}
