package machines.real.lathe;

public interface LatheHal {
    /**
     * 夹取工件
     * @return 成功true 失败false
     */
    boolean grabItem();

    /**
     * 松开工件
     * @return 成功true, 失败false
     */
    boolean releaseItem();
}
