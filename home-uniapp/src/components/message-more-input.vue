<template>
    <view class="more-input-field input-field-container">
        <view class="input-field-content" v-for="(card, index) in cards" :key="index">
            <view class="input-field" @tap="onTap(card)">
                <image class="input-icon" :src="card.src" mode="aspectFit"/>
            </view>
            <view class="field-name">{{card.name}}</view>
        </view>
        <van-popup :show="consultant.show" @close="close" position="bottom">
            <view class="popup-container consultant-container" style="background: #fff" v-if="consultant.show">
                <view class="popup-list consultant-list">
                    <view class="popup-item consultant-item" v-for="(item, index) in consultant.list" :key="index" @tap="selectConsultant(item)">
                        {{item.name}}
                    </view>
                </view>
            </view>
        </van-popup>
        <van-popup :show="reply.show" @close="close" position="bottom">
            <view class="popup-container reply-container" style="background: #fff" v-if="reply.show">
                <view class="popup-list reply-list">
                    <view class="popup-item reply-item" v-for="(item, index) in reply.list" :key="index" @tap="selectReply(item)">
                        {{item.content}}
                    </view>
                </view>
            </view>
        </van-popup>
        <van-popup :show="evaluation.show" @close="close" position="bottom">
            <van-search :value="evaluation.params.search" placeholder="请输入搜索关键词" @search="onEvaluationSearch" @clear="onEvaluationClear" @change="onEvaluationChange"/>
            <scroll-view class="popup-container evaluation-container" scroll-y="true" @scrolltolower="queryMoreEvaluation" style="background: #fff" v-if="evaluation.show">
                <view class="popup-list evaluation-list">
                    <view class="popup-item evaluation-item" v-for="(item, index) in evaluation.list" :key="index" @tap="selectEvaluation(item)">
                        {{item.title}}
                    </view>
                </view>
            </scroll-view>
        </van-popup>
    </view>
</template>

