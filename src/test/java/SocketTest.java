import CommonTools.MySocket.Client.SocketClient;
import CommonTools.MySocket.Server.GetSocket;
import org.junit.Test;

import java.net.SocketTimeoutException;

import static junit.framework.TestCase.assertTrue;

/**
 * 测试SocketServer.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */


public class SocketTest {

    @Test
    public void testSocket() {
        new GetSocket().start();
        SocketClient agent = new SocketClient(SocketClient.AGENT);
        agent.start();
        String cmd = "this is msg";
        String result = null;
        try {
            result = agent.sendCmd("agent", cmd);
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
        }
        System.out.println(result);
        assertTrue(result.equals(cmd));
    }
}


