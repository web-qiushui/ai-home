<template>
    <view class="container">
        <scroll-view class="scroll-container" scroll-x="false" scroll-y="true">
            <view class="banner" auto-focus>
                <view class="title-area">
                    <text class="title-text">{{banner.title}}</text>
                </view>
            </view>
            <view class="article-meta">
                <text class="article-meta-text article-originality" v-if="evaluation.original">{{evaluation.original}}</text>
                <text class="article-meta-text article-author" v-if="evaluation.author">{{evaluation.author}}</text>
                <text class="article-meta-text article-public-name" v-if="evaluation.thePublicName">{{evaluation.thePublicName}}</text>
                <text class="article-meta-text article-time" v-if="evaluation.createTime">{{evaluation.createTime}}</text>
            </view>
            <view class="article-content" v-if="content">
                <rich-text :nodes="content" style="font-size: 14px;"></rich-text>
                <view class="recommend-container" v-if="recommentList && recommentList.length">
                    <view class="recommend-label">其他推荐</view>
                    <view class="recommend-list">
                        <view class="recommend-item" v-for="(commend, index) in recommentList" :key="index" @tap="goToCommend(commend)">
                            {{commend.title}}
                        </view>
                    </view>
                </view>
            </view>
            <view class="comment-wrap"></view>
            <view class="contact-container">
                <van-image class="contact-button" @click="toChat()" fit="cover" round width="100rpx" height="100rpx" :src="contactIcon"/>
            </view>
        </scroll-view>
        <van-tabbar class="horizontal-tabbar" v-if="evaluation" safe-area-inset-bottom="true">
            <van-tabbar-item>
                <van-button class="tabbar-button" icon="/static/ai-icon-share.png" open-type="share">
                    {{evaluation.forwardCount || 0}}
                </van-button>
            </van-tabbar-item>
            <van-tabbar-item @tap="goToComment()">
                <van-button class="tabbar-button comment" icon="/static/ai-icon-comment.png">
                    {{evaluation.commentCount || 0}}
                </van-button>
            </van-tabbar-item>
            <van-tabbar-item @tap="collect()">
                <van-button class="tabbar-button collect" icon="/static/ai-icon-star-active.png" v-if="evaluation.isCollect">
                    {{evaluation.collectCount || 0}}
                </van-button>
                <van-button class="tabbar-button collect" icon="/static/ai-icon-star.png" v-else>
                    {{evaluation.collectCount || 0}}
                </van-button>
            </van-tabbar-item>
            <van-tabbar-item @tap="like()">
                <van-button class="tabbar-button" icon="/static/ai-icon-like-active.png" v-if="evaluation.isLike">
                    {{evaluation.likeCount || 0}}
                </van-button>
                <van-button class="tabbar-button" icon="/static/ai-icon-like.png" v-else>
                    {{evaluation.likeCount || 0}}
                </van-button>
            </van-tabbar-item>
        </van-tabbar>
    </view>
</template>

<script>
import htmlParser from '../../../common/html-parser'
import apiservice from '../../../api/apiservice'
import moment from 'moment'

const FAIL_CONTENT = '<p>获取信息失败1</p>';

/**
 * 处理富文本里的图片宽度自适应
 * 1.去掉img标签里的style、width、height属性
 * 2.img标签添加style属性：max-width:100%;height:auto
 * 3.修改所有style里的width属性为max-width:100%
 * 4.去掉<br/>标签
 * @param html
 * @returns {void|string|*}
 */