<script>
    import apiservice from '../api/apiservice'
    const type = {
        TEXT: 1,
        EVALUATION: 2,
        REPORT: 3,
        REVIEW: 4,
        CARD: 5,
        IMAGE: 6,
        QUICK: 7,
        DEMEND: 8
    }
    export default {
        props: ['cards', 'chat'],
        data() {
            return {
                consultant: {
                    show: false,
                    list: [],
                    total: 0,
                    params: {
                        page: 1,
                        size: 20
                    }
                },
                reply: {
                    show: false,
                    list: [],
                    total: 0,
                    params: {
                        status: 1,
                        type: 1
                    }
                },
                evaluation: {
                    show: false,
                    list: [],
                    total: 0,
                    params: {
                        search: '',
                        page: 1,
                        size: 20
                    }
                },
                userInfo: null
            }
        },
        created() {
            this.userInfo = uni.getStorageSync('sysUserInfo')
        },
        computed: {
        },
        methods: {
            onTap(card) {
                switch(card.code) {
                    case 'image':
                        this.openImage()
                        break;
                    case 'photograph':
                        this.openCamera()
                        break;
                    case 'businessCard':
                        this.openConsultantPopup()
                        break;
                    case 'customerReviews':
                        this.sendCustomerReviews()
                        break;
                    case 'evaluation':
                        this.openEvaluationPopup()
                        break;
                    case 'quickReply':
                        this.openReplyPopup()
                        break;
                }
            },
            send(data) {
                this.$emit('send', data)
            },
            close() {
                this.consultant.show = false
                this.reply.show = false
                this.evaluation.show = false
            },
            openEvaluationPopup() {
                this.evaluation.show = true
                this.evaluation.params.page = 1
                this.evaluation.list = []
                this.evaluation.toatl = 0
                this.evaluation.params.search = ''
                this.queryEvaluataion()
            },
            onEvaluationSearch(e) {
                this.evaluation.params.page = 1
                this.evaluation.list = []
                this.evaluation.toatl = 0
                this.evaluation.params.search = e.detail
                this.queryEvaluataion()
            },
            onEvaluationClear(e) {
                this.evaluation.params.page = 1
                this.evaluation.list = []
                this.evaluation.toatl = 0
                this.evaluation.params.search = ''
                this.queryEvaluataion()
            },
            onEvaluationChange(e) {
                if (!e.detail) {
                    this.onEvaluationClear()
                }
            },
            queryEvaluataion() {
                apiservice.getMainPageInfo(this.evaluation.params).then(data => {
                    if (this.evaluation.params.page === 1) {
                        this.evaluation.list = data.list;
                    } else {
                        this.evaluation.list = this.evaluation.list.concat(data.list);
                    }
                    this.evaluation.total = data.total
                })
            },
            queryMoreEvaluation() {
                if (this.evaluation.total <= this.evaluation.list) {
                    this.evaluation.params.page++
                    this.queryEvaluataion()
                }
            },
            openConsultantPopup() {
                this.consultant.show = true
                this.consultant.params.page = 1
                this.consultant.list = []
                this.consultant.toatl = 0
                this.queryConsultant()
            },
            queryConsultant() {
                apiservice.getHomeConsultantList(this.consultant.params).then(data => {
                    if (this.consultant.params.page === 1) {
                        this.consultant.list = data.list;
                    } else {
                        this.consultant.list = this.consultant.list.concat(data);
                    }
                    this.consultant.total = data.total
                })
            },
            queryMoreConsultant() {
                if (this.consultant.total <= this.consultant.list) {
                    this.consultant.params.page++
                    this.queryConsultant()
                }
            },
            openReplyPopup() {
                this.reply.show = true
                if (this.reply.list.length === 0 ) {
                    this.queryReply()
                }
            },
            queryReply() {
                const auth = this.userInfo.role
                this.reply.params.type = auth
                apiservice.getQuickReplyConfig(this.reply.params).then(data => {
                    this.reply.list = data
                })
            },
            openImage() {
                const that = this;
                uni.chooseImage({
                    count: 1,
                    sizeType: ["original", "compressed"],
                    sourceType: ["album"],
                    success(res){
                        that.sendImage(res);
                    },
                });
            },
            openCamera() {
                const that = this;
                wx.chooseMedia({
                    count: 1,
                    mediaType: ['image'],
                    sourceType: ['camera'],
                    maxDuration: 30,
                    camera: 'back',
                    success(res) {
                        that.sendPhoto(res);
                    }
                })
            },
            sendPhoto(res) {
                const that = this
                let tempFilePath = ''
                if (res.tempFiles) {
                    tempFilePath = res.tempFiles[0].tempFilePath
                } else if(res.tempFilePaths) {
                    tempFilePath = res.tempFilePaths[0];
                }
                this.uploadImage(tempFilePath)

            },
            sendImage(res) {
                const that = this
                let tempFilePath = ''
                if (res.tempFiles) {
                    tempFilePath = res.tempFiles[0].path
                } else if(res.tempFilePaths) {
                    tempFilePath = res.tempFilePaths[0];
                }
                this.uploadImage(tempFilePath)

            },
            uploadImage(tempFilePath) {
                const that = this
                if (!tempFilePath) {
                    return
                }
                wx.uploadFile({
                    url: `${this.$imageHost}/home-service/app/api/upload/images`,
                    header: {
                        'token': uni.getStorageSync('token'),
                    },
                    filePath: tempFilePath,
                    name: 'file',
                    success: function (res) {
                        if (res) {
                            const data = JSON.parse(res.data);
                            const msg = {
                                type: type.IMAGE,
                                content: data.data
                            }
                            that.send(msg)
                        }
                    },
                });
            },
            selectConsultant(consultant) {
                if (consultant) {
                    const content = {
                        uuid: this.guid(),
                        status: 0,
                        star: 0,
                        consultantId: consultant.id,
                        recommendId: this.chat.consultantId,
                        ...consultant
                    }
                    const data = {
                        type: type.CARD,
                        content: JSON.stringify(content)
                    }
                    this.send(data)
                }

                this.close()
            },
            selectReply(reply) {
                const data = {
                    type: type.QUICK,
                    content: reply.content
                }
                this.send(data)
                this.close()
            },
            selectEvaluation(item) {
                if (item) {
                    const data = {
                        type: type.EVALUATION,
                        content: JSON.stringify(item)
                    }
                    this.send(data)
                }
                this.close()
            },
            sendCustomerReviews() {
                const content = {
                    uuid: this.guid(),
                    status: 0,
                    star: 0,
                    consultantId: 0,
                    recommendId: this.chat.consultantId,
                }
                const data = {
                    type: type.REVIEW,
                    content: JSON.stringify(content)
                }
                this.send(data)
            }
        }
    }
</script>

<style lang="scss" scoped>
    .input-field-container {
        display: flex;
        flex-direction: row;
        flex-wrap: wrap;
        justify-content: flex-start;
        align-items: flex-start;
        width: 100%;
        height: 100%;
        background-color: #efefef;
        border-top: 1px solid #ccc;
        transition-duration: 300ms;

        .input-field-content {
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            flex: 0 0 25%;
            padding-top: 30rpx;

            .input-field {
                padding: 10rpx;
                height: 80rpx;
                width: 80rpx;
                border-radius: 25rpx;
                background-color: #fff;
                // border: 2rpx dashed #000;

                display: flex;
                justify-content: center;
                align-items: center;

                .input-icon {
                    width: 80rpx;
                    height: 80rpx;
                }
            }

            .field-name {
                text-align: center;
                font-size: 20rpx;
            }
        }
    }

    .popup-container {
        width: 750rpx;
        height: 450rpx;
        padding: 20rpx 0;
        background-color: #fff;
        color: #333;

        &.evaluation-container {
            padding-top: 0;
        }

        .popup-list {
            padding: 0 20rpx;
            .popup-item {
                padding: 15rpx 15rpx;
                border-bottom: 1rpx solid #efefef;
            }
        }
    }
</style>
