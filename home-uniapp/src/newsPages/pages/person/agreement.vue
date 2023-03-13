<template>
    <view class="container">
        <view class="head">{{title}}</view>
        <view class="content">
            <rich-text :nodes="content" style="font-size: 14px;"></rich-text>
        </view>
    </view>
</template>

<script>
    function formatRichText(html) { //控制小程序中图片大小
        let newContent = html.replace(/<img[^>]*>/gi, function (match, capture) {
            match = match.replace(/style="[^"]+"/gi, '').replace(/style='[^']+'/gi, '');
            match = match.replace(/width="[^"]+"/gi, '').replace(/width='[^']+'/gi, '');
            match = match.replace(/height="[^"]+"/gi, '').replace(/height='[^']+'/gi, '');
            return match;
        });
        newContent = newContent.replace(/style="[^"]+"/gi, function (match, capture) {
            match = match.replace(/width:[^;]+;/gi, 'max-width:100%;').replace(/width:[^;]+;/gi, 'max-width:100%;');
            return match;
        });
        newContent = newContent.replace(/<br[^>]*\/>/gi, '');
        newContent = newContent.replace(/\<img/gi, '<img style="max-width:100%;height:auto;display:inline-block;margin:10rpx auto;"');
        return newContent;
    }

    import apiservice from '../../../api/apiservice'

    export default {
        name: "agreement",
        data() {
            return {
                content: [],
                title: ''
            }
        },
        created() {
        },
        onShow() {
            this.getsinglepagebycode();
        },
        methods: {
            getsinglepagebycode() {
                let parmes = {
                    code: 'USER_AGREEMENT'
                };
                apiservice.getsinglepagebycode(parmes).then((res) => {
                    console.log(res)
                    this.content = formatRichText(res.content);
                    this.title = res.title;
                })
            }
        }
    }
</script>

<style>
    .container {
        width: 100%;
        height: 100%;
        overflow: auto;
    }

    .head {
        height: 50px;
        text-align: center;
        line-height: 50px;
    }
    .content{
        padding: 0 10px;
        font-size: 12px;
    }
</style>
