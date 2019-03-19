package CommonTools.MySocket.Server;

import CommonTools.LoggerUtil;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 服务器端的侦听类，负责获取连接成功的客户端.
 * 默认连接端口5656
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */


public class GetSocket extends Thread {
    private int listenPort;

    public GetSocket() {
        listenPort = 5656;
    }

    public GetSocket(int listenPort) {
        this.listenPort = listenPort;
    }

    @Override
    public void run() {
        try {
            //创建绑定到特定端口的服务器套接字  1-65535
            ServerSocket serversocket = new ServerSocket(listenPort);
            LoggerUtil.commonTools.info("Socket Start Listening " + listenPort);
            while (true) {
                //建立连接，获取socket对象
                Socket socket = serversocket.accept();

                //与客户端通信
                ChatSocket cs = new ChatSocket(socket);
                cs.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
