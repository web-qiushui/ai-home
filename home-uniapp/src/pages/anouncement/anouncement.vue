<template>
    <scroll-view class="container" scroll-x="false" scroll-y="true">
        <view class="inner-container" :class="{'lock-scroll': locked}">
            <view class="head">
                <image src="../../static/icon-kefu.png"></image>
                <view class="head-text">
                    <view>{{topDescription?topDescription:'我是AI君，请问您需要什么样的房子？'}}</view>
                    <image src="../../static/icon-triangle.png"></image>
                </view>
            </view>
            <view class="head-num">{{left}}<span>{{baseCount}}</span>{{right}}</view>
            <view class="content">
                <view class="input">
                    <van-cell-group>
                        <van-field v-model="phone" :border="false" label="联系电话" placeholder="为了更好的为您服务，请确保证联系方式无误"
                                @input="phoneInput"/>
                    </van-cell-group>
                </view>
                <view class="menu-content">
                    <view class="menu-item">
                        <view>区域</view>
                        <van-dropdown-menu class="filter-container" overlay="true" close-on-click-overlay="true"
                                        close-on-click-outside="true">
                            <van-dropdown-item :value="filter.regionId" :options="regionOption" @change="onRegionChange"
                                            @open="open" @close="close"
                                            :title="filter.regionTitle" popup-style="custom-popup"/>
                        </van-dropdown-menu>
                    </view>
                    <view class="menu-item">
                        <view>面积</view>
                        <van-dropdown-menu class="filter-container" overlay="true" close-on-click-overlay="true"
                                        close-on-click-outside="true">
                            <van-dropdown-item :value="filter.areaId" :options="areaOption" @change="onAreaChange"
                                            @open="open" @close="close"
                                            :title="filter.areaTitle" popup-style="custom-popup"/>
                        </van-dropdown-menu>
                    </view>
                </view>
                <view class="menu-content">
                    <view class="menu-item">
                        <view>价格</view>
                        <van-dropdown-menu class="filter-container" overlay="true" close-on-click-overlay="true"
                                        close-on-click-outside="true">
                            <van-dropdown-item class="price-dropdown-item" :value="filter.priceId" :options="priceOption" @change="onPriceChange"
                                            @open="open" @close="close"
                                            :title="filter.priceTitle" popup-style="custom-popup"/>
                        </van-dropdown-menu>
                    </view>
                    <view class="menu-item">
                        <view>更多</view>
                        <van-dropdown-menu class="filter-container" overlay="true" close-on-click-overlay="true"
                                        close-on-click-outside="true" lock-scroll="false">
                            <van-dropdown-item id="more" popup-style="custom-popup" title="选择" @open="open" @close="close">
                                <view class="more-popup-container">
                                    <view class="more-item-label">{{unitTypesTitle}}</view>
                                    <view class="select-group-container">
                                        <view class="select-item-container" v-for="(item, index) in unitTypes" :key="index">
                                            <view class="select-item-box" :class="{'selected': item.selected}" @tap="select(item)">{{item.text}}</view>
                                        </view>
                                    </view>
                                    <view class="more-item-label">{{typesTitle}}</view>
                                    <view class="select-group-container">
                                        <view class="select-item-container" v-for="(item, index) in types" :key="index">
                                            <view class="select-item-box" :class="{'selected': item.selected}" @tap="select(item)">{{item.text}}</view>
                                        </view>
                                    </view>
                                    <view class="more-item-label">{{othersTitle}}</view>
                                    <view class="select-group-container">
                                        <view class="select-item-container" v-for="(item, index) in others" :key="index">
                                            <view class="select-item-box" :class="{'selected': item.selected}" @tap="select(item)">{{item.text}}</view>
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
                </view>
                <view class="tag">
                    <view>标签</view>
                    <view class="tag-list">
                        <view class="tag-item" v-for="(item,index) in tagList" :key="index" @click="deleteTag(index)">
                            <view>{{item}}</view>
                            <image src="../../static/close.png"></image>
                        </view>
                    </view>
                </view>
                <view class="explain">
                    <view>其他说明</view>
                </view>
                <view v-if="showmodal" class="modal">
                    <van-overlay :show="showmodal" z-index="99999">
                        <view class="wrapper" @click.stop="showPopup">
                            <view class="block">
                                <image :src="imgUrl"></image>
                                <image src="../../static/icon-close.png"></image>
                            </view>
                        </view>
                    </van-overlay>
                </view>
                <view class="textarea-container">
                    <textarea v-model="message" placeholder="补充需求"></textarea>
                </view>
                <view class="btns">
                    <van-button color="#C30D23" round size="large" block @click="anouncement">发布购房需求</van-button>
                </view>
            </view>
        </view>
    </scroll-view>
