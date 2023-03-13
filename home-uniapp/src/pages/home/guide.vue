<template>
    <view class="container">
        <swiper autoplay="true" indicator-dots="true">
            <swiper-item v-for="(item, index) in swiper.list" :key="index">
                <van-image width="100%" height="100%" fit="cover" :src="$imageHost + item.imageUrl"/>
            </swiper-item>
        </swiper>
        <view class="search-bar-container" id="search" :class="{'guide-active': currentStep.type === 'SEARCH'}" @tap="next(step)">
            <view class="search-bar">
                <van-search
                    :value="keywords"
                    shape="round"
                    background="#fff"
                    placeholder="查找我的意向楼盘测评"
                    use-left-icon-slot="true"
                >
                    <van-icon slot="left-icon" class="search-bar-icon" name="search"/>
                </van-search>
            </view>
        </view>
        <view class="navigation-bar-container" id="information" :class="{'guide-active': currentStep.type === 'INFORMATION'}" @tap="next(step)">
            <view class="navigation-bar">
            <van-grid column-num="3" :border="false">
                <van-grid-item v-for="(bar, index) in bars" :key="index"
                               use-slot
                               :text="bar.name"
                               link-type="navigateTo"
                               custom-class="custom-grid-item"
                               content-class="custom-grid-item-content">
                    <image class="navigation-image"
                            :src="$imageHost + bar.imagesUrl"
                    />
                    <view class="navigater-text">{{bar.name}}</view>
                </van-grid-item>
            </van-grid>
            </view>
        </view>
        <view class="select-tool-container" id="filter" :class="{'guide-active': currentStep.type === 'FILTER'}" @tap="next(step)">
            <view class="filter-content">
                <assessment-filter disabled="true"></assessment-filter>
            </view>
            <view class="sort">
                <assessment-sort showMore="true"></assessment-sort>
            </view>
        </view>
        <view class="evaluation-list-container" id="evaluation" :class="{'guide-active': currentStep.type === 'EVALUATION'}" @tap="next(step)">
            <view v-for="(item, index) in datalist" :key="index" class="evaluation-card">
                <van-image class="evaluation-img" width="100%" height="150" fit="cover" :src="$imageHost + item.mainImage"/>
                <view class="evaluation-text">{{item.title}}</view>
                <view class="tag-container tag-container-flex">
                    <view class="tag-item" :class="{'last': tagIndex == item.tags.length - 1}" v-for="(tag, tagIndex) in item.tags" :key="tagIndex">
                        <text>{{tag}}</text>
                    </view>
                </view>
            </view>
        </view>
        <view class="guide-tabbar" id="navigation" @tap="next(step)"
             :class="{'guide-active home': currentStep.type === 'NAVIGATION',
                      'guide-active anou': currentStep.type === 'NAVIGATION_ANOU',
                      'guide-active msg': currentStep.type === 'NAVIGATION_MSG'}">
            <guide-tab-bar disabled="true" unsafe="true"></guide-tab-bar>
        </view>
        <view class="guide-container" v-if="guide.show">
            <view class="guide-header" @tap="quit()">
                <view class="quit-button">点我退出</view>
            </view>
            <view class="guide-content" v-if="currentStep" @tap="next(step)">
                <view class="guide-tips" :class="currentStep.type" :style="currentStep.style">
                    <view class="guide-bubble" v-if="currentStep.isShow">
                        <view class="guide-bubble-top" v-if="currentStep.isTop"></view>
                        <view class="guide-bubble-content">{{currentStep.explanation}}</view>
                        <view class="guide-bubble-bottom" v-if="!currentStep.isTop"></view>
                    </view>
                </view>
            </view>
        </view>
    </view>
</template>

