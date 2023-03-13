<template>
    <view class="container">
        <div class="select-content">
            <div class="select-item">
                <div>联系电话</div>
                <div>{{anouncementdetail.phone}}</div>
            </div>
        </div>
        <div class="select-content">
            <div class="select-item">
                <div>区域</div>
                <div>{{anouncementdetail.region}}</div>
            </div>
            <div class="select-item">
                <div>价格</div>
                <div>{{anouncementdetail.price}}</div>
            </div>
        </div>
        <div class="select-content">
            <div class="select-item">
                <div>面积</div>
                <div>{{anouncementdetail.area}}</div>
            </div>
        </div>
        <div class="tag">
            <div>标签</div>
            <div class="tag-list">
                <div class="tag-item" v-for="(item,index) in tagList" :key="index" @click="deleteTag(index)">
                    <div>{{item}}</div>
                    <!--                    <image src="../../../static/close.png"></image>-->
                </div>
            </div>
        </div>
        <div class="text">
            <div>其他说明</div>
            <div>{{anouncementdetail.explanation}}</div>
        </div>
        <div class="btns" @click="edit" v-if="type !== 'view'">
            <div class="btn">编辑</div>
        </div>
    </view>
</template>

<script>
    export default {
        name: "anouncementdetail",
        data() {
            return {
                tagList: [],
                anouncementdetail: null,
                type: ''
            }
        },
        created() {
        },
        onLoad(options) {
            this.anouncementdetail = JSON.parse(decodeURIComponent(options.anouncementdetail));
            this.type = options.type;
            if (this.anouncementdetail.label) {
                this.tagList = this.anouncementdetail.label;
            }
        },
        methods: {
            edit() {
                uni.setStorageSync('anouncementdetail', this.anouncementdetail);
                uni.switchTab({
                    url: `/pages/anouncement/anouncement`
                })
            }
        }
    }
</script>

<style scoped>
    .container {
        padding: 0 15px;
        font-size: 14px;
        height: 100vh;
        background: #F5F5F5;

    }

    .select-content {
        height: 50px;
        display: flex;
        align-items: center;
        border-bottom: 1px solid #C7C7C7;
    }

    .select-item {
        width: 50%;
        display: flex;
        align-items: center;
        justify-content: space-between;
    }

    .select-item > div:last-child {
        color: #000;
        width: 50%;
        font-weight: bold;
    }

    .tag > div:first-child {
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

    .tag-item div {
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

    .text > div:first-child {
        line-height: 40px;
    }

    .text > div:last-child {
        background: #fff;
        min-height: 100px;
        padding: 10px;
    }

    .btns {
        height: 200px;
        display: flex;
        align-items: center;
        justify-content: center;
    }

    .btn {
        width: 100%;
        height: 50px;
        border-radius: 25px;
        background: #C30D23;
        color: #ffffff;
        display: flex;
        align-items: center;
        justify-content: center;
    }
</style>