</template>

<script>
    import apiservice from '../../api/apiservice';
    import util from '../../common/util';
    import config from '../../common/config'

    export default {
        data() {
            return {
                filter: {
                    regionTitle: '不限',
                    regionId: 0,
                    region: '',
                    priceTitle: '不限',
                    priceId: 0,
                    price: '',
                    areaTitle: '不限',
                    areaId: 0,
                    area: '',
                },
                regionOption: [],
                priceOption: [],
                areaOption: [],
                moreTitle: '更多',
                unitTypesTitle: '户型',
                unitTypes: [],
                typesTitle: '类型',
                types: [],
                othersTitle: '其他',
                others: [],
                selectedList: [],
                phone: '',
                message: '',
                showmodal: false,
                tagList: [],
                tagsList: [],
                topDescription: '',
                id: null,
                labelsNum: null,
                anouncementdetail: null,
                tagListAnouncement: [],
                locked: false,
                baseCount: 0,
                left: '',
                right: '',
                showTextArea: false,
                Focus: false

            }
        },
        created() {
            this.getDataDict();
            this.releaseconfig();
        },
        onShow() {
            this.setTabBarIndex(1);
            this.anouncementdetail = uni.getStorageSync('anouncementdetail');
            if (this.anouncementdetail) {
                this.id = this.anouncementdetail.id;
                this.phone = this.anouncementdetail.phone;
                this.message = this.anouncementdetail.explanation;
                this.tagList = this.anouncementdetail.label;
                this.tagListAnouncement = [].concat(this.tagList);
                this.$set(this.filter, 'regionTitle', this.anouncementdetail.region);
                this.$set(this.filter, 'priceTitle', this.anouncementdetail.price);
                this.$set(this.filter, 'areaTitle', this.anouncementdetail.area);
                this.typeSelect();
            }
            let dataDict = uni.getStorageSync('dataDict');
            this.labelsNum = dataDict.filter(item => item.type === 'LABEL_SIZE')[0].value;
        },
        onLoad() {
        },
        onHide() {
            this.resetPage();

        },
        onUnload() {
            this.resetPage();
        },
        methods: {
            isfouce() {
                this.showTextArea = true;
            },
            getout() {
                this.showTextArea = false;
                this.Focus = true;
            },
            open() {
                this.locked = true;
            },
            close() {
                this.locked = false;
            },
            releaseconfig() {
                apiservice.getreleaseconfig().then((res) => {
                    if (res) {
                        this.topDescription = res.topDescription;
                        this.baseCount = res.baseCount;
                        if (res.inDescription) {
                            let index = res.inDescription.indexOf('$');
                            let lastIndex = res.inDescription.lastIndexOf('$');
                            this.left = res.inDescription.substring(0, index);
                            this.right = res.inDescription.substring(lastIndex + 1);
                        }
                    }
                })
            },
            phoneInput(e) {
                this.phone = e.detail;
            },
            fileInput(e) {
                this.message = e.detail;
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
                this.unitTypes.forEach((item) => {
                    item.selected = this.tagList.indexOf(item.text) > -1;
                });
                this.types.forEach((item) => {
                    item.selected = this.tagList.indexOf(item.text) > -1;
                });
                this.others.forEach((item) => {
                    item.selected = this.tagList.indexOf(item.text) > -1;
                });
            },
            showPopup() {
                this.showmodal = !this.showmodal;
            },
            anouncement() {
                util.showLoading('发布中...');
                let demend = {
                    area: this.filter.areaTitle,
                    asc: [],
                    desc: [],
                    id: this.id,
                    explanation: this.message,
                    label: this.tagList,
                    page: 1,
                    size: 10,
                    phone: this.phone,
                    price: this.filter.priceTitle,
                    region: this.filter.regionTitle,
                };
                apiservice.postSaveDemand(demend).then((res) => {
                    util.hideLoading();
                    this.resetPage();
                    setTimeout(() => {
                        this.saveAIBuddy(res)
                    }, 100)
                })

            },
            deleteTag(index) {
                this.tagList.forEach((item, i) => {
                    if (index === i) {
                        this.tagList.splice(index, 1);
                    }
                });
                this.typeSelect();
            },
            getDataDict() {
                let list = uni.getStorageSync('dataDict');
                if (list) {
                    this.initData(list);
                }
            },
            initData(list) {
                this.tagsList = list;
                this.regionOption = this.get(list, 'REGION_SELECT');
                this.priceOption = this.get(list, 'PRICE_SELECT');
                this.areaOption = this.get(list, 'AREA_SELECT');
                this.unitTypes = this.get(list, 'UNIT_TYPE_SELECT');
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
            onRegionChange(e) {
                const item = this.regionOption.find(item => item.id === e.detail);
                if (item) {
                    this.$set(this.filter, 'regionId', item.id);
                    this.$set(this.filter, 'region', item.text);
                    this.$set(this.filter, 'regionTitle', item.text);
                }
            },
            onPriceChange(e) {
                const item = this.priceOption.find(item => item.id === e.detail);
                if (item) {
                    this.$set(this.filter, 'priceId', item.id);
                    this.$set(this.filter, 'price', item.text);
                    this.$set(this.filter, 'priceTitle', item.text);
                }
            },
            onAreaChange(e) {
                const item = this.areaOption.find(item => item.id === e.detail);
                if (item) {
                    this.$set(this.filter, 'areaId', item.id);
                    this.$set(this.filter, 'area', item.text);
                    this.$set(this.filter, 'areaTitle', item.text);
                }
            },
            getSelectedList() {
                const unitTypes = this.unitTypes.filter(item => item.selected).map(item => item.id);
                const types = this.types.filter(item => item.selected).map(item => item.id);
                const others = this.others.filter(item => item.selected).map(item => item.id);
                return [...unitTypes, ...types, ...others].join(',')
            },
            selectedValue() {
                this.selectComponent('#more').toggle();
            },
            reset() {
                this.tagList = [].concat(this.tagListAnouncement);
                this.unitTypes = this.get(this.tagsList, 'UNIT_TYPE_SELECT');
                this.types = this.get(this.tagsList, 'TYPE_SELECT');
                this.others = this.get(this.tagsList, 'OTHER_SELECT');
            },
            resetPage() {
                this.phone = '';
                this.tagList = [];
                this.tagListAnouncement = [];
                this.message = '';
                this.id = null;
                // this.$set(this.filter, 'regionTitle', '不限');
                // this.$set(this.filter, 'priceTitle', '不限');
                // this.$set(this.filter, 'areaTitle', '不限');
                // this.$set(this.filter, 'region', '');
                // this.$set(this.filter, 'price', '');
                // this.$set(this.filter, 'area', '');
                this.filter = {
                    regionTitle: '不限',
                    regionId: 0,
                    region: '',
                    priceTitle: '不限',
                    priceId: 0,
                    price: '',
                    areaTitle: '不限',
                    areaId: 0,
                    area: '',
                }
                this.reset();
                uni.removeStorageSync('anouncementdetail');
            },
            saveAIBuddy(demend) {
                let data = {
                    demandId: demend.id
                };
                apiservice.saveAIBuddy(data).then(data => {
                    this.gotoConversation(data, demend)
                })
            },
            gotoConversation(conversation, demend) {
                this.$store.dispatch('message/chat', {
                    consultantUserId: conversation.id,
                    page: 1,
                    size: 10
                });
                const query = encodeURIComponent(JSON.stringify(conversation));
                const params = encodeURIComponent(JSON.stringify({
                    type: 'demend',
                    data: demend
                }));
                uni.navigateTo({
                    url: `/pages/message/conversation?query=${query}&params=${params}`
                });
            },
        }
    }
</script>

<style>
    page {
        height: 100vh;
    }

    .container {
        height: calc(100vh - 50px);
        font-size: 14px;
        line-height: 24px;
        background: #F2F3F7;
    }

    .inner-container.lock-scroll {
        height: calc(100vh - 50px);
        overflow: hidden;
    }

    .head {
        height: 90px;
        display: flex;
        padding: 10px 15px;
        background: #C30D23;
        align-items: center;
    }

    .head image {
        width: 50px;
        height: 50px;
        margin-right: 20px;
        border-radius: 50%;
    }

    .head-text {
        width: calc(100% - 85px);
        display: flex;
        flex-wrap: wrap;
        background: #ffffff;
        border-radius: 5px;
        padding: 5px 10px;
        color: #C30D23;
        position: relative;
        height: 35px;
        align-items: center;
    }

    .head-text image {
        width: 20px;
        height: 20px;
        position: absolute;
        top: 0;
        bottom: 0;
        left: -14px;
        margin: auto;

    }

    .content {
        padding: 0 15px;
    }

    .head-num {
        padding: 0 15px;
        line-height: 50px;
        background: #ffffff;
        font-size: 15px;
        font-weight: bold;
    }

    .head-num span {
        color: #C30D23
    }

    .input .van-cell {
        height: 50px;
        line-height: 50px;
        padding: 0 !important;
        border-bottom: 1px solid #C7C7C7;
        background: #F2F3F7;
    }

    .input .van-cell__title {
        line-height: 50px;
    }

    .input .van-field__body {
        height: 50px;
    }

    .input .van-field__input {
        font-size: 12px;
    }

    .menu-content {
        display: flex;
        align-items: center;
        justify-content: space-between;
    }

    .menu-item {
        width: 50%;
        height: 50px;
        border-bottom: 1px solid #C7C7C7;
        display: flex;
        align-items: center;
    }

    .menu-item > view {
        width: 50px;
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

    .van-dropdown-item {
        height: 550rpx;
    }

    .van-dropdown-menu__title {
        padding: 5px 15px 5px 5px !important;
        font-size: 12px !important;
    }

    .more-popup-container {
        padding: 20px 20px 60px 20px;
        background-color: #ffffff;
    }

    .more-item-label {
        margin: 5px 0;
        font-size: 15px;
        font-weight: bold;
    }

    .more-popup-container .select-group-container {
        display: flex;
        flex-direction: row;
        flex-wrap: wrap;
    }

    .more-popup-container .select-group-container .select-item-container {
        display: inline-block;
        margin-right: 5px;
        margin-bottom: 5px;
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
    .select-tool-container {
        margin-top: 5px;
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

    .explain {
        height: 40px;
        display: flex;
        align-items: center;
        justify-content: space-between;
    }

    .modal .wrapper {
        display: flex;
        align-items: center;
        justify-content: center;
        height: 100%;
    }

    .modal .block {
        width: 75%;
        height: 400px;
        background: #ffffff;
        border-radius: 5px;
        position: relative;
    }

    .modal .block image:first-child {
        width: 100%;
        height: 100%;
        border-radius: 5px;
    }

    .modal .block image:last-child {
        width: 30px;
        height: 30px;
        position: absolute;
        bottom: -45px;
        right: 0;
        left: 0;
        margin: auto;
    }

    .block-head {
        line-height: 50px;
        font-size: 18px;
        font-weight: bold;
        color: #C30D23;
        text-align: center;
    }

    .block-title {
        line-height: 40px;
        font-size: 15px;
        font-weight: bold;
    }

    .foot-title {
        font-size: 15px;
        color: #C30D23;
    }

    .title-item view {
        line-height: 25px;
        font-size: 12px;
    }

    .close-img {
        width: 30px;
        height: 30px;
        position: absolute;
        top: 0;
        right: 0;
    }

    .explain-btn {
        display: flex;
        align-items: center;
        color: #C30D23;
    }

    .explain-btn image {
        width: 18px;
        height: 18px;
    }

    .textarea-container {
        height: 120px;
        background: #ffffff;
        padding: 10px;
    }

    .textarea-container .van-cell, textarea, .text {
        width: 100%;
        height: 100%;
        padding: 0 !important;
        overflow-y: auto;
    }

    .btns {
        margin: 20px 0;
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
</style>
