<template>
    <view class="container">
        <view class="article-meta">
            <text class="article-meta-text">评论</text>
            <text class="article-meta-text">({{total}})</text>
        </view>
        <view class="comment-list" scroll-x="false" scroll-y="false" scroll-top="true">
            <view class="comment-container" v-for="(item, index) in dataList" 
                :key="index">

                <view class="user-avatar">
                    <van-image
                        round
                        width="50"
                        height="50"
                        lazy-load
                        :src="item.avatarUrl"
                    />
                </view>
                <view class="msg-container">
                    <view class="msg-detail-container">
                        <view class="sender-info">{{item.nickName}}</view>
                        <view class="msg-preview-content">
                            {{item.comment}}
                        </view>
                        <view class="send-time">
                            {{item.createTime}}
                        </view>
                    </view>
                </view>
                <view class="action-bar">
                    <van-icon v-if="item.isLike" name="like" color="red" size="40rpx" @tap="dislike(item, index)"/>
                    <van-icon v-else name="like-o" size="40rpx" @tap="like(item, index)"/>
                    <view class="like-num">{{item.likeCount || 0}}</view>
                </view>
            </view>
        </view>
        <view class="more-btn-container">
            <van-button color="#efefef" block v-if="!loading && dataList.length < total">上滑加载更多↑↑</van-button>
            <van-button color="#efefef" block v-if="!loading && dataList.length >= total">没有更多了</van-button>
            <van-button color="#efefef" block loading loading-type="spinner" v-if="loading"/>
        </view>
        <view class="footer">
            <view class="input-field-container">
                <view class="input-field">
                    <input v-model="input.value"/>
                </view>
                <view class="button-field">
                    <van-button custom-class="send-btn" @tap="send()" size="small">发送</van-button>
                </view>
            </view>
        </view>
    </view>
</template>

<script>
import moment from 'moment'
import htmlParser from '../../../common/html-parser'
import apiservice from '../../../api/apiservice'

export default {
    data() {
        return {
            query: '',
            banner: {},
            dataList: [],
            params: {
                // desc: 'createTime',
                asc: 'createTime',
                evaluationId: '',
                page: 1,
                size: 10,
                // startTime: '',
                // endTime: '',
            },
            total: 0,
            input: {
                value: ''
            },
            loading: true,
            $imageHost: this.$imageHost
        }
    },
    onShareAppMessage() {
        return {
            title: this.banner.title,
            path: '/pages/detail/detail?query=' + JSON.stringify(this.banner)
        }
    },
    onLoad(event) {
        // 目前在某些平台参数会被主动 decode，暂时这样处理。
        try {
            this.banner = JSON.parse(decodeURIComponent(event.query));
        } catch (error) {
            this.banner = JSON.parse(event.query);
        }

        uni.setNavigationBarTitle({
            title: this.banner.title
        });

        this.params.evaluationId = this.banner.id

        this.getComments(this.params);
    },
    onReachBottom() {
        console.log('on reach bottom')
        this.getMore()
    },
    methods: {
        getComments(params, isClear) {
            this.loading = true;
            apiservice.getEvaluationComment(params).then(data => {
                console.log('evaluation comments', data)
                if (isClear) {
                    this.dataList = data.list;
                } else {
                    this.dataList = this.dataList.concat(data.list);
                }
                this.params.page = data.pageNum;
                this.total = data.total;
                this.loading = false;
            }, err => {
                this.loading = false;
            });
        },
        timeFormat(timestramp) {
            return moment(timestramp).format("YYYY-MM-DD HH:mm:ss")
        },
        like(item, index) {
            if (!item.isLike) {
                this.sendLike(item.id).then(data => {
                    item.isLike = true;
                    item.likeCount = (item.likeCount || 0) + 1;
                });
            }
        },
        dislike(item, index) {
            if (item.isLike) {
                this.sendLike(item.id).then(data => {
                    item.isLike = false;
                    item.likeCount = item.likeCount - 1;
                });
            }
        },
        getMore() {
            if (this.dataList.length >= this.total) {
                return;
            }
            this.params.page = this.params.page + 1;
            this.getComments(this.params);
        },
        sendLike(id) {
            const data = {
                evaluationCommentId: id
            }
            return apiservice.saveEvaluationCommentLike(data);
        },
        send() {
            this.sendComment()
        },
        sendComment() {
            if (!this.input.value) {
                return
            }
            const that = this
            const data = {
                evaluationId: this.banner.id,
                comment: this.input.value
            }
            this.$set(this.input, 'value', '')
            apiservice.saveEvaluationComment(data).then(data => {
                that.params.page = 1;
                that.dataList = [];
                that.getComments(that.params, true)
            })
        },
        closePanel() {}
    },
}
</script>

<style lang="scss">
page {
    background: $basic-background-color;
}
.container {
    font-size: 28rpx;
    padding-bottom: 50px;
    background: $basic-background-color;
}

.article-meta {
    padding: 20rpx 30rpx;
    flex-direction: row;
    align-items: center;
    justify-content: flex-start;
    background: #fff;

}
.article-meta-text {
    color: #666;
}

.comment-list {
    background: #fff;

    .comment-container {
        padding: 20rpx;
        display: flex;
        flex-direction: row;
        flex-wrap: nowrap;
        justify-content: flex-start;
        align-items: flex-start;
        min-height: 140rpx;
        border-top: 1rpx solid;
        border-color: #efefef;

        .user-avatar {
            margin-top: 20px;
            flex-grow: 0;
        }
        .msg-container {
            flex-grow: 1;

            .msg-detail-container {
                padding: 10rpx 20rpx;

                .sender-info {
                    width: 500rpx;
                    font-size: 24rpx;
                    color: $basic-color;
                }

                .msg-preview-content {
                    margin-top: 10rpx;
                    color: #000;
                    font-size: 28rpx;
                }

                .send-time {
                    margin-top: 20rpx;
                    font-size: 24rpx;
                    color: #aaa;
                }
            }
        }
    }
}

.action-bar {
    display: flex;
    flex-direction: column;
    justify-content: space-around;
    align-items: center;
    flex-grow: 0;
    color: #aaa;

    margin-top: 20px;
    margin-right: 10rpx;

    .like-num {
        margin-top: 10rpx;
        width: 25rpx;
        font-size: 25rpx;
        text-align: center;
    }
}

.footer {
    position: fixed;
    bottom: 0;
    left: 0;
    width: 100%;
    height: 100rpx;

    .input-field-container {
        display: flex;
        flex-direction: row;
        flex-wrap: nowrap;
        justify-content: space-between;
        align-items: center;
        width: 100%;
        height: 100%;
        background-color: #efefef;

        .input-field {
            flex-grow: 1;
            margin: 15rpx;
            margin-right: 0;
            padding: 15rpx;
            border-top-left-radius: 10rpx;
            border-bottom-left-radius: 10rpx;
            background-color: #fff;
        }

        .button-field {
            flex-grow: 0;
            margin-right: 15rpx;

            .send-btn {
                padding-top: 35rpx;
                padding-bottom: 35rpx;
                border-top-right-radius: 10rpx;
                border-bottom-right-radius: 10rpx;
                color: #fff;
                background: $basic-color;
            }

            .action-btn {
                margin-top: 15rpx;
                margin-right: 15rpx;
            }
        }
    }
}


.more-btn-container {
    .van-button {
        border: none;
    }
    .van-button__text {
        color: #aaa;
    }

    .van-button--active:before {
        opacity: 0!important;
    }
}

</style>
