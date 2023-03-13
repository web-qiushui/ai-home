<template>
    <view class="container">
        <view class="search-bar-container">
            <view class="search-bar">
                <van-search
                    v-model="keywords"
                    shape="round"
                    @change="onSearch"
                    @onSearch="onSearch"
                    @cancel="onCancel"
                    placeholder="搜索"
                    use-left-icon-slot="true"
                >
                    <van-icon slot="left-icon" class="search-bar-icon" name="search"/>
                </van-search>
            </view>
        </view>
        <view class="converation-list">
            <view class="converation-item" v-for="(item, index) in searchList"
                :key="index"
                @tap="gotoConversation(item)">

                <view class="user-avatar">
                    <van-image
                        round
                        width="50"
                        height="50"
                        fit="cover"
                        :src="item.receiveAvatar"
                    />
                </view>
                <view class="msg-container">
                    <view class="msg-detail-container">
                        <view class="sender-info">
                            <view class="sender-name">{{item.receiveName}}</view>
                            <view class="send-time">{{timeFormat(item.createTime)}}</view>
                        </view>
                        <view class="msg-preview-content">
                            <view class="msg-content">{{convert(item)}}</view>
                            <view class="msg-unread">
                                <view v-show="getUnreadNum(item.id)" class="msg-unread-circle">{{getUnreadNum(item.id)}}</view>
                            </view>
                        </view>
                    </view>
                </view>
            </view>
        </view>
    </view>
</template>

<script>
    import apiservice from '../../api/apiservice'
    import moment from 'moment'

    export default {
        data() {
            return {
                keywords: '',
                $imageHost: this.$imageHost,
                list: [],
                loading: false,
                total: 0
            }
        },
        onShow() {
            this.setTabBarIndex(2)
            this.getBuddyList();
        },
        created() {
        },
        computed: {
            unreads() {
                return this.$store.state.message.unreads;
            },
            searchList() {
                return this.filterList(this.list, this.keywords)
            }
        },
        onPullDownRefresh() {
            this.list = []
            this.getBuddyList();
        },
        methods: {
            filterList(list, keywords) {
                console.log('filter list', list, keywords)
                const that = this
                if (!keywords) {
                    return list;
                }
                return list.filter(item => {
                    if (item && item.receiveName) {
                        console.log('match', item.receiveName, keywords)
                        return !!item.receiveName.match(keywords);
                    } 
                    return false;
                })
            },
            onSearch(e) {
                this.keywords = e.detail;
            },
            onCancel() {
                this.keywords = ''
            },
            getBuddyList() {
                console.log(apiservice)
                apiservice.getBuddyList().then(data => {
                    this.list = data.map(item => {
                        return {
                            ...item,
                            receiveAvatar: this.getImageUrl(item.receiveAvatar),
                            sendAvatar: this.getImageUrl(item.sendAvatar)
                        }
                    });
                    uni.stopPullDownRefresh();
                }, e => {
                    uni.stopPullDownRefresh();
                })
            },
            getUnreadNum(id) {
                return this.unreads.filter(msg => {
                    return msg.consultantUserId === id;
                }).length || ''
            },
            timeFormat(timestramp) {
                if (timestramp) {
                    const oneDay = 24 * 60 * 60 * 1000;
                    const isBefore = Date.now() - new Date(timestramp).getTime() > oneDay;

                    if (isBefore) {
                        return moment(timestramp).format('yyyy/MM/DD')
                    } else {
                        return moment(timestramp).format('HH:mm')
                    }
                }
            },
            gotoConversation(conversation) {
                this.$store.dispatch('message/chat', {
                    consultantUserId: conversation.id,
                    page: 1,
                    size: 10
                })
                const query = encodeURIComponent(JSON.stringify(conversation));
                uni.navigateTo({
                    url: `/pages/message/conversation?query=${query}`
                });
            },
            convert(msg) {
                if (msg) {
                    let obj = {}
                    try {
                        obj = JSON.parse(msg.news)
                    } catch (e) {
                        obj = {}
                    }
                    switch (parseInt(msg.newsType)) {
                        case 1:
                            return msg.news || '';
                        case 2:
                            return `[测评] ${obj.title || ''}`;
                        case 3:
                            return `[购房报告] ${msg.news}`;
                        case 4:
                            return `[评价] ${obj.star || ''}`;
                        case 5:
                            return `[名片] ${obj.name}`;
                        case 6:
                            return '[图片]';
                        case 8:
                            return '[购房需求]';
                        default:
                            return msg.news || '';
                    }
                }
                return ''
            }
        }
    }
</script>

<style lang="scss">
    page {
        background-color: $basic-background-color;
    }
    .container {
        padding: 0;
        font-size: 25rpx;
        line-height: 25rpx;
        background-color: $basic-background-color;
        color: #565656;
        padding-bottom: 50px;
    }

    .search-bar-container {
        height: 140rpx;
        margin-bottom: 0;
        background-color: $basic-color;

        .search-bar {
            padding: 10rpx 10rpx 0 10rpx;

            .van-search {
                background: $basic-color !important;
            }

            .search-bar-icon {
                color: $basic-color;
                font-size: 35rpx;
                margin-left: 10rpx;
                margin-right: 10rpx;
            }
        }
    }


    .converation-list {
        background-color: #fff;

        .converation-item {
            display: flex;
            flex-direction: row;
            flex-wrap: nowrap;
            justify-content: space-between;
            align-items: center;
            border-bottom: 1px solid;
            border-color: #eee;
            padding: 20rpx 20rpx;

            .user-avatar {
                flex-grow: 0;
                height: 50px;
            }

            .msg-container {
                flex-grow: 1;

                .msg-detail-container {
                    padding: 0 20rpx;

                    .sender-info {
                        display: flex;
                        flex-direction: row;
                        flex-wrap: nowrap;
                        align-items: center;
                        justify-content: space-between;
                        flex-grow: 0;

                        .sender-name {
                            width: 400rpx;
                            text-overflow:ellipsis;
                            white-space:nowrap;
                            flex-grow: 1;
                            font-size: 25rpx;
                        }

                        .send-time {
                            flex-grow: 0;
                            color: #aaa;
                        }
                    }

                    .msg-preview-content {
                        margin-top: 25rpx;
                        display: flex;
                        flex-direction: row;
                        flex-wrap: nowrap;
                        align-items: center;
                        justify-content: space-between;
                        flex-grow: 1;

                        .msg-content {
                            width: 400rpx;
                            overflow:hidden;
                            text-overflow:ellipsis;
                            white-space:nowrap;
                            color: #aaa;
                            padding: 10rpx 0;
                        }

                        .msg-unread {
                            flex-grow: 0;

                            .msg-unread-circle {
                                padding: 0rpx 5rpx;
                                color: #fff;
                                background: $basic-color;
                                border-radius: 100rpx;
                                border: 4rpx solid $basic-color;
                                min-width: 18rpx;
                                text-align: center;
                            }
                        }
                    }
                }
            }

        }
    }



</style>
