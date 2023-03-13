<template>
    <view class="container">
        <div class="login-content">
            <div class="head-text">AI家</div>
            <div style="font-size: 18px;line-height: 25px;margin: 10px 0">给您置业帮助从这里开始</div>
            <div class="icon-check">
                <van-checkbox v-model="checked" shape="square" ref="checkbox" @change="toggle">
                    <text @click.stop="agreement">同意用户协议</text>
                </van-checkbox>
            </div>
            <div class="btns">
                <van-button color="#C30D23" open-type="getUserInfo" round lang="zh_CN" size="large" block
                            @getuserinfo="getUserinfo">
                    微信授权登录
                </van-button>
                <!--                <van-button size="large" block @click="loginPhone()">手机号码登录</van-button>-->
            </div>
        </div>
    </view>
</template>

<script>
    import util from "../../common/util";
    import apiservice from "../../api/apiservice";

    export default {
        data() {
            return {
                checked: true,
            };
        },
        created() {
        },
        mounted() {
        },
        methods: {
            toggle(e) {
                this.checked = e.detail;
            },
            getUserinfo(resp) {
                let info = resp.detail;
                uni.setStorageSync('userInfo', info.userInfo);
                if (!this.checked) {
                    util.toast("请同意用户协议");
                    return false;
                } else {
                    uni.login({
                        success: res => {
                            let msg = {
                                rawData: info.rawData,
                                signature: info.signature,
                                code: res.code
                            };
                            util.showLoading('正在登录...');
                            apiservice.loginWx(msg).then((result) => {
                                util.hideLoading();
                                if (result) {
                                    uni.setStorageSync("token", result);
                                    uni.switchTab({
                                        url: '/pages/home/home'
                                    });
                                    this.$store.dispatch('message/init', this.$socketHost)
                                }
                            });
                        }
                    });
                }
            },
            agreement() {
                uni.navigateTo({
                    url: "/newsPages/pages/person/agreement"
                });
            }
        }
    }
    ;
</script>

<style>
    .container {
        padding: 20px;
        font-size: 14px;
        line-height: 24px;
    }

    .login-content {
        width: 100%;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
    }

    .head-text {
        line-height: 47px;
        font-size: 36px;
    }

    .icon-check {
        width: 100%;
        height: 160px;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: flex-end;
        font-size: 18px;
    }

    .van-checkbox__icon--checked {
        background-color: #C30D23 !important;
        border-color: #C30D23 !important;
    }

    .btns {
        width: 100%;
        height: 160px;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: space-around;
        padding-top: 10px;
    }

    .btns van-button {
        width: 100%;
    }
</style>
