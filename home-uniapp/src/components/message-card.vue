<template>
    <view class="message-card">
        <view class="message-content message-text" v-if="msg.type === 1 || msg.type === 7" :class="className">
            <view class="message-bubble bubble-left" v-if="!right"></view>
            <view class="message-context" :class="className">
                {{msg.content}}
            </view>
            <view class="message-bubble bubble-right" v-if="right"></view>
        </view>
        <view class="message-content message-evaluation" v-if="msg.type === 2" :class="className">
            <view class="evaluation-container" @tap="clickOnPanel()">
                <view class="evaluation-content">
                    <view class="evaluation-img-content">
                        <van-image class="evaluation-img" width="200rpx" height="200rpx" fit="cover" :src="$imageHost + obj.mainImage"/>
                    </view>
                    <view class="evaluation-title">{{obj.title}}</view>
                </view>
                <view class="evaluation-footer"></view>
            </view>
        </view>
        <view class="message-content message-report" v-if="msg.type === 3" :class="className">
            <view></view>
        </view>
        <view class="message-content message-review" v-if="msg.type === 4" :class="className">
            <view class="review-container">
                <view class="review-header">
                    您对{{consultantName}}的服务满意吗？
                </view>
                <view class="review-content">
                    <view class="review-star" v-for="(i, index) in 5" :key="index" @tap="doScore(index)">
                        <van-icon name="star-o" v-if="score <= i"/>
                        <van-icon name="star" v-if="score > i"/>
                    </view>
                </view>
                <view class="review-footer">
                    <view class="btn-text left">不满意</view>
                    <view class="btn-text right">满意</view>
                </view>
            </view>
        </view>
        <view class="message-content message-namecard" v-if="msg.type === 5" :class="className">
            <view class="namecard-container" @tap="clickOnPanel">
                <view class="namecard-content">
                    <view class="namecard-avator">
                        <image class="namecard-img" mode="aspectFill" :src="$imageHost + obj.avatar"/>
                    </view>
                    <view class="namecard-text">
                        <view class="namecard-name">{{obj.name}}</view>
                    </view>
                </view>
            </view>
        </view>
        <view class="message-content message-image" v-if="msg.type === 6" :class="className">
            <view class="image-container" @tap="openPopup()">
                <image class="msg-img" mode="widthFix" :src="$imageHost + msg.content">
            </view>
        </view>
        <view class="message-content message-demend" v-if="msg.type === 8" :class="className">
            <view class="demend-container" @tap="clickOnPanel">
                <view class="demend-header">
                    购房需求
                </view>
                <view class="demend-content">
                    <view>区域: {{obj.region}}</view>
                    <view>价格: {{obj.price}}</view>
                    <view>面积: {{obj.area}}</view>
                    <view>电话: {{obj.phone}}</view>
                    <view>标签: {{obj.label}}</view>
                </view>
                <view class="demend-footer">
                </view>
            </view>
        </view>
        <van-popup class="message-popup" custom-style="background: transparent" :show="showPopup" @close="onClose">
            <view class="popup-container" v-if="msg.type === 6" @tap="onClose()">
                <image class="popup-img" :src="$imageHost + msg.content" mode="widthFix"></image>
            </view>
        </van-popup>
        <van-dialog id="van-dialog" confirm-button-color="#c30D23"/>
    </view> 
</template>

