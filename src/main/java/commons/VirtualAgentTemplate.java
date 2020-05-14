package commons;

/**
 * 无Hal的Agent模板. 与带hal的agent模板的差别是： 带hal的模板，一般会配备checkHalOnline行为，改行为控制agent注册自身服务
 * 无hal的模板，需要通过初始化时，手动进行注册服务
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */
public class VirtualAgentTemplate extends RealAgentTemplate {

  @Override
  protected void setup() {
    super.setup();
    registerDf(serviceType);
  }
}
