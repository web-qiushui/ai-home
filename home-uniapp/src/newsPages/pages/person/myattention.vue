<template>
    <view class="container" :class="isOpen?'active':'unactive'">
        <view class="select-tool-container">
            <assessment-filter @change="onFilterChange()" @open="open" @close="close"></assessment-filter>
            <assessment-sort @change="onSortChange" showMore="true"></assessment-sort>
        </view>
        <view class="assessment-list-container">
            <view v-for="(item, index) in evaluationCollectList" :key="index" class="assessment-card"
                  @click="toEvaluationDetail(item)">
                <van-image class="assessment-img" width="100%" height="150" :src="item.mainImage"/>
                <view class="assessment-text">{{item.title}}</view>
                <view class="tag-container tag-container-flex">
                    <view class="tag-item" :class="{'last': tagIndex == item.label.length - 1}"
                          v-for="(tag, tagIndex) in item.label" :key="tagIndex">
                        <text>{{tag}}</text>
                    </view>
                </view>
            </view>
        </view>
        <view class="eui-loading">
            <block v-if="hasMore">
                <van-button color="#f5f5f5" block loading loading-type="spinner" loading-text="加载中..."/>
            </block>
            <block v-if="!hasMore">
                <block v-if="evaluationCollectList.length === 0">没有查询到您想要的数据</block>
                <block v-else>没有更多数据了</block>
            </block>
        </view>
    </view>
</template>

<script>
    import apiservice from '../../../api/apiservice'
    import config from '../../../common/config'

    export default {
        name: "myattention",
        data() {
            return {
                evaluationCollectList: [],
                params: {
                    page: 1,
                    size: 10,
                },
                hasMore: true,
                loaded: true,
                isOpen: false
            }
        },
        created() {
            this.evaluationcollectlist();
        },
        onReachBottom() {
            console.log('上拉');
            if (this.hasMore && this.loaded) {
                this.params.page++;
                this.evaluationcollectlist();
            }
        },
        onPullDownRefresh() {
            this.hasMore = true;
            this.loaded = true;
            this.evaluationCollectList = [];
            this.params.page = 1;
            this.evaluationcollectlist();
        },
        methods: {
            open() {
                this.isOpen = true;
            },
            close() {
                this.isOpen = false;
            },
            evaluationcollectlist() {
                if (this.hasMore && this.loaded) {
                    this.loaded = false;
                    apiservice.getevaluationcollectlist(this.params).then((res) => {
                        if (res) {
                            if (res.pages === 0) {
                                this.hasMore = false;
                                this.loaded = true;
                            } else {
                                res.list.forEach((item) => {
                                    item.label = JSON.parse(item.label);
                                    item.mainImage = config.imageHost + item.mainImage;
                                });
                                this.evaluationCollectList = this.evaluationCollectList.concat(res.list);
                                setTimeout(() => {
                                    this.loaded = true;
                                    this.hasMore = !(this.params.page === res.pages);
                                }, 100);
                            }
                        } else {
                            this.hasMore = false;
                            this.loaded = true;
                        }
                        uni.stopPullDownRefresh();
                    }, e => {
                        uni.stopPullDownRefresh();
                    })
                }
            },
            onFilterChange(e) {
                const filter = e.detail.__args__[0];
                this.$set(this.params, 'region', filter.region);
                this.$set(this.params, 'price', filter.price);
                this.$set(this.params, 'area', filter.area);
                this.hasMore = true;
                this.loaded = true;
                this.params.page = 1;
                this.evaluationCollectList = [];
                this.evaluationcollectlist(this.params);
            },
            onSortChange(e) {
                const sort = e.detail.__args__[0];
                this.hasMore = true;
                this.loaded = true;
                this.params.page = 1;
                this.params.desc = '';
                this.params.asc = '';
                this.evaluationCollectList = [];
                this.evaluationcollectlist(this.params);
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

<style>
    page {
        height: 100vh;
        background: #F5F5F5;
    }

    .container {
        padding: 0;
        font-size: 25rpx;
        line-height: 24rpx;
        background-color: #efefef;
        color: #565656;
    }

    .container.active {
        height: 100%;
        overflow: hidden;
    }

    .container.unactive {
        overflow: auto;
    }

    .select-tool-container {
        margin-top: 20rpx;
        background-color: #fff;
    }

    .filter-bar {
        padding-left: 25rpx;
        padding-right: 25rpx;
    }

    .filter-container .van-dropdown-menu__title {
        background-color: #efefef !important;
        padding: 10rpx 70rpx 10rpx 45rpx !important;
        border-radius: 30rpx;
        font-size: 25rpx !important;
        color: #565656;
    }

    .filter-container .van-dropdown-menu__title::after {
        right: 45rpx !important;
    }

    .assessment-list-container {
        background-color: #fff;
    }

    .assessment-card {
        position: relative;
        padding: 30rpx;
        border-bottom: 1px solid #efefef;
    }

    .assessment-img .van-image .van-image__img {
        border-radius: 10rpx;
    }

    .assessment-text {
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
    }

    .tag-container {
        position: absolute;
        left: 30rpx;
        bottom: 30rpx;
        width: calc(100% - 60rpx);
        height: 60rpx;
        border-bottom-left-radius: 10rpx;
        border-bottom-right-radius: 10rpx;
        background-color: rgba(0, 0, 0, .7);
    }

    .tag-container-flex {
        display: flex;
        flex-direction: row;
        flex-wrap: nowrap;
        align-items: center;
        justify-content: space-around;
    }

    .tag-item {
        color: #fff;
        flex-grow: 1;
        text-align: center;
        border-right: 1px solid #fff;
    }

    .tag-item.last {
        border: none;
    }

    .eui-loading {
        display: flex;
        justify-content: center;
        align-items: center;
        height: 42px;
        font-size: 12px;
        color: #999999;
        background: #F5F5F5 !important;
    }

    .eui-loading .van-button {
        background: #F5F5F5 !important;
        border: none !important;
        color: #cccccc !important;
    }
</style>
