<template>
    <view class="ellipsis">
        <view class="ellipsis-container" :class="{'no-expand': !item.expanded}">
            <view class="ellipsis-content">
                <text>{{item.content}}</text>
                <view class="ellipsis-less" v-if="item.expanded && item.isEllipsis" @tap="contract()">收起</view>
            </view>
            <view class="ellipsis-ghost" v-if="!item.expanded && item.isEllipsis">
                <view class="ellipsis-more" @tap="expand()">更多</view>
            </view>
        </view>
    </view> 
</template>

<script>
    export default {
        props: ['value'],
        data() {
            return {
                item: {
                    content: this.value || '',
                    expanded: false,
                    isEllipsis: this.isEllipsis()
                }
            }
        },
        created() {
            this.query = uni.createSelectorQuery('.content');
        },
        computed: {},
        methods: {
            expand() {
                this.item.expanded = true;
            },
            contract() {
                this.item.expanded = false;
            },
            isEllipsis() {
                if (this.value && this.value.length) {
                    console.log(this.value.length)
                    const LEN = 100;
                    return this.value.length > LEN;
                }

                return false;
            }
        }
    }
</script>

<style lang="scss">

$font-size: 26rpx;
$line-height: 45rpx;

.ellipsis {
    position: relative;
    width: 100%;
    overflow: hidden;

    .ellipsis-container {
        position: relative;
        display: -webkit-box;
        -webkit-box-orient: vertical;
        font-size: 100rpx;
        color: transparent;

        &.no-expand {
            -webkit-line-clamp: 3;
        }

        .ellipsis-content {
            color: #ccc;
            vertical-align: top;
            font-size: $font-size;
            line-height: $line-height;

            .ellipsis-less {
                display: inline;
                font-size: $font-size;
                color: $basic-color;
                line-height: $line-height;
            }
        }

        .ellipsis-ghost {
            position:absolute;
            z-index: 1;
            top: 0;
            left: 50%;
            width: 50%;
            height: 100%;
            color: #000;

            display: flex;
            justify-content: flex-end;
            align-items: flex-end;

            .ellipsis-more {
                font-size: $font-size;
                // line-height: 40rpx;
                width: 70rpx;
                color: $basic-color;
            }
        }
    }
}
</style>
