package machines.real.warehouse;

import commons.BaseAgent;
import commons.tools.DfServiceType;
import commons.tools.IniLoader;
import commons.tools.db.DbInterface;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import machines.real.warehouse.behaviours.cycle.ItemExportBehaviour;
import machines.real.warehouse.behaviours.cycle.ProductContractNetResponder;
import machines.real.warehouse.behaviours.cycle.RawContractNetResponder;
import machines.real.warehouse.behaviours.cycle.RecvItemExportRequestBehaviour;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

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
    private Integer posIn;
    private Integer posOut;
    private WarehouseHalInterface hal;
    private Queue<ACLMessage> exportQueue = new LinkedBlockingQueue<>();

    private Behaviour[] behaviours;

    public void setBehaviours(Behaviour[] behaviours) {
        this.behaviours = behaviours;
    }

    public WarehouseHalInterface getHal() {
        return hal;
    }

    public Integer getPosIn() {
        return posIn;
    }

    public void setPosIn(Integer posIn) {
        this.posIn = posIn;
    }

    public Integer getPosOut() {
        return posOut;
    }

    public void setPosOut(Integer posOut) {
        this.posOut = posOut;
    }

    public Queue<ACLMessage> getExportQueue() {
        return exportQueue;
    }

    @Override
    protected void setup() {
        super.setup();
        registerDf(DfServiceType.WAREHOUSE);

        ApplicationContext ac2 = new FileSystemXmlApplicationContext("./resources/warehouse.xml");
        hal = ac2.getBean("hal", WarehouseHalInterface.class);

        ThreadedBehaviourFactory tbf = new ThreadedBehaviourFactory();
        for (Behaviour behaviour : behaviours) {
            addBehaviour(tbf.wrap(behaviour));
        }
    }
}
