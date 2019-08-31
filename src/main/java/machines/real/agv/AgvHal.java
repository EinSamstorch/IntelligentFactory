package machines.real.agv;

public interface AgvHal {
    /**
     * 运输工件
     *
     * @param from 起始工位台
     * @param to   目的工位台
     * @return 成功true
     */
    boolean move(int from, int to);
}
