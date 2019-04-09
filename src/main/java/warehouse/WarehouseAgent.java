package warehouse;

import commons.AgentTemplate;
import commons.tools.DFServiceType;
import commons.tools.IniLoader;
import warehouse.behaviours.RawCNResponder;
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
    private int pos_in;
    private int pos_out;

    public String getSqlitePath() {
        return sqlitePath;
    }

    @Override
    protected void setup() {
        super.setup();
        registerDF(DFServiceType.WAREHOUSE);

        ThreadedBehaviourFactory tbf = new ThreadedBehaviourFactory();
        addCNResponder(tbf);
    }

    @Override
    protected void loadINI() {
        Map<String, String> setting = IniLoader.load(IniLoader.SECTION_WAREHOUSE);
        sqlitePath = setting.get("sqlite_path");
        pos_in = new Integer(setting.get("pos_in"));
        pos_out = new Integer(setting.get("pos_out"));
    }

    protected void addCNResponder(ThreadedBehaviourFactory tbf) {

        MessageTemplate mt = MessageTemplate.and(
                MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET),
                MessageTemplate.MatchPerformative(ACLMessage.CFP)
        );
        Behaviour b = new RawCNResponder(this, mt);
        addBehaviour(tbf.wrap(b));

    }
}
