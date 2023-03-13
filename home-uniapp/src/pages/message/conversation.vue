<template>
    <view class="container" id="conversation">
        <view class="tip" v-if="tip">
            <view class="tip-container">
                <view class="tip-content">
                    <view class="demend-container" v-if="tip.type === 'demend'">
                        <view class="demend-header">
                            购房需求
                        </view>
                        <view class="demend-content">
                            <view>区域: {{tip.data.region}}</view>
                            <view>价格: {{tip.data.price}}</view>
                            <view>面积: {{tip.data.area}}</view>
                            <view>电话: {{tip.data.phone}}</view>
                            <view>标签: {{tip.data.label === null ? "" : tip.data.label}}</view>
                            <view class="demend-action">
                                <van-button class="action-btn" size="mini" round @tap="sendTip()">发送</van-button>
                            </view>
                        </view>
                        <view class="demend-footer">
                        </view>
                    </view>
                    <view class="evaluation-container" v-if="tip.type === 'evaluation'">
                        <!-- <view class="evaluation-header">
                            测评
                        </view> -->
                        <view class="evaluation-content">
                            <view class="evaluation-img-content">
                                <van-image class="evaluation-img" width="200rpx" height="200rpx" fit="cover" :src="$imageHost + tip.data.mainImage"/>
                            </view>
                            <view class="evaluation-right-content">
                                <view class="evaluation-title">{{tip.data.title}}</view>
                                <view class="evaluation-action">
                                    <van-button class="action-btn" size="mini" round @tap="sendTip()">发送</van-button>
                                </view>
                            </view>
                        </view>
                        <view class="evaluation-footer">
                        </view>
                    </view>
                </view>
            </view>
        </view>
        <scroll-view class="conversation-view" :class="{'showMore': morePanel.show || emojiPanel.show, 'showTip': tip}" scroll-y="true" @scrolltoupper="queryHistory" :scroll-into-view="scrollIntoItem">
            <view class="message-list" v-for="(message, index) in messages" :key="index">
                <view class="message-container">
                    <view class="left-icon">
                        <van-image :class="{'hide': message.sendId === banner.sendId}"
                            round
                            width="50"
                            height="50"
                            fit="cover"
                            :src="banner.receiveAvatar"
                        />
                    </view>
                    <view class="message-card-container">
                        <message-card :msg="message" :right="message.sendId === banner.sendId" :chat="banner"></message-card>
                    </view>
                    <view class="right-icon">
                        <van-image :class="{'hide': message.sendId !==  banner.sendId}"
                            round
                            width="50"
                            height="50"
                            fit="cover"
                            :src="banner.sendAvatar"
                        />
                    </view>
                </view>
            </view>
            <view id="message-bottom"></view>
        </scroll-view>
        <view class="footer" :class="{'showMore': morePanel.show || emojiPanel.show}">
            <view class="input-field-container">
                <view class="input-field">
                    <input v-model="input.value" confirm-type="send" @confirm="sendText()" @focus="closePanel"/>
                </view>
                <view class="button-field">
                    <van-icon class="action-btn" name="smile-o" size="25px" @tap="openEmoji"/>
                    <van-icon class="action-btn" name="add-o" size="25px" @tap="openPanel"/>
                </view>
            </view>
        </view>
        <view class="emoji-panel" v-if="emojiPanel.show">
            <message-emoji @send="addEmoji"></message-emoji>
        </view>
        <view class="more-panel" v-if="morePanel.show">
            <message-more-input @send="sendMore" :cards="chatCards" :chat="banner"></message-more-input>
        </view>
    </view>
</template>

