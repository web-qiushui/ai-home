<template>
    <view class="container">
        <view class="head">AI家顾问认证</view>
        <van-form>
            <van-field
                    v-model="form.name"
                    label="姓名"
                    placeholder="请输入姓名"
                    @input="fileInput('name',$event)"
                    :readonly="readonlystate"
            />
            <van-field
                    v-model="form.phone"
                    label="手机号码"
                    placeholder="请输入手机号码"
                    type="tel"
                    @input="fileInput('phone',$event)"
                    :readonly="readonlystate"
            />
            <van-field
                    v-model="form.yanZhengMa"
                    label="验证码"
                    type="number"
                    placeholder="请输入验证码"
                    @input="fileInput('yanZhengMa',$event)"
                    v-if="!readonlystate"
            >
                <template #button>
                    <van-button size="small" native-type="button" @click="getCaptcha">
                        <span v-if="cooldown">{{cooldownTime}}秒</span>
                        <span v-else>获取验证码</span>
                    </van-button>
                </template>
            </van-field>
            <van-field
                    v-model="form.idCard"
                    label="身份证"
                    placeholder="请输入身份证号码"
                    @input="fileInput('idCard',$event)"
                    :readonly="readonlystate"
            />
            <view class="head-text">个人简介</view>
            <view class="textarea-container">
                <van-field
                        v-model="form.message"
                        rows="2"
                        type="textarea"
                        placeholder="个人简介，参加过什么项目，服务过的客户等"
                        :border="false"
                        @input="fileInput('message',$event)"
                        :readonly="readonlystate"
                />
            </view>
            <view class="head-text">添加图片</view>
            <view class="picture">
                <view class="picture_item" v-if="imgUrl">
                    <image :src="imgUrl" mode="aspectFill" @tap="uploadheadimg()"></image>
                    <text class='del' @tap="deleteImg()" v-if="!readonlystate">×</text>
                </view>
                <view class="picture_item" @tap="uploadheadimg()" v-if="!readonlystate&&!imgUrl">
                    <view class="up-icon">
                        <image src="../../../static/img-add.png"></image>
                    </view>
                </view>
            </view>
            <view style="margin: 16px;">
                <van-button round block color="#C30D23" @click="onSubmit">下一步</van-button>
            </view>
        </van-form>
    </view>
</template>

