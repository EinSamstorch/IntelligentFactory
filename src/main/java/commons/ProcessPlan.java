package commons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 预定的工艺路线.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class ProcessPlan {
    private Map<String, List<String>> processPlan = new HashMap<String, List<String>>(){
        {
            List<String > l = new ArrayList<String>(){
                {
                    add(MachineType.WAREHOUSE);
                    add(MachineType.LATHE);
                }
            };
            processPlan.put("002", l);
        }
    };

    public Map<String, List<String>> getProcessPlan() {
        return processPlan;
    }
}
