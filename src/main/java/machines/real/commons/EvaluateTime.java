package machines.real.commons;

/**
 * .
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class EvaluateTime {
    private Integer evaluateTime;
    private Integer waitingTime;

    public EvaluateTime(Integer evaluateTime, Integer waitingTime) {
        this.evaluateTime = evaluateTime;
        this.waitingTime = waitingTime;
    }

    public Integer getEvaluateTime() {
        return evaluateTime;
    }

    public Integer getWaitingTime() {
        return waitingTime;
    }
}
