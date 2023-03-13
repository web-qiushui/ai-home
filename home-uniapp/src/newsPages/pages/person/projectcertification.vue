<template>
    <view class="container" >
        <view class="head">所属项目</view>
        <van-form>
            <van-field
                    v-model="form.name"
                    label="项目名称"
                    placeholder="请输入项目名称"
                    @input="fileInput('name',$event)"
                    :readonly="readonlystate"
            />
            <picker mode="region" @change="fileInput('suoZaiDi',$event)" v-if="!readonlystate">
                <view class="picker-content">
                    <view class="picker-head">项目地址</view>
                    <view class="picker-body">{{form.suoZaiDi}}</view>
                </view>
            </picker>
            <view class="picker-content" v-if="readonlystate">
                <view class="picker-head">项目地址</view>
                <view class="picker-body">{{form.suoZaiDi}}</view>
            </view>
            <view class="picker-content" v-if="!readonlystate">
                <view class="picker-head">项目标签</view>
                <van-dropdown-menu class="filter-container" overlay="true" close-on-click-overlay="true"
                                   close-on-click-outside="true">
                    <van-dropdown-item id="more" popup-style="custom-popup" title="选择项目标签">
                        <view class="more-popup-container">
                            <view class="more-item-label">{{typesTitle}}</view>
                            <view class="select-group-container">
                                <view class="select-item-container" v-for="(item, index) in types" :key="index">
                                    <view class="select-item-box" :class="{'selected': item.selected}"
                                          @tap="select(item)">
                                        {{item.text}}
                                    </view>
                                </view>
                            </view>
                            <view class="more-item-label">{{othersTitle}}</view>
                            <view class="select-group-container">
                                <view class="select-item-container" v-for="(item, index) in others" :key="index">
                                    <view class="select-item-box" :class="{'selected': item.selected}"
                                          @tap="select(item)">
                                        {{item.text}}
                                    </view>
                                </view>
                            </view>
                            <view class="tags-btn">
                                <view class="btn" @tap="reset()">重置</view>
                                <view class="btn active" @tap="selectedValue()">确认</view>
                            </view>
                        </view>
                    </van-dropdown-item>
                </van-dropdown-menu>
            </view>
            <view class="tag" v-if="tagList.length>0&&!readonlystate">
                <view class="tag-list">
                    <view class="tag-item" v-for="(item,index) in tagList" :key="index" @click="deleteTag(index)">
                        <view>{{item}}</view>
                        <image src="../../../static/close.png"></image>
                    </view>
                </view>
            </view>
            <view v-if="readonlystate">
                <view class="picker-content">
                    <view class="picker-head">项目标签</view>
                </view>
                <view class="tag" v-if="tagList.length>0">
                    <view class="tag-list">
                        <view class="tag-item" v-for="(item,index) in tagList" :key="index">
                            <view>{{item}}</view>
                        </view>
                    </view>
                </view>
            </view>
            <view class="head-text">项目图片</view>
            <view class="picture">
                <view class="picture_item" v-if="projectImg">
                    <image :src="projectImg" mode="aspectFill" @tap="uploadheadimg('project')"></image>
                    <text class='del' @tap="deleteImg('project')" v-if="!readonlystate">×</text>
                </view>
                <view class="picture_item" @tap="uploadheadimg('project')" v-if="!readonlystate&&!projectImg">
                    <view class="up-icon">
                        <image src="../../../static/img-add.png"></image>
                    </view>
                </view>
            </view>
            <view style="margin: 16px;" v-if="!readonlystate">
                <van-button round block color="#C30D23" @click="onSubmit">提交修改</van-button>
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
                projectImg: null,
                projectImgUrl: '',
                form: {
                    message: '',
                    phone: '',
                    yanZhengMa: '',
                    idCard: '',
                    suoZaiDi: '',
                    name: '',
                    realName: '',
                    avatar: '',
                    uuid: ''
                },
                tagList: [],
                tagsList: [],
                typesTitle: '类型',
                types: [],
                othersTitle: '其他',
                others: [],
                labelsNum: null,
                tagListAnouncement: [],
                id: null,
                readonlystate: false,
            }
        },
        created() {
            this.consultantuser();
        },
        onShow() {
            this.getDataDict();
        },
        onLoad(options) {
            if (options.project) {
                let info = JSON.parse(decodeURIComponent(options.project));
                this.form.realName = info.name;
                this.form.message = info.message;
                this.form.idCard = info.idCard;
                this.form.phone = info.phone;
                this.form.avatar = info.avatar;
                this.form.yanZhengMa = info.yanZhengMa;
                this.form.uuid = info.uuid;
            }
            if (options.authenticateStatus && options.authenticateStatus === '1') {
                this.readonlystate = true;
            }
        },
        methods: {
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
                                    that.projectImg = config.imageHost + data.data;
                                    that.projectImgUrl = data.data;
                                }
                            },
                        });
                    },
                });
            },
            deleteImg() {
                this.projectImg = null;
            },
            formCheck() {
                if (!this.form.name) {
                    util.toast('请输入项目名称!');
                    return false;
                }
                if (!this.form.suoZaiDi) {
                    util.toast('请选择项目地址!');
                    return false;
                }
                return true;
            },
            onSubmit() {
                if (this.formCheck()) {
                    util.showLoading('提交中...');
                    let result = {
                        id: this.id,
                        avatar: this.form.avatar,
                        idCard: this.form.idCard,
                        name: this.form.realName,
                        phone: this.form.phone,
                        introduction: this.form.message,
                        code:this.form.yanZhengMa,
                        uuid:this.form.uuid,
                        type: 2,
                        project: {
                            address: this.form.suoZaiDi,
                            imagesUrl: this.projectImgUrl,
                            label: this.tagList,
                            name: this.form.name,
                        }
                    };
                    apiservice.postsaveconsultant(result).then((res) => {
                        util.hideLoading();
                        uni.switchTab({
                            url: '/pages/me/me'
                        });
                    })
                }
            },
            consultantuser() {
                apiservice.getconsultantuser().then((res) => {
                    if (res) {
                        this.id = res.id;
                        if (res.project) {
                            this.projectImg = config.imageHost + res.project.imagesUrl;
                            this.projectImgUrl = res.project.imagesUrl;
                            this.form.name = res.project.name;
                            this.form.suoZaiDi = res.project.address;
                            this.tagList = res.project.label;
                            this.tagListAnouncement = [].concat(this.tagList);
                            this.typeSelect();
                        }
                    }
                })
            },
            getDataDict() {
                let list = uni.getStorageSync('dataDict');
                if (list) {
                    this.labelsNum = list.filter(item => item.type === 'LABEL_SIZE')[0].value;
                    this.initData(list);
                }
            },
            initData(list) {
                this.tagsList = list;
                this.types = this.get(list, 'TYPE_SELECT');
                this.others = this.get(list, 'OTHER_SELECT');
            },
            get(list, key) {
                return list.filter(item => item.n1 === key).map(item => {
                    return {
                        id: item.id,
                        text: item.name,
                        value: item.id,
                        selected: this.tagListAnouncement.indexOf(item.name) > -1
                    }
                })
            },
            select(item) {
                let isMax = this.tagList.length >= (+this.labelsNum);
                if (!item.selected && isMax) {
                    util.toast('标签选择数量不能超过' + this.labelsNum);
                }
                if (item.selected || isMax) {
                    item.selected = false;
                } else {
                    item.selected = true;
                }
                let index = this.tagList.indexOf(item.text);
                if (index > -1 && !item.selected) {
                    this.tagList.splice(index, 1);
                    this.typeSelect();
                }
                if (index === -1 && item.selected) {
                    this.tagList = this.tagList.concat(item.text);
                    this.typeSelect();
                }
            },
            typeSelect() {
                this.types.forEach((item) => {
                    item.selected = this.tagList.indexOf(item.text) > -1;
                });
                this.others.forEach((item) => {
                    item.selected = this.tagList.indexOf(item.text) > -1;
                });
            },
            deleteTag(index) {
                this.tagList.forEach((item, i) => {
                    if (index === i) {
                        this.tagList.splice(index, 1);
                    }
                });
                this.typeSelect();
            },
            selectedValue() {
                this.selectComponent('#more').toggle();
            },
            reset() {
                this.tagList = [].concat(this.tagListAnouncement);
                this.types = this.get(this.tagsList, 'TYPE_SELECT');
                this.others = this.get(this.tagsList, 'OTHER_SELECT');
            },
        }
    }
</script>

<style>
    page{
        height: 100vh;
    }
    .container {
        width: 100%;
        background: #F5F5F5;
        height: 100%;
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

    .van-dropdown-menu__title {
        padding: 5px 15px 5px 5px !important;
        font-size: 12px !important;
    }

    .more-popup-container {
        padding: 20px 20px 60px 20px;
        background-color: #ffffff;
    }

    .more-popup-container .select-item-box {
        background-color: #EEEEEE !important;
        padding: 4px 20px !important;
        border-radius: 4px !important;
        font-size: 12px;
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