<script>
    import Dialog from '../../wxcomponents/@vant/weapp/dialog/dialog'
    import apiservice from '../../api/apiservice'

    export default {
        data() {
            return {
                swiper: {
                    list: []
                },
                guide: {
                    list: [],
                    show: false
                },
                keywords: '',
                bars: [],
                params: {
                    // desc: null,
                    // asc: null,
                    // label: null,
                    page: 1,
                    size: 5,
                    search: '',
                    region: '',
                    price: '',
                    area: ''
                },
                datalist: [],
                list: [],
                step: 0,
                currentStep: {
                    explanation: '',
                    type: '',
                    style: '',
                    isTop: false,
                    isShow: false
                },
                $imageHost: this.$imageHost
            }
        },
        created() {
            this.getSwipers()
            this.getBars()
            this.queryList(this.params);
            this.initGuideConfig();
        },
        onShow() {
            console.log('showShow')
            this.setTabBarIndex(0)
        },
        methods: {
            goTo(bar) {
                uni.navigateTo({
                    url: bar.url,
                })
            },
            queryList(params) {
                const that = this
                apiservice.getMainPageInfo(params).then(data => {
                    console.log(data)
                    const list = data.list.map(item => {
                        try {
                            item.tags = item.label
                        } catch (e) {
                            item.tags = [];
                        }
                        return item;
                    });
                    that.datalist = [list[0]];
                })
            },
            getSwipers() {
                const that = this;
                const swipers = uni.getStorageSync('swipers')

                if (swipers) {
                    that.swiper.list = swipers;
                } else {
                    apiservice.getCarouselList().then(data => {
                        that.swiper.list = data;
                    })
                }
            },
            getBars() {
                const that = this;
                const kingkong = uni.getStorageSync('kingkong')
                if (kingkong) {
                    that.initBar(kingkong)
                } else {
                    apiservice.getKingKongConfig().then(data => {
                        console.log('king kong config', data)
                        that.initBar(data)
                    })
                }
            },
            initBar(list) {
                const MAP = {
                    'REGISTERING': '/newsPages/pages/kingkongs/registering',
                    'PRE_SALE': '/newsPages/pages/kingkongs/presale',
                    '24H_NEWS': '/newsPages/pages/kingkongs/news',
                }
                const bars = list.map(item => {
                    return {...item, url: MAP[item.type]}
                })

                this.bars = this.bars.concat(bars);
            },
            initGuideConfig() {
                const that = this;
                const guideConfig = uni.getStorageSync('guideConfig');
                if (!guideConfig) {
                    apiservice.getNewComerGuideConfig().then(data => {
                        console.log('guide config', data);
                        this.guide.list = data;
                        that.initGuideInfo()
                    })
                } else {
                    this.guide.list = guideConfig;
                    that.initGuideInfo()
                }
            },
            initGuideInfo(list) {
                this.list = this.guide.list.map(guide => {
                    return {
                        ...guide,
                        isShow: false,
                        hasShow: false
                    }
                })
                this.step = 0

                // start
                this.startGuide()
                this.show()
            },
            startGuide() {
                this.$set(this.guide, 'show', true);
            },
            quit() {
                this.$set(this.guide, 'show', false)
                uni.setStorageSync('hasShowGuide', true)
                this.toHomePage()
            },
            show() {
                if (this.list && this.list[this.step]) {
                    const type = this.list[this.step].type;
                    this.updatePosition(type);

                } else {
                    this.quit()
                }
            },
            updateContext() {
                this.$set(this.list[this.step], 'isShow', true)
                this.$set(this.list[this.step], 'hasShow', true)
                this.$set(this.currentStep, 'explanation', this.list[this.step].explanation)
                this.$set(this.currentStep, 'type', this.list[this.step].type)
                this.$set(this.currentStep, 'isShow', true)
            },
            next(step) {
                this.$set(this.currentStep, 'isShow', false)
                this.step++;
                this.show()
                return false
            },
            toHomePage() {
                uni.switchTab({
                    url: '/pages/home/home'
                })
            },
            updateBubblePosition(id, isTop, fixed) {
                const query = uni.createSelectorQuery()
                const that = this;
                query.select(id).boundingClientRect();
                query.exec(res => {
                    // const top = isTop ? (res[0].bottom - 20) : (res[0].top + 20);
                    if (res && res[0]) {
                        const fixedGap = 15;
                        const top = res[0].top;
                        const bottom = res[0].bottom;
                        const height = 50;
                        let style = ''
                        if (!fixed) {
                            style = isTop ? `top: ${bottom + fixedGap}px` : `bottom: calc(100% - ${top - fixedGap}px)`
                        } else {
                            const windowHeight = uni.getSystemInfoSync().windowHeight;
                            style = isTop ? `top: ${bottom}px` : `bottom: ${height + fixedGap}px`
                        }
                        that.$set(this.currentStep, 'style', style)
                        that.$set(this.currentStep, 'isTop', isTop)
                        console.log(`${id} style`, style, isTop, res)
                    }
                    this.updateContext()
                })
            },
            updatePosition(type) {
                let id = ''
                let isTop = true
                let fixed = 0
                switch (type) {
                    case 'SEARCH':
                        id = '#search';
                        isTop = true;
                        fixed = false
                        break;
                    case 'INFORMATION':
                        id = '#information';
                        isTop = true;
                        fixed = false
                        break;
                    case 'FILTER':
                        id = '#filter';
                        isTop = true;
                        fixed = false
                        break;
                    case 'EVALUATION':
                        id = '#evaluation';
                        isTop = false;
                        fixed = false
                        break;
                    case 'NAVIGATION':
                    case 'NAVIGATION_ANOU':
                    case 'NAVIGATION_MSG':
                        id = '#navigation';
                        isTop = false;
                        fixed = true
                        break;
                    defulat:
                        break;
                }
                this.updateBubblePosition(id, isTop, fixed);
            }
        }
    }