<script>
    import config from '../../../common/config'
    import apiservice from "../../../api/apiservice";
    import util from '../../../common/util'

    export default {
        name: "certification",
        data() {
            return {
                imgUrl: null,
                form: {
                    name: '',
                    message: '',
                    phone: '',
                    yanZhengMa: '',
                    idCard: '',
                    avatar: '',
                    uuid: ''
                },
                readonlystate: false,
                readonlystate: false,
                authenticateStatus: null,
                cooldown: false,
                cooldownTime: 60,
                timeInterval: 0,

            }
        },
        created() {
            this.consultantuser();
        },
        onShow() {
            if (this.timeInterval) {
                clearInterval(this.timeInterval);
            }
        },
        onHide() {
            if (this.timeInterval) {
                clearInterval(this.timeInterval);
            }
        },
        onUnload() {
            if (this.timeInterval) {
                clearInterval(this.timeInterval);
            }
        },
        onLoad(options) {
            this.authenticateStatus = options.authenticateStatus;
            if (options.authenticateStatus && options.authenticateStatus === '1') {
                this.readonlystate = true;
            }
        },
        methods: {
            getCaptcha() {
                if (!this.form.phone) {
                    util.toast('请输入手机号码!');
                    return false;
                }
                if (!util.isMobile(this.form.phone)) {
                    util.toast('请输入正确的手机号码!');
                    return false;
                }
                this.guid();
                let parmes = {
                    phone: this.form.phone,
                    uuid: this.form.uuid,
                    type: 0
                };
                apiservice.sendcaptcha(parmes).then(() => {
                    this.cutdown();
                }, () => {
                    this.cutdown();
                })

            },
            cutdown() {
                if (!this.cooldown) {
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
            guid() {
                this.form.uuid = (this.S4() + this.S4() + "-" + this.S4() + "-" + this.S4() + "-" + this.S4() + "-" + this.S4() + this.S4() + this.S4());
            },
            S4() {
                return (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1);
            },
            fileInput(file, event) {
                if (file === 'suoZaiDi') {
                    this.form[file] = event.target.value.join(' ');
                } else {
                    this.form[file] = event.detail;
                }
            },
            uploadheadimg() {
                let that = this;
                wx.showActionSheet({
                    itemList: ['从相册中选择', '拍照'],
                    itemColor: '#C30D23',
                    success: function (res) {
                        if (!res.cancel) {
                            if (res.tapIndex === 0) {
                                that.chooseWxImageShop('album');
                            } else if (res.tapIndex === 1) {
                                that.chooseWxImageShop('camera');
                            }
                        }
                    },
                });
            },
            chooseWxImageShop(type) {
                let that = this;
                wx.chooseImage({
                    sizeType: ['original', 'compressed'],
                    sourceType: [type],
                    count: 1,
                    success: function (res) {
                        var tempFilePaths = res.tempFilePaths;
                        wx.uploadFile({
                            url: `${config.imageHost}/home-service/app/api/upload/images`,
                            header: {
                                'token': uni.getStorageSync('token'),
                            },
                            filePath: tempFilePaths[0],
                            name: 'file',
                            success: function (res) {
                                if (res) {
                                    let data = JSON.parse(res.data);
                                    that.imgUrl = config.imageHost + data.data;
                                    that.form.avatar = data.data;
                                }
                            },
                        });
                    },
                });
            },
            deleteImg() {
                this.imgUrl = null;
            },
            formCheck() {
                if (!this.form.name) {
                    util.toast('请输入姓名!');
                    return false;
                }
                if (!util.regName(this.form.name)) {
                    util.toast('请输入真实姓名!');
                    return false;
                }
                if (!this.form.phone) {
                    util.toast('请输入手机号码!');
                    return false;
                }
                if (!util.isMobile(this.form.phone)) {
                    util.toast('请输入正确的手机号码!');
                    return false;
                }
                if (!this.form.yanZhengMa) {
                    util.toast('请输入验证码!');
                    return false;
                }
                if (!this.form.idCard) {
                    util.toast('请输入身份证号码!');
                    return false;
                }
                if (!util.isIdcard(this.form.idCard)) {
                    util.toast('请输入正确的身份证号码!');
                    return false;
                }
                return true;
            },
            onSubmit() {
                if (this.readonlystate) {
                    uni.navigateTo({
                        url: `/newsPages/pages/person/projectcertification?authenticateStatus=${this.authenticateStatus}`
                    })
                } else {
                    if (this.formCheck()) {
                        util.showLoading('处理中...');
                        let phoneobj = {
                            phone: this.form.phone,
                            uuid: this.form.uuid,
                            code: this.form.yanZhengMa
                        };
                        apiservice.checkcaptcha(phoneobj).then((res) => {
                            let project = encodeURIComponent(JSON.stringify(this.form));
                            setTimeout(() => {
                                uni.navigateTo({
                                    url: `/newsPages/pages/person/projectcertification?project=${project}`
                                })
                            });
                        })

                    }
                }
            },
            consultantuser() {
                apiservice.getconsultantuser().then((res) => {
                    if (res) {
                        this.id = res.id;
                        this.imgUrl = config.imageHost + res.avatar;
                        this.form.name = res.name;
                        this.form.message = res.introduction;
                        this.form.idCard = res.idCard;
                        this.form.phone = res.phone;
                        this.form.avatar = res.avatar;
                    }
                })
            },
        }
    }
</script>

<style>
    .container {
        width: 100%;
        background: #F5F5F5;
        height: 100vh;
    }

    .head {
        height: 50px;
        line-height: 50px;
        padding-left: 15px;
        font-weight: bold;
    }

    .van-cell {
        border-bottom: 1px solid #F5F5F5 !important;
    }

    .van-cell::after {
        border-bottom: none !important;
    }

    .van-button--small {
        border: none !important;
        color: #C30D23 !important;
    }

    .textarea-container {
        border-top: 1px solid #F5F5F5;
    }

    .textarea-container .van-cell, textarea {
        height: 130px !important;
    }

    .head-text {
        height: 50px;
        line-height: 50px;
        padding-left: 15px;
        font-weight: bold;
        background: #ffffff;
    }

    .picture {
        width: 100%;
        display: flex;
        align-items: center;
        flex-wrap: wrap;
        padding: 10px;
        background: #fff;
        border-top: 1px solid #F5F5F5;
    }

    .picture_item {
        position: relative;
        width: 33.33%;
        height: 110px;
        padding: 5px;
    }

    .picture_item .del {
        position: absolute;
        top: -4px;
        right: -3px;
        color: #fff;
        border-radius: 50%;
        width: 20px;
        height: 20px;
        z-index: 2;
        line-height: 20px;
        text-align: center;
        background-color: #C30D23;
    }

    .picture_item image {
        width: 100%;
        height: 100%;
    }

    .up-icon {
        width: 100%;
        height: 100%;
        color: #ddd;
        font-size: 144px;
        border-radius: 10px;
        display: flex;
        align-items: center;
        justify-content: center;
        border: 1px solid #dddddd;
    }

    .up-icon image {
        width: 50px;
        height: 50px;
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

    .van-dropdown-menu__title--active {
        color: #C30D23 !important;
    }

    .van-dropdown-menu__title--down::after {
        color: #C30D23 !important;
    }

    .van-dropdown-item__option--active .van-dropdown-item__icon, .van-dropdown-item__option--active .van-dropdown-item__title {
        color: #C30D23 !important;
    }

    .van-dropdown-menu {
        width: 100px;
        height: 30px !important;
        border-radius: 5px;
    }

    .van-dropdown-item.van-dropdown-item--down {
        margin: 0 15px !important;
        box-shadow: 2px 2px 5px 2px #aaa;
        bottom: 50px;
    }

    .van-dropdown-menu__title {
        padding: 5px 15px 5px 5px !important;
        font-size: 12px !important;
    }

    .more-popup-container {
        padding: 20px;
        background-color: #ffffff;
    }

    .more-popup-container .select-item-box {
        background-color: #EEEEEE !important;
        padding: 5px 20px !important;
        border-radius: 4px !important;
    }

    .more-popup-container .select-item-box.selected {
        background-color: #C30D23 !important;
        color: #ffffff;
    }

    .more-item-label {
        margin: 5px 0;
        font-size: 15px;
        font-weight: bold;
    }

    .select-group-container {
        display: flex;
        flex-direction: row;
        flex-wrap: wrap;
    }

    .select-item-container {
        display: inline-block;
        margin-right: 5px;
        margin-bottom: 5px;
    }

    .select-tool-container {
        margin-top: 5px;
    }

    .tag {
        padding: 10px 16px;
        background: #ffffff;
        border-bottom: 1px solid #f5f5f5;
        font-size: 14px;
    }

    .tag-list {
        display: flex;
        align-items: center;
        flex-wrap: wrap;
    }

    .tag-item {
        width: 100px;
        height: 30px;
        position: relative;
        padding: 5px;
    }

    .tag-item view {
        height: 100%;
        background: #C30D23;
        color: #ffffff;
        display: flex;
        align-items: center;
        justify-content: center;
        border-radius: 5px;
    }

    .tag-item image {
        width: 15px;
        height: 15px;
        background: #FF0000;
        border-radius: 50%;
        position: absolute;
        right: 0;
        top: 0;
    }

    .tags-btn {
        height: 40px;
        display: flex;
        align-items: center;
        justify-content: space-between;
    }

    .tags-btn .btn {
        width: 45%;
        height: 30px;
        background: #eeeeee;
        border-radius: 5px;
        display: flex;
        align-items: center;
        justify-content: center;
    }

    .tags-btn .btn.active {
        color: #ffffff;
        background: #C30D23;
    }

    .tag > view:first-child {
        line-height: 40px;
    }

    .tag-list {
        display: flex;
        align-items: center;
        flex-wrap: wrap;
    }

    .tag-item {
        width: 100px;
        height: 30px;
        position: relative;
        padding: 5px;
    }

    .tag-item view {
        height: 100%;
        background: #C30D23;
        color: #ffffff;
        display: flex;
        align-items: center;
        justify-content: center;
        border-radius: 5px;
    }

    .tag-item image {
        width: 15px;
        height: 15px;
        background: #FF0000;
        border-radius: 50%;
        position: absolute;
        right: 0;
        top: 0;
    }
</style>
