<template>
    <view class="container">
        <scroll-view class="scroll-container" scroll-x="false" :scroll-y="!preventScroll" :class="{'show-login': !isLogin}">
            <swiper :autoplay="!preventScroll" indicator-dots="true">
                <swiper-item v-for="(item, index) in swiper.list" :key="index" @tap="onSwiperTap(item)">
                    <van-image width="100%" height="100%" fit="cover" :src="$imageHost + item.imageUrl"/>
                </swiper-item>
            </swiper>
            <view class="search-bar-container">
                <view class="search-bar">
                    <van-search
                        :value="keywords"
                        shape="round"
                        @search="onSearch"
                        @clear="onClear"
                        @change="onSearchChange"
                        background="#fff"
                        placeholder="查找我的意向楼盘测评"
                        use-left-icon-slot="true"
                    >
                        <van-icon slot="left-icon" class="search-bar-icon" name="search"/>
                    </van-search>
                </view>
            </view>
            <view class="navigation-bar-container">
                <view class="navigation-bar">
                <van-grid column-num="3" :border="false">
                    <van-grid-item v-for="(bar, index) in bars" :key="index"
                                use-slot
                                :text="bar.name"
                                link-type="navigateTo"
                                :url="bar.url"
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
            <view class="select-tool-container">
                <view class="filter-content">
                    <assessment-filter @change="onFilterChange" @open="open" @close="close" ref="filter"></assessment-filter>
                </view>
                <view class="sort-content">
                    <assessment-sort @change="onSortChange" showMore="true" ref="sort"></assessment-sort>
                </view>
            </view>
            <view class="evaluation-list-container">
                <view v-for="(item, index) in list" :key="index" class="evaluation-card"
                    @click="toEvaluationDetail(item)">
                    <van-image class="evaluation-img" width="100%" height="150" fit="cover" :src="$imageHost + item.mainImage"/>
                    <view class="evaluation-text">{{item.title}}</view>
                    <view class="tag-container tag-container-flex">
                        <view class="tag-item" :class="{'last': tagIndex == item.tags.length - 1}" v-for="(tag, tagIndex) in item.tags" :key="tagIndex">
                            <text>{{tag}}</text>
                        </view>
                    </view>
                </view>
            </view>
            <view class="more-btn-container">
                <van-button color="#efefef" block v-if="!loading && list.length < total" @click="toEvaluationList()">{{moreText}}</van-button>
                <van-button class="no-active" color="#efefef" block v-if="!loading && list.length >= total">{{noMoreText}}</van-button>
                <van-button class="no-active" color="#efefef" block loading loading-type="spinner"  v-if="loading"/>
            </view>
        </scroll-view>
        <view class="login-container" v-if="!isLogin">
            <view class="login-bar">
                <view class="login-text">{{loginText}}</view>
                <view class="login-btn">
                    <van-button color="#c30D23" size="small" @click="login()">登陆</van-button>
                </view>
            </view>
        </view>
        <van-dialog id="van-dialog" confirm-button-color="#c30D23"/>
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
                    desc: 'create_time',
                    asc: '',
                    label: '',
                    page: 1,
                    size: 5,
                    search: '',
                    region: '',
                    price: '',
                    area: ''
                },
                list: [],
                total: 0,
                loading: false,
                isLogin: true,
                $imageHost: this.$imageHost,
                preventScroll: false,
                isShowStart: false,
                loginText: '马上登录，享受一对一专属服务',
                moreText: '查看更多详细测评',
                noMoreText: '没有更多了'
            }
        },
        created() {
            this.getSwipers()
            this.getKingkong()
            // this.queryList(this.params);
        },
        onLoad() {
            console.log('on load')
        },
        onLaunch() {
            console.log('on launch')
        },
        onShow() {
            this.refreshList()
            this.setTabBarIndex(0)
            this.openGuideConfirm()
            this.checkToken()
        },
        onPullDownRefresh() {
            this.refreshForce()
            this.list = []
            this.queryList(this.params);
        },
        methods: {
            refreshForce() {
                this.getSwipers(true)
                this.getKingkong(true)
            },
            refreshList() {
                this.isShowStart = true
                this.$refs.filter.reset()
                this.$refs.sort.reset()
                this.queryList(this.params);
            },
            open(){
                this.preventScroll = true
            },
            close(){
                this.preventScroll = false
                if (!this.loading) {
                    this.loading = true
                    setTimeout(() => {
                        this.loading = false    
                    }, 10);
                }
            },
            checkToken() {
                this.isLogin = !!uni.getStorageSync('token')
            },
            goTo(bar) {
                uni.navigateTo({
                    url: bar.url,
                })
            },
            onSearchChange(e) {
                if (!e.detail) {
                    this.keywords = ''
                    this.params.search = e.detail;
                    this.list = []
                    this.queryList(this.params);
                }
            },
            onSearch(e) {
                console.log('on search', e)
                this.keywords = e.detail
                // uni.navigateTo({
                //     url: `/newsPages/pages/evaluation/list?keywords=${this.keywords}`
                // })
                this.params.search = e.detail;
                this.list = []
                this.queryList(this.params);
            },
            onClear(e) {
                console.log('on clear', e)
                this.keywords = ''
                this.params.search = ''
                this.list = []
                this.queryList(this.params);
            },
            onSwiperTap(swiper) {
                if (swiper.pagePath) {
                    uni.navigateTo({
                        url: swiper.pagePath
                    })
                }
            },
            onFilterChange(e) {
                console.log(e)
                const filter = e.detail.__args__[0];
                this.$set(this.params, 'region', filter.region)
                this.$set(this.params, 'price', filter.price)
                this.$set(this.params, 'area', filter.area)
                this.$set(this.params, 'label', filter.type)
                this.list = []
                if (!this.isShowStart) {
                    this.queryList(this.params);
                }
            },
            onSortChange(e) {
                const sort = e.detail.__args__[0];
                if (sort.order === 'desc') {
                    this.params.desc = sort.key
                    this.params.asc = ''
                } else {
                    this.params.asc = sort.key
                    this.params.desc = ''
                }
                this.list = []
                if (!this.isShowStart) {
                    this.queryList(this.params);
                }
            },
            toEvaluationList(id) {
                uni.navigateTo({
                    url: `/newsPages/pages/evaluation/list`
                })
            },
            toEvaluationDetail(item) {
                const query = encodeURIComponent(JSON.stringify({
                    id: item.id,
                    title: item.title,
                    author: item.author,
                    createTime: item.createTime
                }));
                uni.navigateTo({
                    url: `/newsPages/pages/evaluation/detail?query=${query}`
                })
            },
            queryList(params) {
                this.loading = true
                apiservice.getMainPageInfo(params).then(data => {
                    console.log(data)
                    this.list = data.list.map(item => {
                        try {
                            item.tags = item.label;
                        } catch (e) {
                            item.tags = [];
                        }
                        return item;
                    });
                    this.total = data.total;
                    this.loading = false
                    this.isShowStart = false
                    uni.stopPullDownRefresh();
                }, err => {
                    this.loading = false
                    this.isShowStart = false
                    uni.stopPullDownRefresh();
                })
            },
            getSwipers(force = false) {
                const that = this;
                const swipers = uni.getStorageSync('swipers')

                if (swipers && !force) {
                    that.swiper.list = swipers;
                } else {
                    apiservice.getCarouselList().then(data => {
                        that.swiper.list = data;
                    })
                }
            },
            getKingkong(force = false) {
                const that = this;
                const kingkong = uni.getStorageSync('kingkong')
                if (kingkong && !force) {
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
                    'REGISTERING': '/newsPages/pages/kingkong/registering',
                    'PRE_SALE': '/newsPages/pages/kingkong/presale',
                    '24H_NEWS': '/newsPages/pages/kingkong/news',
                }
                const bars = list.map(item => {
                    return {...item, url: MAP[item.type]}
                })

                this.bars = bars;
            },
            login() {
                uni.navigateTo({
                    url: `/pages/login/login`
                })
            },
            openGuideConfirm() {
                const hasShowGuide = uni.getStorageSync('hasShowGuide');
                if (hasShowGuide) {
                    return;
                }

                Dialog.confirm({
                        title: '提示',
                        message: '你好,\n这是您首次进入AI家，是否为您打开新人指引，帮助您更快的了解我们？',
                        messageAlign: 'left',
                        confirmButtonText: '是',
                        cancelButtonText: '否'
                    })
                    .then(() => {
                        this.toGuidePage()
                        uni.setStorageSync('hasShowGuide', true)
                    })
                    .catch(() => {
                        uni.setStorageSync('hasShowGuide', true)
                    });
            },
            toGuidePage() {
                uni.navigateTo({
                    url: '/pages/home/guide'
                })
            }
        }
    }
</script>

<style lang="scss">
    page {
        background-color: $basic-background-color;
        height: calc(100vh - 50px);
    }
    .container {
        font-size: 25rpx;
        line-height: 24rpx;
        background-color: #efefef;
        color: #565656;

        .scroll-container {
            height: calc(100vh - 50px - env(safe-area-inset-bottom));

            swiper {
                height: 360rpx;
            }

            &.no-scroll {
                overflow: hidden;
            }

            &.show-login {
                height: calc(100vh - 100px - env(safe-area-inset-bottom));
            }
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
            filter: brightness(0.8)
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

    .more-btn-container {
        .van-button__text {
            color: #aaa;
        }

        .no-active {
            .van-button--active {
                &:before {
                    opacity: 0!important;
                }
            }

        }
    }

    .login-container {
        background-color: #fff;
        position: fixed;
        top: 0;
        left: 0;
        right: 0;
        height: 50px;
        margin-top: calc(100vh - 100px);


        .login-bar {
            display: flex;
            flex-direction: row;
            align-items: center;
            justify-content: space-between;
            padding: 20rpx 30rpx;
        }

        .login-text {
            display: inline-block;
            line-height: 50rpx;
            color: $basic-color;
        }
    }

</style>
