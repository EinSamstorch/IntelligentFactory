package commons.tools;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

/**
 * HTTP请求工具，包含send与post方法.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */
public class HttpRequest {

  private static String API_GETORDER = "http://%s/ks/FactoryAction_getOrder.action?";

  /**
   * 向特定网址发送get请求.
   *
   * @param url 网址
   * @param param 携带参数
   * @return 返回消息
   */
  public static String sendGet(String url, String param) {
    String result = "";
    // 以行为单位接受信息的类
    BufferedReader in = null;
    //try-catch 设置连接,创建连接并读取数据的过程
    try {
      // 连接url和要输出的参数
      String urlNameString = url + param;
      URL realUrl = new URL(urlNameString);
      // 创建和URL之间的连接
      URLConnection connection = realUrl.openConnection();
      // 设置通用的请求属性
      connection.setRequestProperty("accept", "*/*");
      connection.setRequestProperty("connection", "Keep-Alive");
      connection.setRequestProperty("user-agent",
          "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
      // 建立实际的连接
      connection.connect();
      // 获取所有响应头字段
      Map<String, List<String>> map = connection.getHeaderFields();
      // 定义 BufferedReader输入流来读取URL的输入流
      in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
      String line;
      while ((line = in.readLine()) != null) {
        result += line;
      }
    } catch (Exception e) {
      LoggerUtil.agent.error("发送GET请求出现异常！" + e);
      e.printStackTrace();
    } finally {
      try {
        if (in != null) {
          in.close();
        }
      } catch (Exception e2) {
        e2.printStackTrace();
      }
    }
    return result;
  }

  /**
   * 从云端获取新订单.
   *
   * @param host 云端地址
   * @param numTodo 待处理订单数量
   * @param numDoing 正在处理的订单数量
   * @return 网页返回的字符串
   */
  public static String getOrder(String host, int numTodo, int numDoing) {
    return sendGet(String.format(API_GETORDER, host),
        String.format("num_todo=%d&num_doing=%d", numTodo, numDoing));
  }
}
