<template>
    <view class="content">
            <image :src="openScreenUrl" mode="aspectFit"></image>
    </view>
</template>

<script>
    export default {
        data() {
            return {
                config: {},
                openScreenUrl: "",
                $imageHost: this.$imageHost
            }
        },
        onLoad() {
            this.getDataDict()
        },
        onShow() {
            setTimeout(()=> {
                this.gotoHome()
            }, 2000)
        },
        methods: {
            gotoHome() {
                uni.switchTab({
                    url: '/pages/home/home'
                })
            },
            getDataDict() {
                let list = uni.getStorageSync('dataDict');
                if (list) {
                    this.openScreenUrl = this.$imageHost + this.get(list, 'OPEN_SCREEN_URL');
                }
            },
            get(list, key) {
                return list.filter(item => item.type === key).map(item => {
                    return item.value
                })
            },
        }
    }
</script>

<style lang="scss">
    .content {
        height: 100vh;
        width: 100vw;

            image {
                width: 100%;
                height: 100%;
            }
    }
</style>
