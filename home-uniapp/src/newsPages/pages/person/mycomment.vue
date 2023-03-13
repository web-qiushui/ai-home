<template>
    <view class="container">
        <div class="tab">
            <div class="tab-item" :class="{'active': timeActive}" @click="tabClick('time')">
                <div>按时间排序</div>
            </div>
            <div class="tab-item" :class="{'active': likeActive}" @click="tabClick('like')">
                <div>按好评排序</div>
            </div>
        </div>
        <div class="anouncement">
            <div class="anouncement-list" v-for="(item,index) in anouncementList" :key="index"
                 @click="toEvaluationDetail(item)">
                <image :src="item.avatarUrl"></image>
                <div class="anouncement-text">
                    <div>{{item.comment}}</div>
                    <div>{{item.createTime}}</div>
                </div>
            </div>
            <div class="eui-loading">
                <block v-if="hasMore">
                    <van-button color="#f5f5f5" block loading loading-type="spinner" loading-text="加载中..."/>
                </block>
                <block v-if="!hasMore">
                    <block v-if="anouncementList.length === 0">没有查询到您想要的数据</block>
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
        name: "mycomment",
        data() {
            return {
                anouncementList: [],
                params: {
                    page: 1,
                    size: 10,
                },
                hasMore: true,
                loaded: true,
                timeActive: true,
                likeActive: false,
            }
        },
        created() {
            this.evaluationcommentlist();
        },
        onReachBottom() {
            if (this.hasMore && this.loaded) {
                this.params.page++;
                this.evaluationcommentlist();
            }
        },
        onPullDownRefresh() {
            this.hasMore = true;
            this.loaded = true;
            this.anouncementList = [];
            this.params.page = 1;
            this.evaluationcommentlist();
        },
        methods: {
            tabClick(type) {
                if (type === 'time') {
                    this.timeActive = true;
                    this.likeActive = false;
                    this.params.desc = 'a.create_time';
                } else {
                    this.timeActive = false;
                    this.likeActive = true;
                    this.params.desc = 'a.star';
                }
                this.hasMore = true;
                this.loaded = true;
                this.anouncementList = [];
                this.params.page = 1;
                this.evaluationcommentlist();
            },
            toEvaluationDetail(item) {
                const query = encodeURIComponent(JSON.stringify({
                    id: item.evaluationId,
                    title: item.title,
                    author: item.author,
                    createTime: item.createTime
                }));
                uni.navigateTo({
                    url: `/newsPages/pages/evaluation/detail?query=${query}`
                })
            },
            evaluationcommentlist() {
                if (this.hasMore && this.loaded) {
                    this.loaded = false;
                    apiservice.getEvaluationCommentList(this.params).then((res) => {
                        if (res) {
                            if (res.pages === 0) {
                                this.hasMore = false;
                                this.loaded = true;
                            } else {
                                this.anouncementList = this.anouncementList.concat(res.list);
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
        font-size: 14px;
    }

    .tab {
        height: 40px;
        display: flex;
        align-items: center;
        padding: 0 15px;
        background: #ffffff;
        font-size: 12px;
    }

    .tab-item {
        display: flex;
        align-items: center;
        line-height: 30px;
    }

    .tab-item image {
        width: 5px;
        height: 11px;
        margin-left: 5px;
    }

    .tab-item.active {
        color: #C30D23;
        border-bottom: 2px solid #C30D23;
    }

    .tab > .tab-item:first-child {
        margin-right: 20px;
    }

    .anouncement-list {
        display: flex;
        height: 85px;
        border-bottom: 1px solid #F5F5F5;
        display: flex;
        align-items: center;
        padding: 0 10px;
        background: #ffffff;
    }

    .anouncement-list image {
        width: 55px;
        height: 55px;
        border-radius: 5px;
        margin-right: 20px;
    }

    .anouncement-text {
        width: calc(100% - 75px);
        height: 70px;
        display: flex;
        flex-direction: column;
        justify-content: space-around;
    }

    .anouncement-text > div:first-child {
        font-size: 14px;
    }

    .anouncement-text > div:last-child {
        color: #C7C7C7;
        font-size: 12px;
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
