<template>
    <view class="container">
        <scroll-view class="list" scroll-x="false" scroll-y="true" scroll-top="true">
            <view class="item-container">
                <view class="item-content">
                    <view class="preview-image left-content">
                        <van-image width="200rpx" height="200rpx" fit="cover" :src="$imageHost + notice.imageUrl"></van-image>
                    </view>
                    <view class="preview-text right-content">
                        <view class="title van-ellipsis">今日开盘楼盘</view>
                        <ellipsis-card class="content" v-if="notice.description" :value="notice.description"></ellipsis-card>
                    </view>
                </view>
            </view>
            <view class="evaluation-list" v-for="(item, $index) in dataList" :key="$index">
                <view class="evaluation-card" @click="toEvaluationDetail(item)">
                    <van-image class="evaluation-img" width="100%" height="120" fit="cover" :src="$imageHost + item.mainImage"/>
                    <view class="evaluation-text">{{item.title}}</view>
                    <view class="tag-container tag-container-flex">
                        <view class="tag-item" :class="{'last': tagIndex == item.tags.length - 1}" v-for="(tag, tagIndex) in item.tags" :key="tagIndex">
                            <text>{{tag}}</text>
                        </view>
                    </view>
                </view>
            </view>
        </scroll-view>
    </view>
</template>

<script>
    import apiservice from '../../../api/apiservice'

    export default {
        data() {
            return {
                $imageHost: this.$imageHost,
                dataList: [],
                notice: {},
                params: {}
            }
        },
        created() {
            this.queryNotice();
            this.queryList(this.params);
        },
        onReachBottom() {
            console.log('on reach bottom')
        },
        onPullDownRefresh() {
            this.dataList = []
            this.queryNotice();
            this.queryList(this.params);
        },
        methods: {
            queryNotice() {
                apiservice.getOpeningNotice().then(data => {
                    this.notice = data;
                    console.log('open notice', data, this.notice)
                })
            },
            queryList(params, isClear) {
                apiservice.getPreSale(params).then(data => {
                    console.log(data);
                    const list = data.map(item => {
                        try {
                            item.tags = item.label
                        } catch (e) {
                            item.tags = [];
                        }
                        return item;
                    });
                    this.dataList = this.dataList.concat(list);
                    uni.stopPullDownRefresh();
                }, e => {
                    uni.stopPullDownRefresh();
                })
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
        }
    }
</script>

<style lang="scss">
    page {
        background-color: $basic-background-color;
    }
    .container {
        font-size: 25rpx;
        background: $basic-background-color;

        .list {
            height: calc(100vh - 20rpx);
        }
    }

    .item-container.evaluation-list {
        padding: 0;
    }

    .item-container {
        padding: 30rpx;
        background: #fff;
        margin-bottom: 20rpx;

        .item-content {
            display: flex;
            flex-direction: row;
            flex-wrap: nowrap;
            justify-content: flex-start;
            align-items: flex-start;
            width: 100%;

            .left-content {
                flex: 0 0 220rpx;

                .van-image {
                    .van-image__img {
                        border-radius: 10rpx;
                    }

                    .van-image__error {
                        border-radius: 10rpx;
                    }
                }
            }

            .right-content {
                width: calc(100% - 220rpx);

                .title {
                    font-size: $basic-title-font-size;
                    line-height: $basic-title-line-height;
                    margin-bottom: 20rpx;
                }

                .content {
                    font-size: 24rpx;
                    color: #ccc;
                }
            }
        }
    }

    .evaluation-list {
        .evaluation-card {
            position: relative;
            padding: 30rpx;
            border-bottom: 1px solid #efefef;

            .evaluation-img {
                filter: brightness(0.8);

                .van-image {
                    .van-image__img {
                        border-radius: 10rpx;
                    }

                    .van-image__error {
                        border-radius: 10rpx;
                    }
                }
            }

            .evaluation-text {
                position: absolute;
                left: 0;
                top: 0;
                right: 0;
                bottom: 0;
                height: 60px;
                margin: auto;
                color: #fff;
                text-align: center;
                width: 80%;
                font-size: $basic-title-font-size;
                line-height: $basic-title-line-height;
            }
            .tag-container {
                position: absolute;
                left: 30rpx;
                bottom: 37rpx;
                width: calc(100% - 60rpx);
                height: 60rpx;
                border-bottom-left-radius: 10rpx;
                border-bottom-right-radius: 10rpx;
                background-color: rgba(0,0,0,.7);
            }

            .tag-container-flex {
                display: flex;
                flex-direction: row;
                flex-wrap: nowrap;
                align-items: center;
                justify-content: center;
            }

            .tag-item {
                color: #fff;
                padding-left: 20rpx;
                padding-right: 20rpx;
                text-align: center;
                border-right: 1px solid #fff;
            }

            .tag-item.last {
                border: none;
            }
        }
    }


    .item-footer {
        text-align: center;
        color: $basic-color;
        border-top: 1rpx solid #efefef;
        padding: 25rpx 0;
        margin-left: -20rpx;
        margin-right: -20rpx;
    }
</style>
