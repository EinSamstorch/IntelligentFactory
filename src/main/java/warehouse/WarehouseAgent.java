package warehouse;

import commons.AgentTemplate;
import commons.tools.DFServiceType;
import commons.tools.IniLoader;
import warehouse.behaviours.RawContractNetResponder;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import warehouse.hal.WarehouseHal;

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
    private int pos_in;
    private int pos_out;
    private WarehouseHal hal = WarehouseHal.getInstance();

    public String getSqlitePath() {
        return sqlitePath;
    }

    @Override
    protected void setup() {
        super.setup();
        registerDF(DFServiceType.WAREHOUSE);

        ThreadedBehaviourFactory tbf = new ThreadedBehaviourFactory();
        addContractNetResponder(tbf);
    }

    @Override
    protected void loadINI() {
        Map<String, String> setting = IniLoader.load(IniLoader.SECTION_WAREHOUSE);

        String SETTING_SQLITE_PATH = "sqlite_path";
        String SETTING_POS_IN = "pos_in";
        String SETTING_POS_OUT = "pos_out";

        sqlitePath = setting.get(SETTING_SQLITE_PATH);
        pos_in = new Integer(setting.get(SETTING_POS_IN));
        pos_out = new Integer(setting.get(SETTING_POS_OUT));
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
