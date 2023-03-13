<template>
    <view class="container">
        <van-form>
            <van-field
                    v-model="form.nickName"
                    label="昵称"
                    placeholder="请输入昵称"
                    @input="fileInput('nickName',$event)"
            />
            <view class="picker-content" @click="selectXingBie()">
                <view class="picker-head">性别</view>
                <view class="picker-body">{{form.gender}}</view>
            </view>
            <van-field
                    v-model="form.phone"
                    label="手机号码"
                    placeholder="请输入手机号码"
                    type="tel"
                    @input="fileInput('phone',$event)"
            />
            <van-field
                    v-model="form.idCard"
                    label="身份证"
                    placeholder="请输入身份证号码"
                    @input="fileInput('idCard',$event)"
            />
            <picker mode="region" @change="fileInput('suoZaiDi',$event)">
                <view class="picker-content">
                    <view class="picker-head">项目地址</view>
                    <view class="picker-body">{{form.suoZaiDi}}</view>
                </view>
            </picker>
            <view style="margin: 16px;">
                <van-button round block color="#C30D23" @click="onSubmit">保存信息</van-button>
            </view>
        </van-form>
    </view>
</template>

<script>
    import util from '../../../common/util'
    import apiservice from "../../../api/apiservice";

    export default {
        name: "modifyinfo",
        data() {
            return {
                form: {
                    nickName: '',
                    phone: '',
                    idCard: '',
                    suoZaiDi: '',
                    id: '',
                    gender: ''
                }
            }
        },
        onLoad(options) {
            if (options.userinfo) {
                let userinfo = JSON.parse(decodeURIComponent(options.userinfo));
                this.form.nickName = userinfo.nickName;
                this.form.phone = userinfo.phone;
                this.form.idCard = userinfo.idCard;
                this.form.suoZaiDi = userinfo.address;
                this.form.id = userinfo.id;
                this.form.gender = userinfo.gender ? userinfo.gender : '';
            }
        },
        methods: {
            selectXingBie() {
                let self = this;
                let xingBie = ['男', '女'];
                uni.showActionSheet({
                    itemList: xingBie,
                    success(res) {
                        self.form.gender = xingBie[res.tapIndex];
                    },
                });
            },
            fileInput(file, event) {
                if (file === 'suoZaiDi') {
                    this.form[file] = event.target.value.join(' ');
                } else {
                    this.form[file] = event.detail;
                }
            },
            onSubmit() {
                if (this.form.phone && !util.isMobile(this.form.phone)) {
                    util.toast('请输入正确的手机号码!');
                    return false;
                }
                if (this.form.idCard && !util.isIdcard(this.form.idCard)) {
                    util.toast('请输入正确的身份证号码!');
                    return false;
                }
                util.showLoading('提交中...');
                let info = {
                    id: this.form.id,
                    address: this.form.suoZaiDi,
                    idCard: this.form.idCard,
                    nickName: this.form.nickName,
                    phone: this.form.phone,
                    gender: this.form.gender ? this.form.gender === '男' ? 1 : 2 : 0
                };
                let pages = getCurrentPages();
                let beforePage=pages[pages.length-2];
                apiservice.updateuserinfo(info).then((res) => {
                    setTimeout(() => {
                        util.hideLoading();
                        beforePage.$vm.getUserInfo();
                        uni.navigateBack()
                    }, 500)
                });
            }
        }
    }
</script>

<style>
    .container {
        width: 100%;
        background: #F5F5F5;
        height: 100vh;
    }

    .picker-content {
        padding: 10px 16px;
        background: #ffffff;
        border-bottom: 1px solid #f5f5f5;
        font-size: 14px;
        line-height: 24px;
        display: flex;
        align-items: center;
    }

    .picker-head {
        min-width: 90px;
        max-width: 90px;
        flex: 1;
    }

    .picker-body {
        flex: 1;
    }

</style>
