package machines.real.warehouse;

import commons.BaseAgent;
import commons.tools.DfServiceType;
import commons.tools.IniLoader;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import machines.real.warehouse.behaviours.cycle.ItemExportBehaviour;
import machines.real.warehouse.behaviours.cycle.ProductContractNetResponder;
import machines.real.warehouse.behaviours.cycle.RawContractNetResponder;
import machines.real.warehouse.behaviours.cycle.RecvItemExportRequestBehaviour;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * 仓库Agent类.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */


public class WarehouseAgent extends BaseAgent {
    private String sqlitePath;
    private Integer posIn;
    private Integer posOut;
    private WarehouseHal hal;
    private Queue<ACLMessage> exportQueue = new LinkedBlockingQueue<>();
    private WarehouseSqlite sqlite;

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

    public WarehouseSqlite getSqlite() {
        return sqlite;
    }

    @Override
    protected void setup() {
        super.setup();
        loadIni();
        registerDf(DfServiceType.WAREHOUSE);

        hal = new WarehouseHal(halPort);
        sqlite = new WarehouseSqlite(getSqlitePath());
        sqlite.initTable();

        ThreadedBehaviourFactory tbf = new ThreadedBehaviourFactory();
        addContractNetResponder(tbf);

        Behaviour b = new RecvItemExportRequestBehaviour(this);
        addBehaviour(tbf.wrap(b));

        b = new ItemExportBehaviour(this);
        addBehaviour(tbf.wrap(b));
    }


    protected void loadIni() {
        Map<String, String> setting = IniLoader.load(getLocalName());

        final String settingSqlitePath = "sqlite_path";
        final String settingPosIn = "pos_in";
        final String settingPosOut = "pos_out";

        halPort = IniLoader.loadHalPort(getLocalName());
        sqlitePath = setting.get(settingSqlitePath);
        posIn = new Integer(setting.get(settingPosIn));
        posOut = new Integer(setting.get(settingPosOut));
    }

    private void addContractNetResponder(ThreadedBehaviourFactory tbf) {
        MessageTemplate mt = MessageTemplate.and(
                MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET),
                MessageTemplate.MatchPerformative(ACLMessage.CFP)
        );
        MessageTemplate mt1 = MessageTemplate.and(mt, MessageTemplate.MatchLanguage("RAW"));
        Behaviour b = new RawContractNetResponder(this, mt1);
        addBehaviour(tbf.wrap(b));

        MessageTemplate mt2 = MessageTemplate.and(mt, MessageTemplate.MatchLanguage("PRODUCT"));
        b = new ProductContractNetResponder(this, mt2);
        addBehaviour(tbf.wrap(b));
    }

    public Queue<ACLMessage> getExportQueue() {
        return exportQueue;
    }
}