<script>
    import Dialog from '../wxcomponents/@vant/weapp/dialog/dialog'
    import apiservice from '../api/apiservice'

    const type = {
        TEXT: 1,
        EVALUATION: 2,
        REPORT: 3,
        REVIEW: 4,
        NAMECARD: 5,
        IMAGE: 6,
        QUICK: 7,
        DEMEND: 8
    }
    export default {
        props: ['msg', 'right', 'chat'],
        data() {
            return {
                score: 0,
                status: 0,
                isScored: false,
                showPopup: false,
                $imageHost: this.$imageHost
            }
        },
        created() {
            this.initMsg()
        },
        computed: {
            className() {
                return this.right ? 'right' : 'left';
            },
            obj() {
                return this.getObj(this.msg)
            },
            consultantName() {
                if (this.chat) {
                    if (this.chat.receiveId === this.chat.consultantUserId) {
                        return this.chat.receiveName;
                    } else if (this.chat.sendId === this.chat.consultantUserId) {
                        return this.chat.sendName;
                    }
                }

                return '顾问';
            }
        },
        methods: {
            doScore(count) {
                if (this.isScored) {
                    return;
                }
                if (this.chat.sendId === this.chat.consultantUserId || this.status > 0) {
                    return;
                }
                this.score = count + 1
                Dialog.confirm({
                    title: '提示',
                    message: '确认提交评价吗？',
                    messageAlign: 'left',
                    context: this,
                    confirmButtonText: '是',
                    cancelButtonText: '否'
                })
                .then(() => {
                    const uuid = this.getUUID()
                    const data = {
                        comment: '',
                        consultentId: this.chat.consultantId,
                        star: this.score,
                        uuid: uuid
                    }
                    apiservice.saveConsultantComment(data).then(res => {
                        this.isScored = true
                    })
                })
                .catch(() => {
                    // on cancel
                });
            },
            openPopup() {
                if (this.msg.type === 6) {
                    this.showPopup = true
                }
            },
            onClose() {
                this.showPopup = false
            },
            initMsg() {
                const content = this.getContent()
                if (content) {
                    this.status = content.status ? content.status : 0
                    this.score = content.star ? content.star : 0
                }
                console.log('message card data', content, this.status, this.score)
            },
            getObj(msg) {
                let content = null
                if (msg && (msg.type === type.EVALUATION ||
                    msg.type === type.NAMECARD ||
                    msg.type === type.DEMEND)) {
                    try {
                        content = JSON.parse(msg.content)
                    } catch (e) {
                        content = {}
                    }
                    return content;
                }

                return null
            },
            getUUID() {
                let content = null;
                try {
                    content = JSON.parse(this.msg.content)
                } catch (e) {
                    content = {}
                }

                if (content && content.uuid) {
                    return content.uuid
                }

                return ''
            },
            getContent() {
                let content = null;
                try {
                    content = JSON.parse(this.msg.content)
                } catch (e) {
                    content = {}
                }

                return content;
            },
            clickOnPanel() {
                if (this.msg.type === type.EVALUATION) {
                    this.toEvaluationDetail(this.obj)
                }
                if (this.msg.type === type.DEMEND) {
                    this.toDemend(this.obj)
                }
                if (this.msg.type === type.NAMECARD) {
                    if (this.status > 0) {
                        return;
                    }
                    if (this.chat.sendId !== this.chat.consultantUserId) {
                        this.addBuddyConfirm(this.obj)
                    }
                }
            },
            toEvaluationDetail(item) {
                const query = encodeURIComponent(JSON.stringify({
                    id: item.id,
                    title: item.title,
                    author: item.author,
                    createTime: item.createTime
                }));
                uni.navigateTo({
                    url: `/newsPages/pages/evaluation/detail?query=${query}`
                })
            },
            toDemend(item) {
                const query = encodeURIComponent(JSON.stringify({
                    ...item
                }));
                let anouncementdetail = encodeURIComponent(JSON.stringify(item));
                uni.navigateTo({url: `/newsPages/pages/anouncement/anouncementdetail?anouncementdetail=${anouncementdetail}&type=view`})
            },
            addBuddyConfirm(item) {
                Dialog.confirm({
                    title: '提示',
                    message: '确认添加顾问吗？',
                    messageAlign: 'left',
                    context: this
                })
                .then(() => {
                    // on confirm
                    this.addBuddy(item);
                })
                .catch(() => {
                    // on cancel
                });
            },
            addBuddy(item) {
                const uuid = this.getUUID()
                const data = {
                    consultantId: item.id,
                    recommendId: this.chat.consultantId,
                    uuid: uuid
                }
                apiservice.saveHomeBuddy(data).then(res => {
                    this.gotoConversation(res)
                })
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
        }
    }
</script>

<style lang="scss">

.message-card {

    .message-content {
        display: flex;

        &.left {
            flex-direction: row;
            justify-content: flex-start;
            align-items: flex-start;
        }

        &.right {
            flex-direction: row;
            justify-content: flex-end;
            align-items: flex-start;
        }

        &.message-evaluation {
            margin-left: 20rpx !important;
            margin-right: 20rpx !important;
            flex-grow: 1;
            background: #fff;
            border-radius: 10rpx;
            color: #333;
            overflow: hidden;

            .evaluation-container {
                width: 100%;
                display: flex;
                flex-direction: column;
                align-items: center;

                .evaluation-content {
                    width: 100%;
                    display: flex;
                    flex-direction: row;
                    align-items: center;
                    justify-content: flex-start;

                    .evaluation-img-content {
                        .evaluation-img {
                            display: flex;
                            flex-direction: row;
                            align-items: center;
                            justify-content: center;

                            .van-image {
                                .van-image__img {
                                    border-top-left-radius: 10rpx !important;
                                    border-bottom-right-radius: 10rpx !important;
                                }

                                .van-image__error {
                                    border-top-left-radius: 10rpx !important;
                                    border-bottom-right-radius: 10rpx !important;
                                }
                            }
                        }

                    }

                    .evaluation-title {
                        align-self: baseline;
                        padding: 10px;
                        font-size: 25rpx;
                        line-height: 40rpx;
                        display: -webkit-box;
                        -webkit-box-orient: vertical;
                        -webkit-line-clamp: 4;
                        overflow: hidden;
                        word-break: break-all;

                    }

                }
                .evaluation-footer {

                }
            }
        }

        &.message-report {
            margin-left: 20rpx !important;
            margin-right: 20rpx !important;
        }

        &.message-review {
            margin-left: 20rpx !important;
            margin-right: 20rpx !important;
            flex-grow: 1;
            background: #fff;
            border-radius: 10rpx;
            color: #333;

            .review-container {
                width: 100%;
                font-size: 20rpx;

                .review-header {
                    flex-grow: 1;
                    text-align: center;
                    padding: 20rpx 0;
                }

                .review-content {
                    display: flex;
                    flex-direction: row;
                    justify-content: center;
                    align-items: center;
                    flex-wrap: nowrap;

                    .review-star {
                        color: $basic-color;
                        flex: 0 0 20%;
                        display: flex;
                        justify-content: center;
                        font-size: 35rpx;
                    }
                }
                .review-footer {
                    display: flex;
                    flex-direction: row;
                    flex-wrap: nowrap;
                    justify-content: space-between;
                    align-items: center;
                    margin: 20rpx 30rpx;

                    .btn-text {
                        flex: 0 0 50%;

                        &.left {
                            text-align: left;
                        }

                        &.right {
                            text-align: right;
                        }
                    }
                }
            }
        }

        &.message-namecard {
            margin-left: 20rpx !important;
            margin-right: 20rpx !important;
            flex-grow: 1;
            background: #fff;
            border-radius: 10rpx;
            color: #333;
            overflow: hidden;

            .namecard-container {
                width: 100%;
                display: flex;
                flex-direction: column;
                align-items: center;

                .namecard-content {
                    width: 100%;
                    display: flex;
                    flex-direction: row;
                    align-items: center;
                    justify-content: flex-start;

                    .namecard-avator {
                        display: flex;
                        flex-direction: column;
                        align-items: center;
                        justify-content: center;

                        .namecard-img {
                            flex-grow: 1;
                            width: 150rpx;
                            height: 150rpx;
                        }
                    }

                    .namecard-text {
                        align-self: flex-start;

                        .namecard-name {
                            padding-top: 20rpx;
                            padding-left: 20rpx;
                            font-size: 28rpx;
                            display: -webkit-box;
                            -webkit-box-orient: vertical;
                            -webkit-line-clamp: 1;
                            overflow: hidden;
                        }
                    }

                }
            }

        }

        &.message-image {
            margin-left: 20rpx !important;
            margin-right: 20rpx !important;

            .image-container {
                .msg-img {
                    max-width: 300rpx;
                }

            }
        }

        &.message-text {
            flex-grow: 1;

            .message-context {
                display: inline-block;
                padding: 20rpx;
                border-radius: 15rpx;
                background-color: #fff;
                min-height: 45rpx;
                word-break: break-all;

                &.right {
                    color: #fff;
                    background: $basic-color;
                }
            }

            .message-bubble {
                display: inline-block;
                border-top: 15rpx solid transparent;
                border-bottom: 15rpx solid transparent;
                margin-top: 25rpx;

                &.bubble-left {
                    border-right: 20rpx solid #fff;
                }

                &.bubble-right {
                    border-left: 20rpx solid $basic-color;
                }
            }
        }

        &.message-demend {
            margin-left: 20rpx !important;
            margin-right: 20rpx !important;
            flex-grow: 1;
            background: #fff;
            border-radius: 10rpx;
            color: #333;

            .demend-container {
                display: flex;
                flex-direction: column;
                font-size: 20rpx;
                width: 100%;

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
        }

    }
    .message-popup {
        .popup-container {
            .image {
                max-width: 750rpx;
            }
        }
    }
}
</style>
