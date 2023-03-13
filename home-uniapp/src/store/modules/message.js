import io from '../../common/weapp.socket.io'

// initial state
const state = () => ({
    socket: null,
    messages: [],
    eventList: [],
    unreads: [],
    socketOpen: false,
    total: 0,
    page: 1,
    size: 10,
    id: null //当前聊天室id
})

// getters
const getters = {
    showMore: (state) => {
        return state.messages.length < state.total;
    },
    unreads: (state) => (id) => {
        if (id) {
            return state.unreads.filter(unreads => {
                return unreads.consultantUserId === id
            }).length;
        }

        return state.unreads.length
    }
}

// actions
const actions = {
    init({ commit }, url) {
        commit('WEBSOCKET_INIT', url);

    },
    //发送消息
    send({ commit }, data) {
        commit('WEBSOCKET_SEND', data)
    },
    //进入聊天
    chat({ commit }, data) {
        commit('WEBSOCKET_CHAT_ENTER', data)
        commit('WEBSOCKET_HISTORY', data)
        commit('WEBSOCKET_UNREAD_CLEAR', data)
    },
    //退出聊天
    quit({ commit }, data) {
        console.log('quit chat')
        commit('WEBSOCKET_CHAT_QUIT', data)
    }
}

// mutations
const mutations = {
    //申明并连接websocket，
    //在mutation也能定义websocket的一些方法，但是在vuex严格模式下，异步处理数据浏览器会抛出错误。所以只是申明了websocket
    WEBSOCKET_INIT(state, url) {
        console.log('开始连接websocket')
        const token = uni.getStorageSync('token')
        const socketUrl = `${url}?token=${token}`

        state.socket = io(socketUrl, { transports: ['websocket'] })

        state.socket.on('/sendMsg', res => {
            console.log('io on /sendMsg: ', res, state)
            if (res.code === 200) {
                if (res.data.consultantUserId === state.id) {
                    state.messages = state.messages.concat(res.data);
                } else {
                    state.unreads = state.unreads.concat(res.data);
                }
            }
        })

        state.socket.on('connect', res => {
            console.log('connect success', res)
        })

        state.socket.on('reconnect_failed', res => {
            console.log('reconnect failed', res)
        })

        state.socket.on('connect_error', res => {
            console.log('on error', res)
        })

        console.log(state.socket)
    },
    //发送
    WEBSOCKET_SEND(state, data) {
        state.socket.emit('/sendMsg', data, (res) => {
            if (res.code === 200 && res.data) {
                state.messages = state.messages.concat(res.data);
            }
        })
    },
    //历史
    WEBSOCKET_HISTORY(state, data) {
        console.log('/historycalNews', data)
        state.socket.emit('/historicalNews', data, (res) => {
            if (res.code === 200 && res.data) {
                if (res.data.pageNum === 1) {
                    state.messages = res.data.list;
                    state.total = res.data.total
                    state.page = res.data.pageNum
                } else if (res.data.pageNum > 1) {
                    state.messages = [...res.data.list, ...state.messages];
                    state.total = res.data.total
                    state.page = res.data.pageNum
                }
            }
        })
    },
    WEBSOCKET_CHAT_ENTER(state, data) {
        state.id = data.consultantUserId
    },
    WEBSOCKET_CHAT_QUIT(state, data) {
        state.socket.emit('/updateMsgStatus', data, (res) => {
            console.log(res)
        })
        state.id = ''
    },
    WEBSOCKET_UNREAD_CLEAR(state, data) {
        state.unreads = state.unreads.filter(msg => {
            return msg.consultantUserId !== data.consultantUserId
        })
    },
    WEBSOCKET_UNREAD_CLEAR_ALL(state, data) {
        state.unreads = []
    }
}

export default {
    namespaced: true,
    state,
    getters,
    actions,
    mutations
}


// const IOWSConnect = (state, url, token) => {
//     const socketUrl = `${url}?token=${token}`
//     state.socket = io(socketUrl, { transports: ['websocket'] })

//     state.socket.on('/sendMsg', res => {
//         console.log('io on /sendMsg: ', res)
//     })

//     state.socket.on('connect', res => {
//         console.log('connect success', res)
//     })
// }
