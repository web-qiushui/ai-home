<template>
    <view class="sort-bar flex-row">
        <view class="sort-area flex-row">
            <view class="sort-item flex-row" :class="{'active': sort.active}" v-for="(sort, index) in sortList" :key="index">
                <view class="filter-text" :class="{'active': sort.active}" @tap="onTap(sort, index)">{{sort.name}}</view>
                <!-- <van-icon class="filter-icon" name="down" /> -->
            </view>
        </view>
        <view class="more-area flex-row" v-if="showMore">
            <view class="more" @tap="onMoreTap()">查看更多</view>
            <van-icon class="filter-icon" name="arrow" />
        </view>
    </view>
</template>

<script>
    export default {
        props: ['showMore'],
        data() {
            return {
                sortList: [{
                    id: '1',
                    key: 'create_time',
                    order: 'desc',
                    name: '最新测评',
                    active: true
                }, {
                    id: '2',
                    key: 'comment_count',
                    order: 'desc',
                    name: '最多评论',
                    active: false
                }]
            }
        },
        created() {
        },
        methods: {
            onMoreTap() {
                uni.navigateTo({
                    url: `/newsPages/pages/evaluation/list`
                })
            },
            reset() {
                this.$set(this.sortList[0], 'active', true)
                this.$set(this.sortList[1], 'active', false)
                this.$emit('change', this.sortList[0])
            },
            onTap(item, index) {
                if (index == 0) {
                    if (item.active) {
                        return;
                        if (item.order === 'desc') {
                            item.order = 'asc'
                        } else {
                            item.order = 'desc'
                        }
                    } else {
                        this.$set(this.sortList[0], 'active', true)
                        this.$set(this.sortList[0], 'order', 'desc')
                        this.$set(this.sortList[1], 'active', false)
                        this.$set(this.sortList[1], 'order', 'desc')
                    }
                } else {
                    if (item.active) {
                        return;
                        if (item.order === 'desc') {
                            item.order = 'asc'
                        } else {
                            item.order = 'desc'
                        }
                    } else {
                        this.$set(this.sortList[0], 'active', false)
                        this.$set(this.sortList[0], 'order', 'desc')
                        this.$set(this.sortList[1], 'active', true)
                        this.$set(this.sortList[1], 'order', 'desc')
                    }
                }
                this.$emit('change', item);
            },
        }
    }
</script>

<style lang="scss"> 
    .sort-bar {
        padding: 35rpx;
        background-color: #fff;
        border-top: 1rpx solid #efefef;
        border-bottom: 1rpx solid #efefef;
    }

    .flex-row {
        display: flex;
        flex-direction: row;
        flex-wrap: nowrap;
        justify-content: space-between;
        align-items: flex-start;
    }

    .sort-area {

        .sort-item.active {
            color: $basic-active-color;
        }

        .sort-item {
            margin-right: 40rpx;

            .filter-text.active {
                color: $basic-active-color;
                border-bottom: 5rpx solid $basic-active-color;
            }

            .filter-text {
                padding-bottom: 10rpx;
            }

            .filter-icon {
                margin-left: 10rpx;
            }
        }
    }


</style>
