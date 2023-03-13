<template>
    <view class="container">
        <div style="padding: 0 15px">
            <div class="counselor-info">
                <div>{{consultant.name}}</div>
                <div class="counselor-tag">
                    <div class="counselor-item">
                        <div>{{consultant.advisoryCount}} 次</div>
                        <div>免费咨询</div>
                    </div>
                    <div class="counselor-item">
                        <div>{{consultant.buyHomeCount}} 人</div>
                        <div>推荐置业</div>
                    </div>
                    <div class="counselor-item">
                        <van-rate v-model="consultant.star" readonly/>
                        <div>服务星级</div>
                    </div>
                </div>
                <div class="img">
                    <image :src="consultant.avatar"></image>
                </div>
            </div>
        </div>
        <div class="recommend">
            <div class="recommend-head">
                <image src="../../../static/icon-project.png"></image>
                <div>所属项目</div>
            </div>
            <div class="recommend-content">
                <div class="recommend-left">
                    <image :src="consultant.project.imagesUrl"></image>
                </div>
                <div class="recommend-right">
                    <div>{{consultant.project.name}}</div>
                    <div class="recommend-text">
                        <div>{{consultant.project.address}}</div>
                    </div>
                    <div class="tag-list">
                        <van-tag color="#C30D23" text-color="#ffffff" v-for="(tag,tagIndex) in consultant.project.label"
                                 :key="tagIndex">{{tag}}
                        </van-tag>
                    </div>
                </div>
            </div>
        </div>
        <div class="evaluate">
            <div>客户评价</div>
            <div class="evaluate-list" v-for="(item,index) in counselorList" :key="index">
                <image :src="item.avatar"></image>
                <div class="evaluate-item">
                    <div class="evaluate-user">
                        <div>{{item.name}}</div>
                        <van-rate v-model="item.star" readonly/>
                    </div>
                    <div class="evaluate-content">
                        <div>{{item.comment}}
                            <text>{{item.createTime}}</text>
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
        </div>
    </view>
</template>

