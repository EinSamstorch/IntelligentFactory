package machines.real.arm;

public interface ArmHal {

  /**
   * 搬运工件.
   *
   * @param from 搬运起始地
   * @param to 搬运目的地
   * @param goodsid 工件种类
   * @param step 工序步骤（车床专用）
   * @return 成功true
   */
  boolean moveItem(String from, String to, String goodsid, int step);

  /**
   * 搬运工件.
   *
   * @param from 搬运起始地
   * @param to 搬运目的地
   * @param goodsid 工件种类
   * @return 成功true
   */
  boolean moveItem(String from, String to, String goodsid);
}
