<template>
    <view class="container">
        <div class="info-content">
            <div class="info-item">
                <div>头像</div>
                <image :src="userinfo.avatarUrl"></image>
            </div>
            <div class="info-item">
                <div>昵称</div>
                <div>{{userinfo.nickName == null ? '' : userinfo.nickName}}</div>
            </div>
            <div class="info-item">
                <div>性别</div>
                <div>{{userinfo.gender}}</div>
            </div>
            <div class="info-item">
                <div>手机号</div>
                <div>{{userinfo.phone == null ? '' : userinfo.phone}}</div>
            </div>
            <div class="info-item">
                <div>身份证</div>
                <div>{{userinfo.idCard == null ? '' : userinfo.idCard}}</div>
            </div>
            <div class="info-item">
                <div>通讯地址</div>
                <div>{{userinfo.address == null ? '' : userinfo.address}}</div>
            </div>
        </div>
        <div class="btn">
            <van-button round color="#C30D23" size="large" block @click="modify">编辑</van-button>
        </div>
    </view>
</template>

<script>
    import apiservice from '../../../api/apiservice';

    export default {
        name: "userinfo",
        data() {
            return {
                userinfo: {}
            }
        },
        created() {
        },
        onShow(){
            this.getUserInfo();
        },
        methods: {
            getUserInfo() {
                apiservice.getUser().then((res) => {
                    if (res.gender == null || res.gender == 0) {
                        res.gender = '未知'
                    } else if (res.gender == 1) {
                        res.gender = '男'
                    } else if (res.gender == 2) {
                        res.gender = '女'
                    }
                    this.userinfo = res;
                    uni.setStorageSync('sysUserInfo', this.userinfo)
                })
            },
            modify() {
                let userinfo = encodeURIComponent(JSON.stringify(this.userinfo));
                uni.navigateTo({
                    url: `/newsPages/pages/person/modifyinfo?userinfo=${userinfo}`
                })
            }
        }
    }
</script>

<style scoped>
    .container {
        height: 100vh;
        font-size: 14px;
        line-height: 24px;
        background: #F5F5F5;
    }

    .info-content {
        height: 300px;
        background: #ffffff;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: space-around;
        padding: 0 20px;
    }

    .info-content .info-item:first-child {
        height: 60px;
    }

    .info-content .info-item:last-child {
        height: 70px;
        border-bottom: none;
    }

    .info-item {
        width: 100%;
        height: 50px;
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding: 0 20px;
        border-bottom: 1px solid #F5F5F5;
    }

    .info-item > div:first-child {
        width: 100px;
    }

    .info-item > div:last-child {
        width: calc(100% - 100px);
        display: flex;
        justify-content: flex-end;
    }

    .info-item image {
        width: 50px;
        height: 50px;
        border-radius: 50%;
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