<script>
    import apiservice from '../../../api/apiservice'
    import util from '../../../common/util'
    import config from '../../../common/config'

    export default {
        name: "counselordetail",
        data() {
            return {
                value: 5,
                params: {
                    consultantId: null,
                    page: 1,
                    size: 10,
                },
                counselorList: [],
                consultant: null,
                hasMore: true,
                loaded: true
            }
        },
        created() {
        },
        onLoad(options) {
            if (options.consultantId) {
                this.params.consultantId = options.consultantId;
                this.consultantuserinfo();
                this.getConsultantComment();
            }
        },
        onReachBottom() {
            this.params.page++;
            this.getConsultantComment();
        },
        methods: {
            getConsultantComment() {
                if (this.hasMore && this.loaded) {
                    this.loaded = false;
                    apiservice.getconsultantcomment(this.params).then((res) => {
                        if (res) {
                            if (res.pages === 0) {
                                this.hasMore = false;
                                this.loaded = true;
                            } else {
                                res.list.forEach((item) => {
                                    item.createTime = util.format(item.createTime, 'yyyy-MM-dd');
                                });
                                this.counselorList = this.counselorList.concat(res.list);
                                setTimeout(() => {
                                    this.loaded = true;
                                    this.hasMore = !(this.params.page === res.pages);
                                }, 100);
                            }

                        } else {
                            this.hasMore = false;
                            this.loaded = true;
                        }
                    })
                }
            },
            consultantuserinfo() {
                let consultantId = {
                    id: this.params.consultantId
                };
                apiservice.getconsultantinfo(consultantId).then((res) => {
                    if (res) {
                        res.avatar = config.imageHost + res.avatar;
                        if (res.project && res.project.imagesUrl) {
                            res.project.imagesUrl = config.imageHost + res.project.imagesUrl;
                        }
                        this.consultant = res;
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

    .container {
        padding-top: 10px;
        font-size: 14px;
        overflow-y: auto;
    }

    .counselor-info {
        margin-top: 50px;
        height: 140px;
        background: #ffffff;
        border-radius: 5px;
        display: flex;
        flex-direction: column;
        align-items: center;
        position: relative;
    }

    .counselor-info > div:first-child {
        margin-top: 40px;
        width: 100%;
        line-height: 30px;
        text-align: center;
    }

    .counselor-tag {
        width: 100%;
        height: 100px;
        display: flex;
        align-items: center;
        justify-content: space-around;
    }

    .counselor-tag .counselor-item:not(:last-child) > div:first-child {
        color: #C30D23;
        font-weight: bold;
    }

    .counselor-item {
        height: 60px;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: space-around;
        flex: 1;
    }

    .counselor-tag .counselor-item:not(:last-child) {
        border-right: 1px solid #F5F5F5;
    }

    .van-icon-star {
        color: #C30D23 !important;
        font-size: 15px !important;
    }

    .van-icon {
        color: #C30D23 !important;
        font-size: 15px !important;
    }

    .counselor-tag .counselor-item > div:last-child {
        font-size: 12px;
    }

    .img {
        height: 100px;
        display: flex;
        align-items: center;
        justify-content: center;
        position: absolute;
        top: -50px;
        right: 0;
        left: 0;
    }

    .img image {
        width: 80px;
        height: 80px;
        border-radius: 50%;
    }

    .recommend {
        margin-top: 10px;
        background: #ffffff;
    }

    .recommend .recommend-head {
        padding: 0 15px;
        width: 100%;
        line-height: 40px;
        font-weight: bold;
        border-bottom: 1px solid #F5F5F5;
        display: flex;
        align-items: center;
        color: #C30D23;
    }

    .recommend .recommend-head image {
        width: 15px;
        height: 17px;
        margin-right: 10px;
    }

    .recommend-content {
        height: 120px;
        display: flex;
        align-items: center;
        padding: 5px 15px;
    }

    .recommend-left {
        width: 100px;
        height: 100px;
        border-radius: 5px;
        margin-right: 10px;
    }

    .recommend-left image {
        width: 100%;
        height: 100%;
        border-radius: 5px;
    }

    .recommend-right {
        width: calc(100% - 130px);
        display: flex;
        flex-direction: column;
        justify-content: space-around;
        line-height: 30px;
    }

    .recommend-right > div:first-child {
        font-weight: bold;
        line-height: 25px;
    }

    .recommend-text {
        display: flex;
        align-items: center;
        font-size: 12px;
        color: #8E8E8E;
    }

    .recommend-text > div:first-child {
        padding-right: 10px;
        border-right: 1px solid #F5F5F5;
    }

    .recommend-right .recommend-num {
        font-size: 12px;
        color: #C30D23;
        line-height: 25px;
    }

    .tag-list {
        width: 100%;
        display: flex;
        align-items: center;
    }

    .tag-list .van-tag {
        height: 20px;
        margin-right: 10px;
        width: 40px;
        justify-content: center;
    }

    .evaluate {
        background: #ffffff;
        margin-top: 10px;
    }

    .evaluate > div:first-child {
        width: 100%;
        line-height: 30px;
        border-bottom: 1px solid #F5F5F5;
        padding: 0 15px;
        font-weight: bold;
    }

    .evaluate-list {
        display: flex;
        align-items: center;
        border-bottom: 1px solid #F5F5F5;
        font-size: 14px;
        padding: 10px 15px;
    }

    .evaluate-list image {
        width: 60px;
        height: 60px;
        border-radius: 50%;
        margin-right: 10px;
    }

    .evaluate-item {
        width: calc(100% - 70px);
        display: flex;
        flex-direction: column;
    }

    .evaluate-content {
        display: flex;
        align-items: center;
        flex-wrap: wrap;
    }

    .evaluate-content div > text {
        color: #C7C7C7;
        font-size: 12px;
        margin-left: 10px;
    }

    .evaluate-user {
        display: flex;
        align-items: center;
        justify-content: space-between;
    }

    .evaluate-user > div {
        color: #C30D23;
    }

    .eui-loading {
        display: flex;
        justify-content: center;
        align-items: center;
        height: 42px;
        font-size: 13px;
        color: #999999;
        background: #F5F5F5 !important;
    }

    .eui-loading .van-button {
        background: #F5F5F5 !important;
        border: none !important;
        color: #cccccc !important;
    }
</style>
