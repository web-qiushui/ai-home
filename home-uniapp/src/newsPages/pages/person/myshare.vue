<template>
    <view class="container">
        <view class="search-bar-container">
            <view class="search-bar">
                <van-search
                        :value="keywords"
                        shape="round"
                        @search="onSearch"
                        @change="onSearchChange"
                        background="#fff"
                        placeholder="输入关键字搜索"
                        use-left-icon-slot="true"
                >
                    <van-icon slot="left-icon" class="search-bar-icon" name="search"/>
                </van-search>
            </view>
        </view>
        <div class="list-container">
            <div class="item" v-if="shareList.length>0">
                <div class="assessment-card" v-for="(item,index) in shareList" :key="index"
                     @click="toEvaluationDetail(item)">
                    <image class="assessment-img" :src="item.mainImage"></image>
                    <div class="assessment-text">
                        <div>{{item.title}}</div>
                    </div>
                    <div class="tag-container">
                        <div class="tag-item" v-for="(tag,tagIndex) in item.label" :key="tagIndex">
                            <text>{{tag}}</text>
                        </div>
                    </div>
                </div>
            </div>
            <div class="eui-loading">
                <block v-if="hasMore">
                    <van-button color="#f5f5f5" block loading loading-type="spinner" loading-text="加载中..."/>
                </block>
                <block v-if="!hasMore">
                    <block v-if="shareList.length === 0">没有查询到您想要的数据</block>
                    <block v-else>没有更多数据了</block>
                </block>
            </div>
        </div>
    </view>
</template>

<script>
    import apiservice from '../../../api/apiservice'
    import config from '../../../common/config'

    export default {
        name: "myshare",
        data() {
            return {
                shareList: [],
                params: {
                    page: 1,
                    size: 10,
                },
                hasMore: true,
                loaded: true,
                keywords: ''
            }
        },
        onShow() {
            this.myShare();
        },
        onReachBottom() {
            if (this.hasMore && this.loaded) {
                this.params.page++;
                this.myShare();
            }
        },
        onPullDownRefresh() {
            this.hasMore = true;
            this.loaded = true;
            this.shareList = [];
            this.params.page = 1;
            this.myShare();
        },
        methods: {
            onSearch(e) {
                this.keywords = e.detail;
                this.params.search = e.detail;
                this.params.page = 1;
                this.hasMore = true;
                this.loaded = true;
                this.shareList = [];
                this.myShare();
            },
            onSearchChange(e) {
                if (!e.detail) {
                    this.keywords = '';
                    this.params.search = e.detail;
                    this.params.page = 1;
                    this.hasMore = true;
                    this.loaded = true;
                    this.shareList = [];
                    this.myShare();
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
            myShare() {
                if (this.hasMore && this.loaded) {
                    this.loaded = false;
                    apiservice.getEvaluationForwardList(this.params).then((res) => {
                        if (res) {
                            if (res.pages === 0) {
                                this.hasMore = false;
                                this.loaded = true;
                            } else {
                                res.list.forEach((item) => {
                                    item.label = JSON.parse(item.label);
                                    item.mainImage = config.imageHost + item.mainImage
                                });
                                this.shareList = this.shareList.concat(res.list);
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
            }

        }
    }
</script>

<style>
    page {
        height: 100%;
        background: #F5F5F5;
    }

    .container {
        overflow-y: auto;
        font-size: 12px;
    }

    .search-bar-container {
        height: 50px;
        margin-bottom: 0;
        background-color: #fff;
    }

    .search-bar-icon {
        color: #c30D23;
        font-size: 18px;
        margin-left: 5px;
        margin-right: 5px;
    }

    .van-dropdown-item.van-dropdown-item--down {
        margin: 0 15px !important;
        box-shadow: 2px 2px 5px 2px #aaa;
    }

    .item {
        padding: 10px;
        background: #ffffff;
    }

    .assessment-card {
        width: 100%;
        height: 130px;
        position: relative;
        margin-bottom: 10px;
        border: 1px solid #ccc;
        border-radius: 5px;
        background: #ffffff;
    }

    .assessment-img {
        width: 100%;
        height: 100%;
        border-radius: 5px;
    }

    .assessment-text {
        position: absolute;
        left: 0;
        top: 10px;
        right: 0;
        width: 100%;
        margin: auto;
        color: #fff;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        height: 50px;
    }

    .assessment-text div {
        line-height: 25px;
        font-size: 14px;
    }

    .tag-container {
        position: absolute;
        bottom: 0;
        width: 100%;
        height: 30px;
        border-bottom-left-radius: 5px;
        border-bottom-right-radius: 5px;
        background-color: rgba(0, 0, 0, .7);
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

    .tag-container .tag-item:last-child {
        border: none;
    }

    .eui-loading {
        display: flex;
        justify-content: center;
        align-items: center;
        height: 42px;
        font-size: 13px;
        color: #999999;
        background: transparent !important;
    }

    .eui-loading .van-button {
        background: #F5F5F5 !important;
        border: none !important;
        color: #cccccc !important;
    }
</style>
