<template>
    <scroll-view class="emoji-scroll-view" scroll-y="true">
        <view class="emoji-field-container">
            <view class="emoji-field-content" v-for="(item, index) in list" :key="index">
                <view class="emoji-field" @tap="onTap(item)">
                    {{item}}
                </view>
            </view>
        </view>
    </scroll-view>
</template>

<script>
    import emojis from '../common/emoji'

    export default {
        data() {
            return {
                list: emojis
            }
        },
        created() {
        },
        computed: {
        },
        methods: {
            onTap(item) {
                this.$emit('send', item)
            }
        }
    }
</script>

<style lang="scss" scoped>
    .emoji-scroll-view {
        width: 750rpx;
        height: 400rpx;
        background-color: #efefef;
        border-top: 1px solid #ccc;
    }

    .emoji-field-container {
        display: flex;
        flex-direction: row;
        flex-wrap: wrap;
        justify-content: flex-start;
        align-items: flex-start;
        width: 100%;
        height: 100%;
        transition-duration: 300ms;

        .emoji-field-content {
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            flex: 0 0 12.5%;
            padding-top: 30rpx;

            .emoji-field {
                padding: 10rpx;
                height: 20rpx;
                width: 20rpx;
                font-size: 45rpx;

                display: flex;
                justify-content: center;
                align-items: center;

                .emoji-icon {
                    width: 80rpx;
                    height: 80rpx;
                }
            }

            .field-name {
                text-align: center;
                font-size: 20rpx;
            }
        }
    }
</style>
