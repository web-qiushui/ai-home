<template>
    <view class="container">
        <div>
            <van-tabs title-active-color="#C30D23" color="#C30D23" @click="switchTab">
                <van-tab title="客户顾问">
                    <div class="counselor-content">
                        <div class="counselor-list" v-for="(item,index) in customerList" :key="index"
                             @click="counselorDetail(item.id)">
                            <image :src="item.avatar"></image>
                            <div class="counselor">
                                <div class="counselor-user">
                                    <div class="user">
                                        <div>{{item.name}}</div>
                                        <div>AI顾问</div>
                                    </div>
                                    <van-rate v-model="item.star" readonly/>
                                </div>
                                <div class="btns">
                                    <div class="btn" @click.stop="consult(item.id)">咨询</div>
                                    <div class="btn" @click.stop="counselor(item)">评价</div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="eui-loading">
                        <block v-if="hasMore">
                            <van-button color="#f5f5f5" block loading loading-type="spinner" loading-text="加载中..."/>
                        </block>
                        <block v-if="!hasMore">
                            <block v-if="customerList.length === 0">没有查询到您想要的数据</block>
                            <block v-else>没有更多数据了</block>
                        </block>
                    </div>
                </van-tab>
                <van-tab title="置业顾问">
                    <div class="counselor-content">
                        <div class="counselor-list" v-for="(listitem,listindex) in counselorList" :key="listindex"
                             @click="counselorDetail(listitem.id)">
                            <image :src="listitem.avatar"></image>
                            <div class="counselor">
                                <div class="counselor-user">
                                    <div class="user">
                                        <div>{{listitem.name}}</div>
                                        <div>置业顾问</div>
                                    </div>
                                    <van-rate v-model="listitem.star" readonly/>
                                </div>
                                <div class="btns">
                                    <div class="btn" @click.stop="consult(listitem.id)">咨询</div>
                                    <div class="btn" @click.stop="counselor(listitem)">评价</div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="eui-loading">
                        <block v-if="hasMore">
                            <van-button color="#f5f5f5" block loading loading-type="spinner" loading-text="加载中..."/>
                        </block>
                        <block v-if="!hasMore">
                            <block v-if="counselorList.length === 0">没有查询到您想要的数据</block>
                            <block v-else>没有更多数据了</block>
                        </block>
                    </div>
                </van-tab>
            </van-tabs>
        </div>
    </view>
</template>

<script>
    import apiservice from '../../../api/apiservice'
    import config from '../../../common/config'

    export default {
        name: "mycounselor",
        data() {
            return {
                value: 5,
                customerList: [],
                counselorList: [],
                params: {
                    page: 1,
                    size: 10,
                    type: 1
                },
                hasMore: true,
                loaded: true
            }
        },
        created() {
            this.myConsultant();
        },
        onShow() {

        },
        onReachBottom() {
            if (this.hasMore && this.loaded) {
                this.params.page++;
                this.myConsultant();
            }
        },
        onPullDownRefresh() {
            if (this.params.type === 1) {
                this.customerList = [];
            } else {
                this.counselorList = [];
            }
            this.hasMore = true;
            this.loaded = true;
            this.params.page = 1;
            this.myConsultant();
        },
        methods: {
            myConsultant() {
                if (this.hasMore && this.loaded) {
                    this.loaded = false;
                    apiservice.getUserConsultantList(this.params).then((res) => {
                        if (res) {
                            if (res.pages === 0) {
                                this.hasMore = false;
                                this.loaded = true;
                            } else {
                                res.list.forEach((item) => {
                                    item.avatar = config.imageHost + item.avatar;
                                });
                                this.customerList = this.customerList.concat(this.get(res.list, 1));
                                this.counselorList = this.counselorList.concat(this.get(res.list, 2));
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
            get(list, key) {
                return list.filter(item => item.type === key)
            },
            switchTab(e) {
                if (e.detail.title === '客户顾问') {
                    this.params.type = 1;
                    this.customerList = [];
                } else {
                    this.params.type = 2;
                    this.counselorList = [];
                }
                this.params.page = 1;
                this.loaded = true;
                this.hasMore = true;
                this.myConsultant();
            },
            counselor(item) {
                let consultant = encodeURIComponent(JSON.stringify(item));
                uni.navigateTo({url: `/newsPages/pages/person/evaluating?consultant=${consultant}`})
            },
            counselorDetail(id) {
                uni.navigateTo({url: `/newsPages/pages/anouncement/counselordetail?consultantId=${id}`})
            },
            consult(id) {
                let consultant = {
                    consultantId: id
                };
                apiservice.getbuddybyconsultant(consultant).then((res) => {
                    console.log(res);
                    if (res) {
                        this.$store.dispatch('message/chat', {
                            consultantUserId: res.id,
                            page: 1,
                            size: 10
                        })
                        const query = encodeURIComponent(JSON.stringify(res));
                        uni.navigateTo({
                            url: `/pages/message/conversation?query=${query}`
                        });
                    }
                })
            }
        }
    }
</script>

<style>
    page {
        height: 100%;
        background: #F5F5F5;
    }

    .counselor-content {
        margin-top: 10px;
    }

    .counselor-list {
        height: 90px;
        display: flex;
        align-items: center;
        padding: 0 15px;
        border-bottom: 1px solid #F5F5F5;
        background: #ffffff;
    }

    .counselor-list image {
        width: 45px;
        height: 45px;
        border-radius: 50%;
        margin-right: 15px;
    }

    .counselor {
        width: calc(100% - 55px);
        height: 60px;
        display: flex;
        flex-direction: column;
        justify-content: space-between;
    }

    .counselor-user {
        display: flex;
        align-items: center;
        justify-content: space-between;
    }

    .user {
        width: 100px;
        display: flex;
        align-items: center;
        justify-content: space-between;
    }

    .user > div:first-child {
        font-size: 15px;
        font-weight: bold;
    }

    .user > div:last-child {
        font-size: 12px;
        color: #cccccc;
    }

    .van-icon-star {
        color: #C30D23 !important;
        font-size: 13px !important;
    }

    .van-icon {
        color: #C30D23 !important;
        font-size: 13px !important;
    }

    .btns {
        display: flex;
        align-items: center;
    }

    .btn {
        width: 75px;
        height: 25px;
        background: #C30D23;
        color: #ffffff;
        border-radius: 5px;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 12px;
        margin-right: 20px;
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
