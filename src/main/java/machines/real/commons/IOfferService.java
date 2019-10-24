package machines.real.commons;

import java.util.Properties;

/**
 * 定义了Agent提供服务的接口
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */
public interface IOfferService {
    /**
     * 注册服务
     * @param serviceType 服务类型
     * @param props       额外属性
     */
    void registerDf(String serviceType, Properties props);
    /**
     * 注册服务
     *
     * @param serviceType 服务类型
     * @param password    服务密码
     */
    void registerDf(String serviceType, String password);

    /**
     * 注册服务
     *
     * @param serviceType 服务类型
     */
    void registerDf(String serviceType);

    /**
     * 注销服务
     */
    void deregisterDf();
}
