package machines.real.commons;

/**
 * 设置agent上下线.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public interface IMachineOnline {
    /**
     * 获取agent在线状态
     *
     * @return true在线, false离线
     */
    boolean isAgentOnline();

    /**
     * 设置agent上下线,如果状态变更, 相应DF进行注册与注销
     *
     * @param agentOnline
     */
    void setAgentOnline(boolean agentOnline);
}
