package com.home.config;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import com.corundumstudio.socketio.store.pubsub.PubSubStore;
import com.home.modules.app.utils.JwtUtils;
import com.home.modules.sys.entity.SysUserTokenEntity;
import com.home.modules.sys.service.SysUserTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author xiewei
 * @date 2020/4/26 10:44
 */
@Slf4j
@Configuration
public class SocketIoConfig {

    @Resource
    private JwtUtils jwtUtils;

    @Resource
    private SysUserTokenService tokenService;

    @Value("${socketio.port}")
    private Integer port;

    @Value("${socketio.workCount}")
    private int workCount;

    @Value("${socketio.allowCustomRequests}")
    private boolean allowCustomRequests;

    @Value("${socketio.upgradeTimeout}")
    private int upgradeTimeout;

    @Value("${socketio.pingTimeout}")
    private int pingTimeout;

    @Value("${socketio.pingInterval}")
    private int pingInterval;

    @Value("${socketio.maxFramePayloadLength}")
    private int maxFramePayloadLength;

    @Value("${socketio.maxHttpContentLength}")
    private int maxHttpContentLength;

    /**
     * netty-socketio服务器
     */
    @Bean
    public SocketIOServer socketIoServer() {
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        // 配置端口
        config.setPort(port);
        // 开启Socket端口复用
        com.corundumstudio.socketio.SocketConfig socketConfig = new com.corundumstudio.socketio.SocketConfig();
        socketConfig.setReuseAddress(true);
        config.setSocketConfig(socketConfig);
        // 连接数大小
        config.setWorkerThreads(workCount);
        // 允许客户请求
        config.setAllowCustomRequests(allowCustomRequests);
        // 协议升级超时时间(毫秒)，默认10秒，HTTP握手升级为ws协议超时时间
        config.setUpgradeTimeout(upgradeTimeout);
        // Ping消息超时时间(毫秒)，默认60秒，这个时间间隔内没有接收到心跳消息就会发送超时事件
        config.setPingTimeout(pingTimeout);
        // Ping消息间隔(毫秒)，默认25秒。客户端向服务器发送一条心跳消息间隔
        config.setPingInterval(pingInterval);
        // 设置HTTP交互最大内容长度
        config.setMaxHttpContentLength(maxHttpContentLength);
        // 设置最大每帧处理数据的长度，防止他人利用大数据来攻击服务器
        config.setMaxFramePayloadLength(maxFramePayloadLength);
        // 启动随机session
        config.setRandomSession(true);
        // 握手协议参数使用JWT的Token认证方案 认证方案
        config.setAuthorizationListener(data -> {
            // http://localhost:8081?token=test
            // 例如果使用上面的链接进行connect，可以使用如下代码获取token
            // 如果认证不通过会返回一个Socket.EVENT_CONNECT_ERROR事件
            String token = data.getSingleUrlParam("token");
            if (token != null && token.length() == 32) {
                SysUserTokenEntity tokenEntity = tokenService.queryByToken(token);
                //token失效
                return tokenEntity != null && tokenEntity.getExpireTime().getTime() >= System.currentTimeMillis();
            }
            return jwtUtils.verify(token);
        });
        return new SocketIOServer(config);
    }

    /**
     * 用于扫描netty-socketio的注解，比如 @OnConnect、@OnEvent
     */
    @Bean
    public SpringAnnotationScanner springAnnotationScanner(SocketIOServer socketServer) {
        return new SpringAnnotationScanner(socketServer);
    }

    @Bean
    public PubSubStore pubSubStore(SocketIOServer socketServer) {
        return socketServer.getConfiguration().getStoreFactory().pubSubStore();
    }

}
