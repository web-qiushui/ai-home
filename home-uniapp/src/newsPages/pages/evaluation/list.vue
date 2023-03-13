<template>
    <scroll-view class="container" scroll-x="false" :scroll-y="!preventScroll">
        <view class="select-tool-container">
            <view class="filter-content">
                <assessment-filter @change="onFilterChange" @open="open" @close="close"></assessment-filter>
            </view>
            <view class="sort-content">
                <assessment-sort @change="onSortChange"></assessment-sort>
            </view>
        </view>
        <view class="evaluation-list-container">
            <view v-for="(item, index) in list" :key="index" class="evaluation-card"
                 @click="toDetail(item)">
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
            <van-button color="#efefef" block @click="getMore()" v-if="!loading && list.length < total">上滑加载更多↑↑</van-button>
            <van-button color="#efefef" block v-if="!loading && list.length >= total">没有更多了</van-button>
            <van-button color="#efefef" block loading loading-type="spinner"  v-if="loading"/>
        </view>
    </scroll-view>
</template>

<script>
    import apiservice from '../../../api/apiservice'
    export default {
        data() {
            return {
                list: [],
                params: {
                    desc: 'create_time',
                    asc: '',
                    label: '',
                    page: 1,
                    size: 10,
                    search: '',
                    region: '',
                    price: '',
                    area: ''
                },
                total: 0,
                loading: false,
                $imageHost: this.$imageHost,
                preventScroll: false
            }
        },
        created() {
        },
        onLoad(event) {
            // 目前在某些平台参数会被主动 decode，暂时这样处理。
            if (event && event.keywords) {
                this.keywords = event.keywords
                try {
                    this.params.search = decodeURIComponent(event.keywords)
                } catch (error) {
                    this.params.search = event.keywords
                }
            }
            console.log('params', this.params, event)
        },
        onShow() {
            this.queryList(this.params);
        },
        onReachBottom() {
            console.log('on reach bottom')
            this.getMore()
        },
        onPullDownRefresh() {
            this.params.page = 1
            this.list = []
            this.queryList(this.params);
        },
        methods: {
            open(){
                this.preventScroll = true
            },
            close(){
                this.preventScroll = false
            },
            queryList(params, isClear) {
                this.loading = true
                apiservice.getMainPageInfo(params).then(data => {
                    this.loading = false
                    console.log(data)
                    const list  = data.list.map(item => {
                        try {
                            item.tags = item.label
                        } catch (e) {
                            item.tags = [];
                        }
                        return item;
                    });
                    if (isClear) {
                        this.list = list
                    } else {
                        this.list = this.list.concat(list);
                    }

                    this.params.page = data.pageNum;
                    this.total = data.total;
                    uni.stopPullDownRefresh();
                }, e => {
                    this.loading = false
                    uni.stopPullDownRefresh();
                })
            },
            onFilterChange(e) {
                console.log(e)
                const filter = e.detail.__args__[0];
                this.$set(this.params, 'region', filter.region)
                this.$set(this.params, 'price', filter.price)
                this.$set(this.params, 'area', filter.area)
                this.$set(this.params, 'label', filter.type)
                this.params.page = 1
                this.list = []
                this.queryList(this.params, true);
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
                this.params.page = 1
                this.list = []
                this.queryList(this.params, true);
            },
            toDetail(item) {
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
            getMore() {
                if (this.list.length >= this.total) {
                    return;
                }
                this.params.page = this.params.page + 1;
                this.queryList(this.params)
            }
        }
    }
</script>

<style lang="scss">
    page {
        background-color: $basic-background-color;
    }
    .container {
        padding: 0;
        font-size: 25rpx;
        line-height: 24rpx;
        background-color: $basic-background-color;
        color: #565656;
        height: 100vh;

        &.no-scroll {
            overflow: hidden;
        }
    }

    .select-tool-container {
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

        .van-button--active:before {
            opacity: 0!important;
        }
    }

</style>
