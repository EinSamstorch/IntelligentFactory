package machines.real.commons.behaviours.cycle;

import commons.BaseAgent;
import jade.core.behaviours.TickerBehaviour;
import machines.real.commons.hal.BaseHal;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 检查HAL是否在线，修改agent在线状态.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class CheckHalState<T extends BaseAgent, H extends BaseHal> extends TickerBehaviour {
    private T tAgent;
    private H hal;
    private boolean redisEnable = false;
    private JedisPool pool;

    public void setRedisEnable(boolean redisEnable) {
        this.redisEnable = redisEnable;
    }

    public void setPool(JedisPool pool) {
        this.pool = pool;
    }

    /**
     * Construct a <code>TickerBehaviour</code> that call its
     * <code>onTick()</code> method every <code>period</code> ms.
     *
     * @param a      is the pointer to the agent
     * @param period the tick period in ms
     */
    public CheckHalState(T a, long period, H hal) {
        super(a, period);
        this.tAgent = a;
        this.hal = hal;
    }

    @Override
    protected void onTick() {
        boolean online = false;
        if (hal.checkHalOnline()) {
            online = true;
        }
        tAgent.setAgentOnline(online);
        if(redisEnable) {
            Jedis jedis = pool.getResource();
            jedis.set(tAgent.getLocalName(), online? "online":"offline");
            jedis.close();
        }
    }
}