<script>
    import moment from 'moment'
    import apiservice from '../../api/apiservice'

    export default {
        data() {
            return {
                input: {
                    value: ''
                },
                params: {
                    consultantUserId: '',
                    page: 1,
                    size: 10
                },
                id: '',
                banner: {},
                morePanel: {
                    show: false
                },
                emojiPanel: {
                    show: false
                },
                userInfo: null,
                $imageHost: this.$imageHost,
                cameraCtx: null,
                scrollIntoItem: 'message-bottom',
                chatCards: [],
                tip: null
            }
        },
        created() {
            this.userInfo = uni.getStorageSync('sysUserInfo')
        },
        computed: {
            messages() {
                return this.$store.state.message.messages
            }
        },
        onLoad(event) {
            // 目前在某些平台参数会被主动 decode，暂时这样处理。
            try {
                console.log(event.query, event.params)
                this.banner = JSON.parse(decodeURIComponent(event.query));
                this.tip = JSON.parse(decodeURIComponent(event.params))
            } catch (error) {
                this.tip = event.params || null
            }
            if (this.banner) {
                uni.setNavigationBarTitle({
                    title: this.getBannerTitle(this.banner)
                });
                this.id = this.banner.id
                this.params.consultantUserId = this.banner.id
                this.banner.receiveAvatar = this.getImageUrl(this.banner.receiveAvatar)
                this.banner.sendAvatar = this.getImageUrl(this.banner.sendAvatar)
            }
            this.getChatCards();
        },
        onShow() {
        },
        destroyed() {
            this.$store.dispatch('message/quit', this.params)
        },
        methods: {
            queryHistory() {
                if (this.$store.getters['message/showMore']) {
                    this.params.page++
                    this.$store.dispatch('message/chat', this.params)
                }
            },
            onMessageReceive() {
                // 接收消息后 滚动到底部
                this.$store.state.message.socket.on('/sendMsg', res => {
                    if (res.code === 200) {
                        this.pageScrollToBottom()
                    }
                })
            },
            addEmoji(e) {
                console.log(e);
                const emoji = e.detail.__args__[0];
                this.input.value = this.input.value + emoji;
            },
            openEmoji() {
                this.$set(this.emojiPanel, 'show', !this.emojiPanel.show)
                this.$set(this.morePanel, 'show', false)
                this.pageScrollToBottom()
                console.log("oepn emoji")
            },
            openPanel() {
                this.$set(this.morePanel, 'show', !this.morePanel.show)
                this.$set(this.emojiPanel, 'show', false)
                this.pageScrollToBottom()
            },
            closePanel() {
                this.$set(this.morePanel, 'show', false)
                this.$set(this.emojiPanel, 'show', false)
            },
            sendText() {
                const data = {
                    type: 1,
                    content: this.input.value
                }

                this.send(data)
            },
            sendMore(e) {
                const data = e.detail.__args__[0];
                this.send(data)
            },
            send(obj) {
                const data = {
                    sendId: this.banner.sendId,
                    receiveId: this.banner.receiveId,
                    consultantUserId: this.banner.id,
                    content: this.input.value,
                    type: 1,
                    ...obj
                }
                this.$store.dispatch('message/send', data)
                this.$set(this.input, 'value', '')
                this.pageScrollToBottom()
            },
            pageScrollToBottom() {
                this.scrollIntoItem = '';
                setTimeout(() => {
                    this.scrollIntoItem = 'message-bottom'
                }, 200)
            },
            getChatCards() {
                const userInfo = uni.getStorageSync('sysUserInfo')
                const chatCards = uni.getStorageSync('chatCards')
                if (chatCards) {
                    console.log('chatCards', chatCards)
                    this.initChatCards(chatCards)
                } else {
                    apiservice.getChatCard().then(data => {
                        uni.setStorageSync('chatCards', data)
                        this.initChatCards(data)
                    })
                }

            },
            initChatCards(chatCards) {
                const auth = this.userInfo.role
                this.chatCards = chatCards.map(card => {
                    return {
                        ...card,
                        src: this.getImageUrl(card.imagesUrl)
                    }
                })
                console.log('cards', this.chatCards)
            },
            getBannerTitle(banner) {
                const receiveName = this.banner.receiveName
                let consultantType = ''

                if (banner.receiveId === banner.consultantUserId) {
                    if (banner.type && banner.type === 1) {
                        // consultantType = '客户顾问'
                        return 'AI君'
                    } else if (banner.type && banner.type === 2) {
                        consultantType = '置业顾问'
                        if (consultantType) {
                            return `${receiveName}-${consultantType}`
                        }
                    }

                }

                return receiveName
            },
            sendTip() {
                if (this.tip) {
                    if (this.tip.type === 'demend') {
                        const data = {
                            content: JSON.stringify(this.tip.data),
                            type: 8
                        }
                        this.send(data)
                    }
                    if (this.tip.type === 'evaluation') {
                        const data = {
                            content: JSON.stringify(this.tip.data),
                            type: 2
                        }
                        this.send(data)
                    }
                    this.tip = null
                }
            }
        }
    }
</script>

