<template>
    <view class="container">
        <div class="user-info">
            <image :src="consultent.avatar"></image>
            <div class="info-detail">
                <div class="info-item">
                    <div class="info">
                        <div style="font-size: 16px;font-weight: bold;color: #333333">{{consultent.name}}</div>
                        <div v-if="consultent.type===1">客户顾问</div>
                        <div v-if="consultent.type===2">置业顾问</div>
                    </div>
                    <!--                    <div>西安市</div>-->
                </div>
                <div class="info-star">
                    <div>服务星级</div>
                    <van-rate v-model="consultent.star" readonly/>
                </div>
            </div>
        </div>
        <div class="star">
            <div>我的评价</div>
            <van-rate v-model="value1" @change="doStar"/>
        </div>
        <div>
            <div class="add">添加评论</div>
            <van-field
                    v-model="message"
                    rows="2"
                    type="textarea"
                    placeholder="请输入您的评论"
                    :border="false"
                    @input="fileInput"
            />
        </div>
        <div class="btns">
            <van-button color="#C30D23" size="large" round block @click="saveconsultantcomment">提交</van-button>
        </div>
    </view>
</template>

<script>
    import apiservice from '../../../api/apiservice';
    import util from '../../../common/util'

    export default {
        name: "evaluating",
        data() {
            return {
                value: 5,
                value1: 0,
                message: null,
                consultent: null
            }
        },
        onLoad(options) {
            this.consultent = JSON.parse(decodeURIComponent(options.consultant));
        },
        methods: {
            fileInput(e) {
                this.message = e.detail;
            },
            doStar(e) {
                this.value1 = e.detail;
            },
            saveconsultantcomment() {
                if (!this.message) {
                    util.toast('请输入评论');
                    return false
                } else {
                    let data = {
                        comment: this.message,
                        consultentId: this.consultent.id,
                        star: this.value1
                    };
                    util.showLoading('提交中...');
                    apiservice.postsaveconsultantcomment(data).then((res) => {
                        util.hideLoading();
                        uni.navigateBack();
                    })
                }
            }
        }
    }
</script>

<style>
    .container {
        height: 100vh;
        background: #F5F5F5;
    }

    .user-info {
        height: 75px;
        display: flex;
        align-items: center;
        padding: 0 15px;
        background: #ffffff;
    }

    .user-info image {
        width: 45px;
        height: 45px;
        border-radius: 50%;
        margin-right: 20px;
    }

    .info-detail {
        width: calc(100% - 75px);
        height: 50px;
        display: flex;
        flex-direction: column;
        justify-content: space-around;
        font-size: 12px;
    }

    .info-item {
        width: 100%;
        display: flex;
        align-items: center;
        justify-content: space-between;
    }

    .info-item div:last-child {
        color: #888888;
    }

    .info-star {
        display: flex;
        align-items: center;
    }

    .info-star div:first-child {
        margin-right: 20px;
    }

    .info {
        display: flex;
        align-items: center;
        justify-content: space-between;
    }

    .info > div:first-child {
        margin-right: 20px;
    }

    .info-star .van-icon-star {
        font-size: 13px !important;
    }

    .info-star .van-rate__icon, .info-star .van-icon {
        font-size: 13px !important;
        color: #C30D23 !important;
    }

    .van-icon {
        color: #C30D23 !important;
    }

    .van-icon-star {
        color: #C30D23 !important;
    }

    .star {
        height: 50px;
        display: flex;
        align-items: center;
        font-size: 14px;
        background: #ffffff;
        border-bottom: 1px solid #F5F5F5;
        padding: 0 15px;
        margin-top: 10px;
    }

    .star > div {
        margin-right: 20px;
        font-size: 15px;
        font-weight: bold;
    }

    .add {
        height: 50px;
        line-height: 50px;
        font-size: 15px;
        font-weight: bold;
        background: #ffffff;
        border-bottom: 1px solid #F5F5F5;
        padding: 0 15px;
    }

    .van-cell, textarea {
        height: 150px !important;
    }

    .btns {
        margin-top: 20px;
        padding: 0 15px;
    }
</style>