</script>

<style lang="scss" scoped>
    .container {
        padding: 0;
        font-size: 25rpx;
        line-height: 24rpx;
        background-color: #efefef;
        margin-bottom: 50px;
        color: #565656;

        swiper {
            height: 360rpx;
        }
    }

    .search-bar-container {
        height: 110rpx;
        margin-bottom: 0;
        background-color: #fff;

        .search-bar {
            .search-bar-icon {
                color: $basic-color;
                font-size: 35rpx;
                margin-left: 10rpx;
                margin-right: 10rpx;

            }
        }
    }

    .navigation-bar-container {
        margin-top: 20rpx;
        background-color: #fff;

        .navigation-bar {
            padding-left: 15rpx;
            padding-right: 15rpx;
        }

        .navigation-image {
            width: 100%;
            height: 150rpx;
            border-radius: 20rpx;
            background-color: $basic-color;
        }

        .navigater-text {
            width: 200rpx;
            height: 50rpx;
            position: absolute;
            left: 0;
            top: 0;
            right: 0;
            bottom: 0;
            margin: auto;
            color: #ffffff;
            text-align: center;
            line-height: 50rpx;
            font-size: $basic-title-font-size;
        }

        .custom-grid-item-content {
            padding: 30rpx 15rpx !important;
        }
    }

    .select-tool-container {
        margin-top: 20rpx;
        background-color: #fff;

        .filter-content {
            width: 100%;
            background-color: #fff;

            .filter-container {
                padding-left: 25rpx;
                padding-right: 25rpx;

                .filter-bar {

                    .van-dropdown-menu__title {
                        background-color: #efefef !important;
                        padding: 10rpx 70rpx 10rpx 45rpx !important;
                        border-radius: 20rpx;
                        font-size: 25rpx !important;
                        color: #565656;
                    }

                    .van-dropdown-menu__title::after {
                        right: 45rpx !important;
                    }
                }
            }
        }
    }

    .evaluation-list-container {
        background-color: #fff;

        .evaluation-card {
            position: relative;
            padding: 30rpx;
            border-bottom: 1px solid #efefef;

            .evaluation-img {
                filter: brightness(0.8);

                .van-image {
                    .van-image__img {
                        border-radius: 10rpx;
                    }
                    .van-image__error {
                        border-radius: 10rpx;
                    }
                }
            }

            .evaluation-text {
                position: absolute;
                left: 0;
                top: 0;
                right: 0;
                bottom: 0;
                height: 60px;
                margin: auto;
                color: #fff;
                text-align: center;
                width: 80%;
                font-size: $basic-title-font-size;
                line-height: $basic-title-line-height;
            }
            .tag-container {
                position: absolute;
                left: 30rpx;
                bottom: 33rpx;
                width: calc(100% - 60rpx);
                height: 60rpx;
                border-bottom-left-radius: 10rpx;
                border-bottom-right-radius: 10rpx;
                background-color: rgba(0,0,0,.7);
            }

            .tag-container-flex {
                display: flex;
                flex-direction: row;
                flex-wrap: nowrap;
                align-items: center;
                justify-content: center;
            }

            .tag-item {
                color: #fff;
                padding-left: 20rpx;
                padding-right: 20rpx;
                text-align: center;
                border-right: 1px solid #fff;
            }

            .tag-item.last {
                border: none;
            }
        }

    }

    .more-btn-container .van-button__text {
        color: #aaa;
    }

    .guide-container {
        position: fixed;
        width: 100%;
        height: 100%;
        left: 0;
        right: 0;
        bottom: 0;
        z-index: 999;
        background-color: rgba(22, 23, 24, 0.5);

        .guide-header {
            display: flex;
            flex-direction: row;
            flex-wrap: nowrap;
            align-items: center;
            justify-content: flex-start;
            height: 200rpx;
            width: 100%;
            text-align: left;

            .quit-button {
                margin-left: 40rpx;
                padding: 20rpx 30rpx;
                background-color: #000;
                color: #fff;
                text-align: center;
                border-radius: 30rpx;
            }
        }

        .guide-content {
            height: 100%;
            width: 100%;
            color: #fff;

            .guide-tips.search {
                top: 250rpx;
            }
            .guide-tips.search {
                top: 250rpx;
            }
            .guide-tips.search {
                top: 250rpx;
            }
            .guide-tips.search {
                top: 250rpx;
            }
            .guide-tips.search {
                top: 250rpx;
            }

            .guide-tips {
                position: fixed;
                display: flex;
                flex-direction: row;
                justify-content: center;
                width: 100%;

                &.NAVIGATION {
                    justify-content: flex-start;

                }
                &.NAVIGATION_ANOU {
                    justify-content: flex-start;
                    left: 25%;

                }
                &.NAVIGATION_MSG {
                    justify-content: flex-start;
                    left: 50%;
                }

                .guide-bubble {
                    padding: 20rpx;
                    color: $basic-color;
                    background: #fff;
                    border: 2rpx solid #fff;
                    border-radius: 10rpx;
                    min-width: 150rpx;
                }

                .guide-bubble-top {
                    position: absolute;
                    border-bottom: 30rpx solid #fff;
                    border-left: 20rpx solid transparent;
                    border-right: 20rpx solid transparent;
                    top: -25rpx;
                    margin-left: 50rpx;

                }
                .guide-bubble-bottom {
                    position: absolute;
                    border-top: 30rpx solid #fff;
                    border-left: 20rpx solid transparent;
                    border-right: 20rpx solid transparent;
                    bottom: -25rpx;
                    margin-left: 50rpx;
                }
            }
        }


    }

    #search.guide-active {
        position: relative;
        z-index: 1000;
    }
    #information.guide-active {
        position: relative;
        z-index: 1000;
    }
    #filter.guide-active {
        position: relative;
        z-index: 1000;
    }
    #evaluation.guide-active {
        position: relative;
        z-index: 1000;
    }
    #navigation.guide-active {
        position: relative;
        z-index: 1000;
    }

</style>
