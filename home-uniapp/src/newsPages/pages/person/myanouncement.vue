<template>
    <view class="container">
        <div class="anouncement">
            <div class="anouncement-list" v-for="(item,index) in anouncementList" :key="index"
                 @click="anouncementDetail(item)" :class="item.isTouchMove ? 'touch-move-active' : ''"
                 @touchstart="touchstart" @touchmove="touchmove(index,$event)">
                <div class="anouncement-content">
                    <div>购房需求{{index+1}}</div>
                    <div class="anouncement-item">
                        <div>区域：{{item.region}}</div>
                        <div>价格：{{item.price}}</div>
                    </div>
                    <div class="anouncement-item">
                        <div>面积：{{item.area}}</div>
                        <div>电话：{{item.phone}}</div>
                    </div>
                    <div class="anouncement-item">
                        <div>标签：{{item.label}}</div>
                    </div>
                </div>
                <div class="del" @click.stop="del(item.id)">删除</div>
            </div>
            <div class="eui-loading" v-if="!hasMore">
                <block>
                    <block v-if="anouncementList.length === 0">没有查询到您想要的数据</block>
                    <block v-else>没有更多数据了</block>
                </block>
            </div>
            <div class="btn">
                <van-button round color="#C30D23" size="large" block @click="anouncement">免费发布</van-button>
            </div>
            <div v-if="hasMore" class="eui-loading">
                <block>
                    <van-button color="#f5f5f5" block loading loading-type="spinner" loading-text="加载中..."/>
                </block>
            </div>
        </div>
    </view>
</template>

<script>
    import apiservice from '../../../api/apiservice'

    export default {
        name: "myanouncement",
        data() {
            return {
                anouncementList: [],
                params: {
                    page: 1,
                    size: 10,
                },
                hasMore: true,
                loaded: true,
                startX: 0,
                startY: 0
            }
        },
        created() {
            this.demandlist();
        },
        onReachBottom() {
            if (this.hasMore && this.loaded) {
                this.params.page++;
                this.demandlist();
            }
        },
        onPullDownRefresh() {
            this.hasMore = true;
            this.loaded = true;
            this.anouncementList = [];
            this.params.page = 1;
            this.demandlist();
        },
        methods: {
            touchstart(e) {
                this.anouncementList.forEach((item) => {
                    if (item.isTouchMove) {
                        item.isTouchMove = false;
                    }
                });
                this.startX = e.changedTouches[0].clientX;
                this.startY = e.changedTouches[0].clientY;
            },
            touchmove(index, e) {
                let that = this,
                    startX = that.startX,
                    startY = that.startY,
                    touchMoveX = e.changedTouches[0].clientX,
                    touchMoveY = e.changedTouches[0].clientY,
                    angle = that.angle({X: startX, Y: startY}, {X: touchMoveX, Y: touchMoveY});
                this.anouncementList.forEach((item, i) => {
                    item.isTouchMove = false;
                    if (Math.abs(angle) > 30) return;
                    if (i === index) {
                        if (touchMoveX > startX) {
                            item.isTouchMove = false
                        } else {
                            item.isTouchMove = true
                        }
                    }
                });
            },
            angle(start, end) {
                let _X = end.X - start.X,
                    _Y = end.Y - start.Y;
                return 360 * Math.atan(_Y / _X) / (2 * Math.PI);
            },
            del(id) {
                let that = this
                const param = {
                    id: id
                }
                apiservice.deletedemand(param).then(data => {
                    that.hasMore = true;
                    that.loaded = true;
                    that.anouncementList = [];
                    that.params.page = 1;
                    that.demandlist();
                })
            },
            anouncementDetail(item) {
                let anouncementdetail = encodeURIComponent(JSON.stringify(item));
                uni.navigateTo({url: `/newsPages/pages/anouncement/anouncementdetail?anouncementdetail=${anouncementdetail}`})
            },
            demandlist() {
                if (this.hasMore && this.loaded) {
                    this.loaded = false;
                    apiservice.getdemandlist(this.params).then((res) => {
                        if (res) {
                            if (res.pages === 0) {
                                this.hasMore = false;
                                this.loaded = true;
                            } else {
                                res.list.forEach((item) => {
                                    item.isTouchMove = false;
                                });
                                this.anouncementList = this.anouncementList.concat(res.list);
                                setTimeout(() => {
                                    this.loaded = true;
                                    this.hasMore = !(this.params.page === res.pages);
                                }, 100);
                            }
                        } else {
                            this.hasMore = false;
                            this.loaded = true;
                        }
                        uni.stopPullDownRefresh();
                    }, e => {
                        uni.stopPullDownRefresh();
                    })
                }
            },
            anouncement() {
                uni.switchTab({
                    url: '/pages/anouncement/anouncement'
                });
            }
        }
    }
</script>

<style>
    page {
        height: 100%;
        background: #F5F5F5;
    }

    .container {
        font-size: 14px;
    }

    .anouncement-list {
        height: 100px;
        border-bottom: 1px solid #F5F5F5;
        padding: 15px;
        background: #fff;
        display: flex;
        align-items: center;
        justify-content: space-between;
        overflow: hidden;
    }

    .anouncement-content {
        width: 100%;
        height: 100%;
        display: flex;
        flex-direction: column;
        justify-content: space-around;
        transform: translateX(90px);
        transition: all 0.4s;
        margin-left: -90px;
    }

    .del {
        width: 60px;
        height: 50px;
        border-radius: 5px;
        background: #C30D23;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        color: #fff;
        transform: translateX(90px);
        transition: all 0.4s;
    }

    .touch-move-active .anouncement-content,
    .touch-move-active .del {
        transform: translateX(0);
    }

    .anouncement-content > div:first-child {
        font-size: 15px;
        color: #C30D23;
        font-weight: bold;
    }

    .anouncement-item {
        display: flex;
        align-items: center;
        justify-content: space-between;
    }

    .anouncement-item div {
        width: 50%;
        display: flex;
        align-items: center;
        justify-content: flex-start;
        font-size: 12px;
        color: #333333;
    }

    .anouncement-list .anouncement-item:last-child div {
        width: 100%;
    }

    .eui-loading {
        display: flex;
        justify-content: center;
        align-items: center;
        height: 42px;
        font-size: 13px;
        color: #999999;
        background: transparent !important;
    }

    .eui-loading .van-button {
        background: #F5F5F5 !important;
        border: none !important;
        color: #cccccc !important;
    }

    .btn {
        height: 100px;
        display: flex;
        align-items: center;
        justify-content: center;
        padding: 0 15px;
    }

    .btn van-button {
        width: 100%;
    }
</style>
