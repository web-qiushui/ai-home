<template>
    <view class="container">
        <div class="user-info" @click="amend">
            <van-image
                    round
                    width="60px"
                    height="60px"
                    :src="user.avatarUrl" class="user-head"
            />
            <div v-if="isLogin" class="login">
                <div style="font-size: 18px">{{user.nickName}}</div>
                <div @click.stop="toFocus">我的收藏：{{focusNum}}</div>
            </div>
            <div v-else class="login" @click.stop="login">点击登录</div>
            <div class="user-verify" @click.stop="certification">
                <span>{{authenticateStatus[user.authenticateStatus]}}>></span>
            </div>
        </div>
        <div class="business-list">
            <div class="business-item" v-for="(tab, index) in tabs" :key="index" @click="switchpage(tab.url)">
                <div class="business">
                    <image :src="tab.image"></image>
                    <div>{{tab.text}}</div>
                </div>
                <div class="business-num">
                    <div>{{tab.num}}</div>
                    <image src="/static/right.png"></image>
                </div>
            </div>
        </div>
        <div class="btn" @click="loginOut">退出登录</div>
    </view>
</template>

<script>
    import apiservice from '../../api/apiservice'
    import util from '../../common/util'

    export default {
        data() {
            return {
                tabs: [{
                    image: '/static/icon-add.png',
                    text: '我的发布',
                    url: '/newsPages/pages/person/myanouncement',
                    num: 0
                }, {
                    image: '/static/icon-person.png',
                    text: '我的顾问',
                    url: '/newsPages/pages/person/mycounselor',
                    num: 0
                }, {
                    image: '/static/icon-share.png',
                    text: '我的分享',
                    url: '/newsPages/pages/person/myshare',
                    num: 0
                }, {
                    image: '/static/icon-news.png',
                    text: '我的评论',
                    url: '/newsPages/pages/person/mycomment',
                    num: 0
                }, {
                    image: '/static/icon-pingjia.png',
                    text: '我的评价',
                    url: '/newsPages/pages/person/evaluate',
                    num: 0
                }],
                isLogin: true,
                user: null,
                focusNum: 0,
                authenticateStatus: {
                    0: '未认证',
                    1: '认证中',
                    2: '认证成功',
                    3: '认证失败'
                }
            }
        },
        created() {
        },
        mounted() {
        },
        onShow() {
            this.setTabBarIndex(3);
            this.evaluationcollectsize();
            this.mineinfo();
            this.userInfo();
        },
        onPullDownRefresh() {
            this.userInfo();
            this.evaluationcollectsize();
            this.mineinfo();
        },
        methods: {
            certification() {
                uni.navigateTo({url: `/newsPages/pages/person/certification?authenticateStatus=${this.user.authenticateStatus}`})
            },
            toFocus() {
                uni.navigateTo({url: `/newsPages/pages/person/myattention`})
            },
            amend() {
                uni.navigateTo({url: `/newsPages/pages/person/userinfo`})
            },
            login() {
                uni.navigateTo({
                    url: `/pages/login/login`
                })
            },
            switchpage(path) {
                uni.navigateTo({
                    url: path
                })
            },
            userInfo() {
                apiservice.getUser().then((res) => {
                    if (res) {
                        this.user = res;
                        uni.setStorageSync('sysUserInfo', this.user)
                    }
                })
            },
            loginOut() {
                util.showLoading('退出中...');
                // uni.clearStorageSync();
                this.clearStorage()
                setTimeout(() => {
                    util.hideLoading();
                    uni.reLaunch({url: '/pages/home/home'})
                }, 500)
            },
            clearStorage() {
                uni.removeStorageSync('token');
            },
            // 我的关注数量
            evaluationcollectsize() {
                apiservice.getevaluationcollectsize().then((res) => {
                    this.focusNum = res;
                })
            },
            mineinfo() {
                apiservice.getmineinfo().then((res) => {
                    if (res) {
                        this.tabs[0].num = res.demandCount;
                        this.tabs[1].num = res.consultantCount;
                        this.tabs[2].num = res.evaluationForwardCount;
                        this.tabs[3].num = res.evaluationCommentCount;
                        this.tabs[4].num = res.consultantCommentCount;
                    }
                    uni.stopPullDownRefresh();
                }, e => {
                    uni.stopPullDownRefresh();
                })
            }
        }
    }
</script>

<style>
    .container {
        font-size: 14px;
        line-height: 24px;
        height: 100vh;
        background: #F2F3F7;
    }

    .user-info {
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding: 10px 0;
        background: #C30D23;
        color: #ffffff;
        height: 100px;
    }

    .user-head {
        margin: 0 20px;
    }

    .login {
        width: calc(100% - 200px);
    }

    .user-verify {
        display: flex;
        align-items: center;
        justify-content: center;
        height: 30px;
        border-top-left-radius: 15px;
        border-bottom-left-radius: 15px;
        background: #ffffff;
        font-size: 14px;
        color: #C30D23;
        padding: 0 10px;
    }

    .custom-grid-item-content {
        border: 1px solid #999999;
    }

    .navigater-text {
        padding-top: 10px !important;
    }

    .business-list {
        margin-top: 20px;
        background: #ffffff;
    }

    .business-item {
        height: 50px;
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding: 0 20px;
    }

    .business-list > .business-item:not(:last-child) {
        border-bottom: 1px solid #F2F3F7;
    }

    .business {
        display: flex;
        align-items: center;
    }

    .business image {
        width: 20px;
        height: 20px;
        margin-right: 10px;
    }

    .business-num {
        display: flex;
        align-items: center;
    }

    .business-num image {
        width: 20px;
        height: 20px;
        margin-left: 10px;
    }

    .btn {
        height: 50px;
        display: flex;
        align-items: center;
        justify-content: center;
        color: #C30D23;
        background: #ffffff;
        margin-top: 30px;
    }
</style>
