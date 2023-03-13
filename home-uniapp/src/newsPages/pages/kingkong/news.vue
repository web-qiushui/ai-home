<template>
    <view class="container">
        <scroll-view class="list" scroll-x="false" scroll-y="true" scroll-top="true">
            <view class="item-container" v-for="(item, index) in dataList" :key="index">
                <view class="item-content">
                    <view class="content-left date-time">
                        <text class="time-content">{{formatTime(item)}}</text>
                        <text class="date-content">{{formatDate(item)}}</text>
                    </view>
                    <view class="content-right">
                        <view class="item-header">
                            <view class="title van-multi-ellipsis--l3">{{item.title}}</view>
                        </view>
                        <view class="item-body">
                            <view class="item-text" :class="{'van-multi-ellipsis--l3': !item.expanded}">{{item.content}}</view>
                        </view>
                        <view class="item-footer">
                            <view class="footer-left">
                                <view class="expand-title" @tap="open(item)">
                                    <view class="title">{{item.expanded ? '收起' : '展开'}}</view>
                                    <view class="icon" :class="{'up': item.expanded}"></view>
                                </view>
                            </view>
                            <view class="footer-right">
                                <view class="icon-container share">
                                    <van-button class="share-button" icon="/static/ai-icon-share.png" open-type="share" :dataset="item"></van-button>
                                    <view class="text">{{item.forwardCount}}</view>
                                </view>
                                <view class="icon-container like">
                                    <image class="icon" src="/static/ai-icon-like.png" v-if="!item.isLike" @tap="like(item)"></image>
                                    <image class="icon" src="/static/ai-icon-like-active.png" v-else @tap="like(item)"></image>
                                    <view class="text">{{item.likeCount}}</view>
                                </view>
                            </view>
                        </view>
                    </view>
                </view>
            </view>
			<view class="more-btn-container">
			    <van-button color="#efefef" block @click="getMore()" v-if="!loading && list.length < total">上滑加载更多↑↑</van-button>
			    <van-button color="#efefef" block v-if="!loading && list.length >= total">没有更多了</van-button>
			    <van-button color="#efefef" block loading loading-type="spinner"  v-if="loading"/>
			</view>
        </scroll-view>
    </view>
</template>

