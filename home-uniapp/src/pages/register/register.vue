<template>
    <view class="container">
        <div class="head-text">手机注册登录</div>
        <div>
            <van-form>
                <van-field
                        v-model="username"
                        name="用户名"
                        placeholder="请输入手机号"
                        :border="false"
                        @input="fileInput"
                >
                    <template #button>
                        <van-button size="small" native-type="button" @click="getCaptcha">
                            <span v-if="cooldown">{{cooldownTime}}秒</span>
                            <span v-else>发送验证码</span>
                        </van-button>
                    </template>
                </van-field>
                <van-field
                        v-model="yanZhengMa"
                        type="password"
                        name="密码"
                        placeholder="请输入验证码"
                        :border="false"
                        @input="fileInputCode"
                />
                <div style="margin: 16px;">
                    <van-button round block color="#566ADD" native-type="submit" @click="login">
                        登录
                    </van-button>
                </div>
            </van-form>
        </div>
    </view>
</template>

<script>
    import util from '../../common/util'

    export default {
        name: "register",
        data() {
            return {
                username: '',
                yanZhengMa: '',
                cooldown: false,
                cooldownTime: 60,
                timeInterval: 0
            }
        },
        methods: {
            fileInput(e) {
                this.username = e.detail;
            },
            fileInputCode(e) {
                this.yanZhengMa = e.detail;
            },
            formCheck() {
                if (!this.username) {
                    util.toast('请输入手机号码');
                    return false;
                }
                if (!util.isMobile(this.username)) {
                    util.toast('手机号码输入不正确');
                    return false;
                }
                return true;
            },
            getCaptcha() {
                let $this = this;
                if ($this.formCheck()) {
                    this.cooldown = true;
                    this.timeInterval = setInterval(() => {
                        this.cooldownTime = --this.cooldownTime;
                        if (this.cooldownTime === 0) {
                            this.cooldownTime = 60;
                            this.cooldown = false;
                            clearInterval(this.timeInterval);
                        }
                    }, 1000);
                }
            },
            login() {
                if (this.formCheck()) {
                    if (this.formCheck()) {
                        util.showLoading('登录中...');
                        setTimeout(() => {
                            util.hideLoading();
                            util.toast('登录成功');
                            uni.navigateTo({
                                url: `/pages/login/login`
                            })
                        }, 500)

                    }
                }

            }
        }
    }
</script>

<style>
    .container {
        padding: 20px;
        font-size: 14px;
        line-height: 24px;
    }

    .head-text {
        width: 100%;
        height: 80px;
        font-size: 20px;
        padding-left: 16px;
    }

    .van-cell {
        padding: 0 !important;
        height: 60px!important;
        line-height: 60px!important;
        border-bottom: 1px solid #ebedf0;
    }
    .van-field__body{
        height: 60px!important;
    }
</style>
