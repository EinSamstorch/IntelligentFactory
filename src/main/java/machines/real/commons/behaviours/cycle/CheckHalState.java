package machines.real.commons.behaviours.cycle;

import commons.BaseAgent;
import jade.core.behaviours.TickerBehaviour;
import machines.real.commons.hal.BaseHalImpl;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 检查HAL是否在线，修改agent在线状态.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class CheckHalState<T extends BaseAgent, H extends BaseHalImpl> extends TickerBehaviour {

  private T baseAgent;
  private H hal;
  private boolean redisEnable = false;
  private JedisPool pool;

  /**
   * Construct a <code>TickerBehaviour</code> that call its
   * <code>onTick()</code> method every <code>period</code> ms.
   *
   * @param a is the pointer to the agent
   * @param period the tick period in ms
   */
  public CheckHalState(T a, long period, H hal) {
    super(a, period);
    this.baseAgent = a;
    this.hal = hal;
  }

  public void setRedisEnable(boolean redisEnable) {
    this.redisEnable = redisEnable;
  }

  public void setPool(JedisPool pool) {
    this.pool = pool;
  }

  @Override
  protected void onTick() {
    boolean online = false;
    if (hal.checkHalOnline()) {
      online = true;
    }
    baseAgent.setAgentOnline(online);
    if (redisEnable) {
      Jedis jedis = pool.getResource();
      jedis.set(baseAgent.getLocalName(), online ? "online" : "offline");
      jedis.close();
    }
  }
}