<script>
    import apiservice from '../../../api/apiservice'
    import moment from 'moment'

    export default {
        data() {
            return {
                params: {
                    page: 1,
                    size: 10,
                },
                dataList: [],
				total: 0,
				loading: false
            }
        },
        created() {
            this.queryList(this.params);
        },
        onShow() {
            // this.queryList(this.params);
        },
        onReachBottom() {
            console.log('on reach bottom')
			this.getMore()
        },
        onPullDownRefresh() {
            this.params.page = 1
            this.dataList = []
            this.queryList(this.params, true);
        },
        onShareAppMessage(e) {
            const item = e.target.dataset.detail;
            const query = this.createQuery(item)
            this.share(item)
            return {
                title: item.title,
                path: `/newsPages/pages/evaluation/detail?query=${query}`,
                success: function(res){
                    // 转发成功之后的回调
                    if(res.errMsg == 'shareAppMessage:ok'){
                    }
                },
            }
        },
        methods: {
            queryList(params, isClear) {
                const that = this
				this.loading = true
                apiservice.get24hNews().then(data => {
					 this.loading = false
                    console.log(data);
                   /* this.dataList = data.map(item => {
                        return {
                            ...item,
                            expanded: false,
                            query: that.createQuery(item)
                        }
                    }); */
				    const list = data.list.map(item => {
				        try {
				            item.tags = item.label
				        } catch (e) {
				            item.tags = [];
                        }
                        item.expanded = false;
				        return item;
				    });
				    if (isClear) {
				        this.dataList = list
				    } else {
				        this.dataList = this.dataList.concat(list);
				    }
				
				    this.params.page = data.pageNum;
				    this.total = data.total;
                    uni.stopPullDownRefresh();
				}, e => {
				    this.loading = false
                    uni.stopPullDownRefresh();
				})
            },
			getMore() {
			    if (this.dataList.length >= this.total) {
			        return;
			    }
			    this.params.page = this.params.page + 1;
			    this.queryList(this.params)
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
            formatDate(item) {
                return moment(item.createTime).format('yyyy-MM-DD')
            },
            formatTime(item) {
                return moment(item.createTime).format('HH:mm')
            },
            expand(item) {
                item.expanded = !item.expanded;
            },
            like(item) {
                const data = {
                    evaluationId: item.id
                }
                return apiservice.saveEvaluationLike(data).then(data => {
                    if (item.isLike) {
                        item.likeCount = item.likeCount - 1;
                    } else {
                        item.likeCount = item.likeCount + 1;
                    }
                    item.isLike = !item.isLike
                });
            },
            share(obj) {
                if (obj && obj.id) {
                    const item = this.dataList.find(data => data.id === obj.id)
                    const data = {
                        evaluationId: item.id
                    }
                    apiservice.saveEvaluationForward(data).then(data => {
                        item.forwardCount = item.forwardCount + 1;
                        item.isForward = true
                    });
                }
            },
            createQuery(item) {
                const query = encodeURIComponent(JSON.stringify({
                    id: item.id,
                    title: item.title,
                    author: item.author,
                    createTime: item.createTime
                }));

                return query;
            },
            isEllipsis(content) {
                const LEN = 100;
                if (content) {
                    return content.length > LEN;
                }

                return false;
            },
            open(item) {
                item.expanded = !item.expanded
            }
        }
    }
</script>

<style lang="scss">
    page {
        background: #fff;
    }
    .container {
        font-size: 14px;
        line-height: 24px;
        background: #fff;

        .list {
            height: calc(100vh - 20rpx);
        }
    }

    .item-container {
        padding: 20rpx 30rpx;
        padding-left: 0;
        padding-bottom: 0;
        margin: 15rpx 0;
        background: #fff;
        border-bottom: 1rpx solid #efefef;

        .item-content {
            display: flex;
            flex-direction: row;
            flex-wrap: nowrap;
            justify-content: flex-end;
            align-items: flex-start;

            .content-left {
                flex: 1 1 30%;

                display: flex;
                flex-direction: column;
                flex-wrap: nowrap;
                justify-content: center;
                align-items: center;
                text-align: center;
                line-height: 60rpx;

                .time-content {
                    color: $basic-color;
                    font-size: 50rpx;
                    text-align: center;
                }

                .date-content {
                    text-align: center;
                }
            }

            .content-right {
                flex: 1 1 70%;

                .item-header {

                    .title {
                        font-size: $basic-title-font-size;
                        line-height: $basic-title-line-height;
                    }
                }

                .item-body {
                    margin-top: 15rpx;

                    .item-text {
                        font-size: 24rpx;
                    }
                }

                .item-footer {
                    display: flex;
                    flex-direction: row;
                    flex-wrap: nowrap;
                    justify-content: center;
                    align-items: center;
                    width: 100%;

                    .footer-left {
                        flex: 50%;

                        .expand-title {
                            position: relative;
                            box-sizing: border-box;
                            max-width: 100%;
                            color: #323233;

                            .title {
                                font-size: 24rpx;
                            }

                            .icon {
                                position: absolute;
                                top: 50%;
                                left: 60rpx;
                                margin-top: -10rpx;
                                border-color: transparent transparent currentcolor currentcolor;
                                border-style: solid;
                                border-width: 6rpx;
                                -webkit-transform: rotate(-45deg);
                                transform: rotate(-45deg);
                                opacity: .8;
                                content: "";

                                &.up {
                                    margin-top: -3rpx;
                                    -webkit-transform: rotate(135deg);
                                    transform: rotate(135deg); 
                                }
                            }
                        }

                    }

                    .footer-right {
                        display: flex;
                        flex-direction: row;
                        flex-wrap: nowrap;
                        justify-content: space-between;
                        align-items: center;

                        flex: 50%;

                        .icon-container {
                            flex: 0 0 50%;
                            display: flex;
                            flex-direction: row;
                            flex-wrap: nowrap;
                            align-items: center;
                            justify-content: center;

                            &.share {
                                .text {
                                    margin-top: 10rpx;;
                                }
                            }

                            &.like {
                                .text {
                                    margin-top: 10rpx;;
                                }
                            }

                            .share-button {
                                color: #666;
                                height: 90rpx;
                                margin-top: 2rpx;

                                .van-button {
                                    border: none !important;
                                    padding-right: 10rpx;

                                    .van-icon {
                                        width: 30rpx;
                                        height: 30rpx;
                                    }
                                }
                                .van-button--active:before {
                                    opacity: 0!important;
                                }
                            }

                            .icon {
                                width: 30rpx;
                                height: 30rpx;
                            }

                            .text {
                                margin-left: 15rpx;
                            }
                        }
                    }
                }
            }
        }
    }

    .button-container {
        display: flex;
        flex-direction: row;
        justify-content: space-between;
        align-items: center;
    }
</style>
