<template>
    <view class="container">
        <assessment-filter></assessment-filter>
        <assessment-sort></assessment-sort>
    </view>
</template>

<script>
    export default {
        data() {
            return {
            }
        },
        methods: {
        }
    }
</script>

<style>
    .container {
        padding: 15px;
        padding-top: 0;
        font-size: 14px;
        line-height: 24px;
    }

    .van-dropdown-item.van-dropdown-item--down {
        margin: 0 15px !important;
        box-shadow: 2px 2px 5px 2px #aaa;
    }
</style>