function formatRichText (html) { //控制小程序中图片大小
    let newContent= html.replace(/<img[^>]*>/gi,function(match,capture){
        match = match.replace(/style="[^"]+"/gi, '').replace(/style='[^']+'/gi, '');
        match = match.replace(/width="[^"]+"/gi, '').replace(/width='[^']+'/gi, '');
        match = match.replace(/height="[^"]+"/gi, '').replace(/height='[^']+'/gi, '');
        return match;
    });
    newContent = newContent.replace(/style="[^"]+"/gi,function(match,capture){
        match = match.replace(/width:[^;]+;/gi, 'max-width:100%;').replace(/width:[^;]+;/gi, 'max-width:100%;');
        return match;
    });
    newContent = newContent.replace(/<br[^>]*\/>/gi, '');
    newContent = newContent.replace(/\<img/gi, '<img style="max-width:100%;height:auto;display:inline-block;margin:10rpx auto;"');
    newContent = newContent.replace(/\<figure/gi, '<div');
    newContent = newContent.replace(/\<\/figure>/gi, '</div>');
    return newContent;
}

export default {
    data() {
        return {
            query: '',
            banner: {},
            evaluation: null,
            content: [],
            contactIcon: '',
            recommentList: []
        }
    },
    onShareAppMessage(e) {
        const that = this;
        this.share()
        return {
            title: this.banner.title || '测评',
            path: `/newsPages/pages/evaluation/detail?query=${this.query}`,
            success: function(res){
                // 转发成功之后的回调
                if(res.errMsg == 'shareAppMessage:ok'){
                }
            },
        }
    },
    onLoad(event) {
        // 目前在某些平台参数会被主动 decode，暂时这样处理。
        let banner
        try {
            banner = JSON.parse(decodeURIComponent(event.query));
        } catch (error) {
            banner = JSON.parse(event.query);
        }

        if (typeof banner !== 'object') {
            this.banner = {
                id: banner
            }
        } else {
            this.banner = banner;
            this.query = event.query
        }

        if (this.banner.title) {
            uni.setNavigationBarTitle({
                title: this.banner.title
            });
        }
        this.getDetail(this.banner.id);
    },
    onShow() {
        this.getDataDict()
    },
    methods: {
        getDataDict() {
            let list = uni.getStorageSync('dataDict');
            if (list) {
                this.contactIcon = this.$imageHost + this.get(list, 'CONTACT_CONSULTANT_ICON');
            }
        },
        get(list, key) {
            return list.filter(item => item.type === key).map(item => {
                return item.value
            })
        },
        getDetail(id) {
            const data = {
                id: id
            }
            apiservice.getEvaluationInfo(data).then(data => {
                console.log('evaluation info', data)
                this.evaluation = data;
                this.evaluation.createTime = moment(this.evaluation.createTime).format('yyyy年MM月DD日')
                this.content = data.content;
                // this.content = formatRichText(data.content);
                this.getRecommendList(this.banner.id)
                this.updateBanner(data)
            });
        },
        updateBanner(item) {
            this.banner = {
                id: item.id,
                title: item.title,
                author: item.author,
                createTime: item.createTime
            }
            this.query = encodeURIComponent(JSON.stringify(this.banner));
            if (this.banner.title) {
                uni.setNavigationBarTitle({
                    title: this.banner.title
                });
            }
        },
        getRecommendList(id) {
            const data = {
                id: id
            }
            apiservice.getRecommendList(data).then(data => {
                console.log('evaluation info', data)
                this.recommentList = data;
            });
        },
        goToComment() {
            uni.navigateTo({
                url: `/newsPages/pages/evaluation/comment?query=${this.query}`
            })
        },
        goToCommend(item) {
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
        like() {
            const data = {
                evaluationId: this.banner.id
            }
            apiservice.saveEvaluationLike(data).then(data => {
                if (this.evaluation && !this.evaluation.isLike) {
                    this.evaluation.isLike = true;
                    this.evaluation.likeCount = this.evaluation.likeCount + 1;
                } else if (this.evaluation && this.evaluation.isLike) {
                    this.evaluation.isLike = false;
                    this.evaluation.likeCount = this.evaluation.likeCount - 1;
                }
            });
        },
        share() {
            const data = {
                evaluationId: this.banner.id
            }
            apiservice.saveEvaluationForward(data).then(data => {
                if (this.evaluation && !this.evaluation.isForward) {
                    this.evaluation.isForward = true;
                    this.evaluation.forwardCount = this.evaluation.forwardCount + 1;
                }
            });
        },
        collect() {
            const data = {
                evaluationId: this.banner.id
            }
            apiservice.saveEvaluationCollect(data).then(data => {
                if (this.evaluation && !this.evaluation.isCollect) {
                    this.evaluation.isCollect = true;
                    this.evaluation.collectCount = this.evaluation.collectCount + 1;

                } else if (this.evaluation && this.evaluation.isCollect) {
                    this.evaluation.isCollect = false;
                    this.evaluation.collectCount = this.evaluation.collectCount - 1;
                }
            });
        },
        toChat() {
            this.saveAIBuddy()
        },
        saveAIBuddy() {
            const data = {
                evaluationId: this.evaluation.id
            }
            apiservice.saveAIBuddy(data).then(res => {
                this.gotoConversation(res, this.evaluation)
            })
        },
        gotoConversation(conversation, evaluation) {
            this.$store.dispatch('message/chat', {
                consultantUserId: conversation.id,
                page: 1,
                size: 10
            });
            const query = encodeURIComponent(JSON.stringify(conversation));
            const params = encodeURIComponent(JSON.stringify({
                type: 'evaluation',
                data: {...evaluation, content: null}
            }));
            uni.navigateTo({
                url: `/pages/message/conversation?query=${query}&params=${params}`
            });
        },

    }
}
</script>

<style lang="scss">
page {
    background: #fff;
}

.container {
    background: #fff;

    .scroll-container {
        padding-bottom: 50px;
        height: calc(100vh - 50px);

        .contact-container {
            position: fixed;
            top: calc(100vh - 250rpx);
            left: calc(100vw - 150rpx);

            .contact-button {
                width: 100rpx;
                height: 100rpx;
                color: #fff;
                // background: $basic-color;
                width: 100rpx;
                border: 50%;
                border-radius: 50%;
                font-size: 45rpx;

                display: flex;
                flex-direction: row;
                align-items: center;
                justify-content: center;

            }
        }

    }

}

.banner {
    position: relative;
            flex-direction: row;
    overflow: hidden;
    padding: 20rpx 30rpx;
    padding-bottom: 0;

    .title-text {
        font-size: $basic-title-font-size;
        font-weight: 400;
        line-height: 50rpx;
        color: #000;
        overflow: hidden;
    }
}

.article-meta {
    padding: 20rpx 30rpx;
    padding-top: 0;
    flex-direction: row;
    align-items: center;
    justify-content: flex-start;
    font-size: 22rpx;
    line-height: 50rpx;
    color: #aaa;

    .article-meta-text {
        margin-right: 10rpx;

        &.article-originality {
            background: #efefef;
            padding: 0 10rpx;
        }

        &.article-author {
            // color: #009dff;
        }


        &.article-public-name {
            color: #009dff;
        }
    }

}
.article-content {
    padding: 0 30rpx;
    margin-bottom: 30rpx;
    overflow: hidden;

    .recommend-container {
        margin-top: 30rpx;
        padding: 0 20rpx;
        font-size: 24rpx;
        line-height: 40rpx;

        .recommend-list {
            color: #009dff;
        }
    }
}
.horizontal-tabbar {

    .van-tabbar-item {
        flex-direction: row;
        align-items: center;

        .tabbar-button.comment,
        .tabbar-button.collect {

            .van-icon {
                width: 30px;
                height: 22px !important;
            }
        }

        .tabbar-button {
            margin-top: 2rpx;

            .van-button {
                border: none !important;

                .van-icon {
                    width: 30px;
                    height: 18px;
                }
            }
            .van-button--active:before {
                opacity: 0!important;
            }
        }
    }
    .van-tabbar-item__icon {
        margin-top: 10rpx;
        margin-right: 15rpx;
        font-size: 40rpx !important;
    }
}

</style>
