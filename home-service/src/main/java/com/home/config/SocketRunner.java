package com.home.config;

import com.corundumstudio.socketio.HandshakeData;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.home.common.utils.JsonResult;
import com.home.common.utils.JwtUtil;
import com.home.common.utils.SocketCache;
import com.home.modules.sys.entity.SysUserEntity;
import com.home.modules.sys.entity.SysUserTokenEntity;
import com.home.modules.sys.service.SysUserTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author xiewei
 * @date 2020/4/26 10:44
 */
@Component
@Slf4j
public class SocketRunner implements CommandLineRunner {

    /**
     * socketIOServer
     */
    private final SocketIOServer socketIoServer;

    private final SysUserTokenService tokenService;

    @Autowired
    public SocketRunner(SocketIOServer socketIoServer, SysUserTokenService tokenService) {
        this.socketIoServer = socketIoServer;
        this.tokenService = tokenService;

    }

    @Override
    public void run(String... args) {
        log.info("---------- NettySocket通知服务开始启动 ----------");
        // 监听连接成功事件
        socketIoServer.addConnectListener(this::onConnect);
        // 监听连接断开事件
        socketIoServer.addDisconnectListener(this::onDisconnect);
        socketIoServer.start();
        log.info("---------- NettySocket通知服务启动成功 ----------");
    }

    /**
     * 连接成功
     */
    private void onConnect(SocketIOClient client) {
        SocketCache.socketClient.put(client.getSessionId(), client);
        HandshakeData data = client.getHandshakeData();
        String token = data.getSingleUrlParam("token");
        Integer userId;
        if (token != null && token.length() == 32) {
            SysUserEntity sysUserEntity = tokenService.getByToken(token);
            userId = sysUserEntity.getAppUserId();
        } else {
            userId = JwtUtil.getUserId(token);
        }
        SocketCache.socketUser.put(userId, client.getSessionId());
        client.sendEvent("connect", JsonResult.success());
        log.info("客户端：{},连接成功，当前连接人数：{}", client.getSessionId(),SocketCache.socketClient.size());
    }

    /**
     * 连接断开
     */
    private void onDisconnect(SocketIOClient client) {
        SocketCache.disconnect(client.getSessionId());
        log.info("客户端：{},断开连接，剩余连接人数：{}", client.getSessionId(),SocketCache.socketClient.size());
    }

}
