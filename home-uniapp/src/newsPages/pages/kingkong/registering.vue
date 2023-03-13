<template>
    <view class="container">
        <scroll-view class="list" scroll-x="false" scroll-y="true" scroll-top="true">
            <view class="item-container" v-for="item in dataList" :key="item.id">
                <view class="item-content" @tap="toEvaluationDetail(item)">
                    <view class="preview-image left-content">
                        <van-image width="200rpx" height="200rpx" fit="cover" :src="$imageHost + item.mainImage"></van-image>
                    </view>
                    <view class="preview-text right-content">
                        <view class="title van-multi-ellipsis--l3">{{item.title}}</view>
                        <!-- <view class="content van-multi-ellipsis--l3">{{item.content}}</view> -->
                    </view>
                </view>
                <view class="item-footer" @tap="toEvaluationDetail(item)">
                    <!-- <text>进入测评 >></text> -->
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
                params: {}
            }
        },
        created() {
            console.log('create')
            this.queryList(this.params);
        },
        onShow() {
            console.log('on show')
        },
        onReachBottom() {
            console.log('on reach bottom')
        },
        onPullDownRefresh() {
            this.dataList = []
            this.queryList(this.params);
        },
        methods: {
            queryList(params) {
                apiservice.getRegistering(params).then(data => {
                    console.log(data);
                    this.dataList = this.dataList.concat(data);
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
        background: $basic-background-color;

    }
    .container {
        font-size: 14px;
        background: $basic-background-color;

        .list {
            height: calc(100vh - 20rpx);
        }
    }

    .item-container {
        padding: 20rpx;
        background: #fff;
        padding-bottom: 0;
        margin-bottom: 20rpx;

        .item-content {
            display: flex;
            flex-direction: row;
            flex-wrap: nowrap;
            justify-content: flex-start;
            align-items: flex-start;
            height: 200rpx;
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
                    margin-right: 10px;
                    font-size: 12px;
                    color: #ccc;
                }
            }
        }
    }


    .item-footer {
        text-align: right;
        color: $basic-color;
        // border-top: 1rpx solid #efefef;
        margin-left: -20rpx;
        margin-right: -20rpx;
        padding-top: 5rpx;
        padding-bottom: 15rpx;
        padding-left: 0;
        padding-right: 40rpx;

    }
</style>
