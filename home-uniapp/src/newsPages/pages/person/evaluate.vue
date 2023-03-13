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
        <div class="evaluate-list" v-for="(item,index) in consultantCommentList" :key="index"
             @click="counselorDetail(item.consultantId)">
            <image :src="item.avatar"></image>
            <div class="evaluate-item">
                <div class="evaluate-user">
                    <div>{{item.name}}</div>
                    <van-rate v-model="item.star" readonly/>
                </div>
                <div>
                    {{item.comment}}
                    <text>{{item.createTime}}</text>
                </div>
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
    </view>
</template>

<script>
    import apiservice from '../../../api/apiservice'
    import config from '../../../common/config'

    export default {
        name: "evaluate",
        data() {
            return {
                params: {
                    page: 1,
                    size: 10,
                },
                consultantCommentList: [],
                timeActive: true,
                likeActive: false,
                hasMore: true,
                loaded: true
            }
        },
        created() {
            this.getCommentList();
        },
        onReachBottom() {
            if (this.hasMore && this.loaded) {
                this.params.page++;
                this.getCommentList();
            }
        },
        onPullDownRefresh() {
            this.hasMore = true;
            this.loaded = true;
            this.consultantCommentList = [];
            this.params.page = 1;
            this.getCommentList();
        },
        methods: {
            counselorDetail(id) {
                uni.navigateTo({url: `/newsPages/pages/anouncement/counselordetail?consultantId=${id}`})
            },
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
                this.consultantCommentList = [];
                this.params.page = 1;
                this.getCommentList();
            },
            getCommentList() {
                if (this.hasMore && this.loaded) {
                    this.loaded = false;
                    apiservice.getConsultantCommentList(this.params).then((res) => {
                        if (res) {
                            if (res.pages === 0) {
                                this.hasMore = false;
                                this.loaded = true;
                            } else {
                                res.list.forEach((item) => {
                                    item.avatar = config.imageHost + item.avatar;
                                });
                                this.consultantCommentList = this.consultantCommentList.concat(res.list);
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
        height: 100vh;
        background: #F5F5F5;
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

    .evaluate-list {
        height: 80px;
        display: flex;
        align-items: center;
        border-top: 1px solid #F5F5F5;
        font-size: 14px;
        padding: 10px 15px;
        background: #ffffff;
    }

    .evaluate-list image {
        width: 45px;
        height: 45px;
        margin-right: 10px;
        border-radius: 50%;
    }

    .evaluate-item {
        width: calc(100% - 55px);
        display: flex;
        flex-direction: column;
        flex-wrap: wrap;
    }

    .evaluate-item > div:last-child {
        color: #333333;
        font-size: 14px;
    }

    .evaluate-item > div:last-child text {
        color: #cccccc;
        font-size: 12px;
        margin-left: 15px;
    }

    .evaluate-user {
        display: flex;
        align-items: center;
        justify-content: space-between;
    }

    .van-icon-star {
        color: #C30D23 !important;
        font-size: 13px !important;
    }

    .van-icon {
        color: #C30D23 !important;
        font-size: 13px !important;
    }

    .evaluate-user > div {
        font-size: 12px;
        color: #C30D23;
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
