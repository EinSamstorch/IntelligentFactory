package machines.real.warehouse;

import commons.BaseAgent;
import commons.tools.DfServiceType;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;


/**
 * 仓库Agent类.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */


public class WarehouseAgent extends BaseAgent {
    private WarehouseHal hal;
    private Behaviour[] behaviours;
    private Integer posIn;
    private Integer posOut;

    public void setBehaviours(Behaviour[] behaviours) {
        this.behaviours = behaviours;
    }

    public WarehouseHal getHal() {
        return hal;
    }

    public void setHal(WarehouseHal hal) {
        this.hal = hal;
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


    @Override
    protected void setup() {
        super.setup();
        registerDf(DfServiceType.WAREHOUSE);

        ThreadedBehaviourFactory tbf = new ThreadedBehaviourFactory();
        for (Behaviour behaviour : behaviours) {
            addBehaviour(tbf.wrap(behaviour));
        }
    }
}
