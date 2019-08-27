package machines.real.warehouse;

import commons.order.WorkpieceStatus;

public interface DbInterface {
    /**
     * 获得一个原料, 同时从raw table中删去库存
     *
     * @param goodsid 原料种类
     * @return int positon,  0表示失败
     */
    int getRaw(String goodsid);

    /**
     * 查询raw table, 对应 goodsid的原料剩余数量
     *
     * @param goodsid 代查询种类
     * @return 剩余原料数量, 不存在的原料 返回数量0
     */
    int getRawQuantityByGoodsId(String goodsid);

    /**
     * 获得一个空位 同时将数据信息更新到表里
     *
     * @return 空位id
     */
    int getProduct(WorkpieceStatus wpInfo);

    /**
     * 查询 product表 空位数量
     *
     * @return 空位数量
     */
    int getProductQuantity();
}
