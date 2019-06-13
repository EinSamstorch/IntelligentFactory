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

    private static List<String> goodsidList = new ArrayList<>();
    private static Map<String, List<String>> processPlan = new HashMap<>();

    static {
        // 当前支持的goodsid
        goodsidList.add("001");    // 法兰
        goodsidList.add("002");    // 轴
        goodsidList.add("003");    // 板

        List<String> flangePP = new ArrayList<String>(){
            {
                add(MachineType.WAREHOUSE);
                add(MachineType.LATHE);
                add(MachineType.MILLER);
            }
        };
        processPlan.put("001", flangePP);

        List<String> axisPP = new ArrayList<String>(){
            {
                add(MachineType.WAREHOUSE);
                add(MachineType.LATHE);
            }
        };
        processPlan.put("002", axisPP);

        List<String> plainPP = new ArrayList<String>(){
            {
                add(MachineType.WAREHOUSE);
                add(MachineType.MILLER);
            }
        };
        processPlan.put("003", plainPP);
    }

    public static List<String> getProcessPlan(String goodsid) {
        return processPlan.get(goodsid);
    }
}