<style lang="scss">
    $panelHeight: 400rpx;

    page {
        background-color: $basic-background-color;
    }
    .container {
        padding: 0;
        font-size: 14px;
        line-height: 24px;
        background-color: $basic-background-color;
        // padding-bottom: 50px;

        .conversation-view {
            height: calc(100vh - 50px);
            width: 750rpx;

            &.showTip {
                height: calc(100vh - 50px - 240rpx);
            }

            &.showMore {
                height: calc(100vh - 50px - 400rpx);

                &.showTip {
                    height: calc(100vh - 50px - 640rpx);
                }
            }
        }

        .hide {
            visibility: hidden;
        }

        .tip {
            // position: absolute;
            top: 0;
            left: 0;
            right: 0;
            width: 100vw;
            display: flex;
            flex-direction: row;
            align-content: center;
            justify-content: center;
            border-radius: 10rpx;

            .tip-container {
                width: calc(100% - 200rpx);
                height: 200rpx;
                background: #fff;
                border-radius: 10rpx;
                margin-top: 15rpx;
                margin-bottom: 15rpx;
                overflow: hidden;

                .demend-container {
                    display: flex;
                    flex-direction: column;
                    font-size: 20rpx;

                    .demend-header {
                        padding-left: 20rpx;
                        padding-right: 20rpx;
                        border-bottom: 1rpx solid #efefef;
                    }

                    .demend-content {
                        display: flex;
                        flex-direction: row;
                        flex-wrap: wrap;
                        padding-left: 20rpx;
                        padding-right: 20rpx;

                        view {
                            flex: 0 0 50%
                        }

                        .demend-action {
                            display: flex;
                            flex-direction: row-reverse;

                            .action-btn {
                                .van-button {
                                    color: #fff;
                                    background: $basic-color;
                                }
                            }
                        }
                    }

                    .demend-footer {
                        padding-left: 20rpx;
                        padding-right: 20rpx;
                        border-top: 1rpx solid #efefef;
                    }
                }

                .evaluation-container {
                    display: flex;
                    flex-direction: column;
                    font-size: 20rpx;

                    .evaluation-header {
                        padding-left: 20rpx;
                        padding-right: 20rpx;
                        border-bottom: 1rpx solid #efefef;
                    }

                    .evaluation-content {
                        display: flex;
                        flex-direction: row;
                        flex-wrap: wrap;
                        // padding-left: 20rpx;
                        // padding-right: 20rpx;


                        .evaluation-right-content {
                            display: flex;
                            flex-direction: column;
                            width: calc(100% - 200rpx);

                            .evaluation-title {
                                padding: 15rpx;
                                word-wrap: break-all;
                                height: calc(100% - 100rpx);
                            }

                            .evaluation-action {
                                display: flex;
                                flex-direction: row-reverse;
                                padding-right: 15rpx;

                                .action-btn {
                                    .van-button {
                                        color: #fff;
                                        background: $basic-color;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        .message-list {
            display: flex;
            flex-direction: row;
            flex-wrap: nowrap;
            align-items: center;
            justify-content: center;
        }

        .message-container {
            display: flex;
            flex-direction: row;
            flex-wrap: nowrap;
            align-items: flex-start;
            justify-content: space-between;
            margin-top: 15rpx;
            margin-bottom: 15rpx;
            width: 710rpx;
        }

        .message-card-container {
            flex-grow: 1;
        }

        .footer.showMore {
            bottom: $panelHeight;
            padding-bottom: 0;
        }

        .footer {
            position: fixed;
            bottom: 0;
            left: 0;
            width: 100%;
            height: 100rpx;
            padding-bottom: env(safe-area-inset-bottom);

            .input-field-container {
                display: flex;
                flex-direction: row;
                flex-wrap: nowrap;
                justify-content: space-between;
                align-items: center;
                width: 100%;
                height: 100%;
                background-color: #efefef;
                border-top: 1px solid #ccc;

                .input-field {
                    flex-grow: 1;
                    margin: 15rpx;
                    padding: 15rpx;
                    border-radius: 10rpx;
                    background-color: #fff;
                }

                .button-field {
                    flex-grow: 0;

                    .action-btn {
                        margin-top: 15rpx;
                        margin-right: 15rpx;
                    }
                }
            }
        }

        .more-panel, .emoji-panel {
            position: fixed;
            bottom: 0;
            left: 0;
            width: 100%;
            height: $panelHeight;
        }
    }

</style>
