package commons.order;

import commons.tools.DfServiceType;
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
    // 法兰
    goodsidList.add("001");
    // 轴
    goodsidList.add("002");
    // 板
    goodsidList.add("003");

    List<String> flangeProcessPlan = new ArrayList<String>() {
      {
        add(DfServiceType.RAW);
        add(DfServiceType.MILL);
        add(DfServiceType.LATHE);
        add(DfServiceType.CURVE);
      }
    };
    processPlan.put(goodsidList.get(0), flangeProcessPlan);

    List<String> axisProcessPlan = new ArrayList<String>() {
      {
        add(DfServiceType.RAW);
        add(DfServiceType.LATHE);
        add(DfServiceType.MILL);
      }
    };
    processPlan.put(goodsidList.get(1), axisProcessPlan);

    List<String> plainProcessPlan = new ArrayList<String>() {
      {
        add(DfServiceType.RAW);
        add(DfServiceType.CURVE);
        add(DfServiceType.MILL);
      }
    };
    processPlan.put(goodsidList.get(2), plainProcessPlan);
  }

  public static List<String> getProcessPlan(String goodsid) {
    return processPlan.get(goodsid);
  }
}
