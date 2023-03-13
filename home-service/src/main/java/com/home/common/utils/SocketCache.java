package com.home.common.utils;

import com.corundumstudio.socketio.SocketIOClient;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author xiewei
 * @date 2020/4/26 10:44
 */
public class SocketCache {

    /** 聊天地址 */
    public static final String SEND_MSG_PATH = "/sendMsg";
    /** 历史消息地址 */
    public static final String HISTORICAL_NEWS_PATH = "/historicalNews";
    /** 修改消息状态地址 */
    public static final String UPDATE_MSG_STATUS_PATH = "/updateMsgStatus";

    /** 已连接用户 */
    public static ConcurrentMap<UUID, SocketIOClient> socketClient = new ConcurrentHashMap<>();
    /** 在线用户列表 */
    public static ConcurrentMap<Integer, UUID> socketUser = new ConcurrentHashMap<>();

    /** 清空连接用户 */
    public static void clear() {
        socketClient.clear();
        socketUser.clear();
    }

    /** 断开连接 */
    public static void disconnect(UUID uuid) {
        for (Map.Entry<Integer, UUID> entry : socketUser.entrySet()) {
            if(uuid.equals(entry.getValue())){
                socketUser.remove(entry.getKey());
            }
        }
        socketClient.remove(uuid);
    }
}
