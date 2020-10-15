package machines.real.agv.behaviours.cycle;

import commons.tools.LoggerUtil;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import machines.real.agv.algorithm.AgvMapUtils;

/**
 * 心跳过期检查.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */
public class HeartBeatChecker extends TickerBehaviour {

  private Map<AID, Long> checkInMap;
  private long heartBeatTimeOut;

  public void setCheckInMap(Map<AID, Long> checkInMap) {
    this.checkInMap = checkInMap;
  }

  public HeartBeatChecker(Agent a, long period) {
    super(a, period);
    heartBeatTimeOut = period;
  }

  @Override
  protected void onTick() {
    long timestamp = System.currentTimeMillis();
    Iterator<Entry<AID, Long>> it = checkInMap.entrySet().iterator();
    while (it.hasNext()) {
      Entry<AID, Long> next = it.next();
      if (timestamp - next.getValue() > heartBeatTimeOut) {
        AID lost = next.getKey();
        LoggerUtil.agent.info(String.format("Heartbeat timeout: %s", next.getKey().getLocalName()));
        it.remove();
        AgvMapUtils.setAgvLoc(lost, null);
      }
    }
  }
}
