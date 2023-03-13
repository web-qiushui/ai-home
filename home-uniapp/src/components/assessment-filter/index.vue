<template>
    <view class="filter-container">
        <van-dropdown-menu class="filter-bar" overlay="true" close-on-click-overlay="true"  active-color="#c30D23"
                           close-on-click-outside="true">
            <van-dropdown-item :value="filter.regionId" :options="regionOption" @change="onRegionChange" :disabled="disabled"
                               :title="filter.regionTitle" class="region-popup" :popup-style="popupStyle" @open="open" @close="close"/>
            <van-dropdown-item :value="filter.priceId" :options="priceOption" @change="onPriceChange" :disabled="disabled"
                               :title="filter.priceTitle" class="price-popup" :popup-style="popupStyle" @open="open" @close="close"/>
            <van-dropdown-item :value="filter.areaId" :options="areaOption" @change="onAreaChange" :disabled="disabled"
                               :title="filter.areaTitle" class="area-popup" :popup-style="popupStyle" @open="open" @close="close"/>
            <van-dropdown-item id="more" class="more-popup" :title="moreTitle" :disabled="disabled" @open="open" @close="close">
                <view class="more-popup-container">
                    <view class="more-item-label">{{unitTypesTitle}}</view>
                    <view class="select-group-container">
                        <view class="select-item-container" v-for="(item, index) in unitTypes" :key="index">
                            <view class="select-item-box" :class="{'selected': item.selected}" @tap="select(item)">
                                {{item.text}}
                            </view>
                        </view>
                    </view>
                    <view class="more-item-label">{{typesTitle}}</view>
                    <view class="select-group-container">
                        <view class="select-item-container" v-for="(item, index) in types" :key="index">
                            <view class="select-item-box" :class="{'selected': item.selected}" @tap="select(item)">
                                {{item.text}}
                            </view>
                        </view>
                    </view>
                    <view class="more-item-label">{{othersTitle}}</view>
                    <view class="select-group-container">
                        <view class="select-item-container" v-for="(item, index) in others" :key="index">
                            <view class="select-item-box" :class="{'selected': item.selected}" @tap="select(item)">
                                {{item.text}}
                            </view>
                        </view>
                    </view>

                    <view class="action-button-container">
                        <view class="action-button">
                            <view class="sort-button" @tap="resetMorePanel()">重置</view>
                        </view>
                        <view class="action-button">
                            <view class="sort-button primary" @tap="confirm()">确认</view>
                        </view>
                    </view>
                </view>
            </van-dropdown-item>
        </van-dropdown-menu>
    </view>
</template>

<script>
    import apiservice from '../../api/apiservice'

    export default {
        props: ['disabled'],
        data() {
            return {
                filter: {
                    regionId: 0,
                    regionTitle: '区域',
                    region: '',
                    priceId: 0,
                    priceTitle: '价格',
                    price: '',
                    areaId: 0,
                    areaTitle: '面积',
                    area: '',
                },
                regionOption: [],
                priceOption: [],
                areaOption: [],
                moreTitle: '更多',
                unitTypesTitle: '户型',
                unitTypes: [],
                typesTitle: '类型',
                types: [],
                othersTitle: '其他',
                others: [],
                selectedList: [],
                list: [],
                popupStyle: 'padding-bottom: 100rpx; background: rgba(0,0,0,.0);'
            }
        },
        created() {
            this.getDataDict()
        },
        methods: {
            open(){
                this.$emit('open');
            },
            close(){
                console.log('page closed')
                this.$emit('close');
            },
            reset() {
                this.filter = {
                    regionId: 0,
                    regionTitle: '区域',
                    region: '',
                    priceId: 0,
                    priceTitle: '价格',
                    price: '',
                    areaId: 0,
                    areaTitle: '面积',
                    area: '',
                }
                this.resetMorePanel()
                this.emit()
            },
            getDataDict() {
                const that = this
                const dataDict = uni.getStorageSync('dataDict');
                if (dataDict) {
                    this.initData(dataDict)
                } else {
                    apiservice.getDataDict().then(data => {
                        uni.setStorageSync('dataDict', data);
                        that.initData(data)
                    });
                }

            },
            initData (list) {
                this.list = list;
                this.regionOption = this.getForDropDown(list, 'REGION_SELECT')
                this.priceOption = this.getForDropDown(list, 'PRICE_SELECT')
                this.areaOption = this.getForDropDown(list, 'AREA_SELECT')
                this.unitTypes = this.get(list, 'UNIT_TYPE_SELECT')
                this.types = this.get(list, 'TYPE_SELECT')
                this.others = this.get(list, 'OTHER_SELECT')
            },
            getForDropDown (list, key) {
                return list.filter(item => item.n1 === key || item.type === key).map(item => {
                    return {
                        id: item.id,
                        text: item.type === key ? '全部' : item.name,
                        name: item.type === key ? '' : item.name,
                        value: item.id,
                        type: item.type === key ? '' : item.type,
                        isAll: item.type === key,
                        selected: false
                    }
                })
            },
            get(list, key) {
                return list.filter(item => item.n1 === key).map(item => {
                    return {
                        id: item.id,
                        text: item.name,
                        name: item.name,
                        value: item.id,
                        type: item.type,
                        selected: false
                    }
                })
            },
            onRegionChange(e) {
                const item = this.regionOption.find(item => item.id === e.detail)
                if (item) {
                    this.$set(this.filter, 'region', item.name)
                    this.$set(this.filter, 'regionId', item.id)
                    this.$set(this.filter, 'regionTitle', item.isAll ? '区域' : item.text)
                }
                this.emit()
                console.log('onRegion change', e, item)
            },
            onPriceChange(e) {
                const item = this.priceOption.find(item => item.id === e.detail)
                if (item) {
                    this.$set(this.filter, 'price', item.name)
                    this.$set(this.filter, 'priceId', item.id)
                    this.$set(this.filter, 'priceTitle', item.isAll ? '价格' : item.text)
                }
                this.emit()
            },
            onAreaChange(e) {
                const item = this.areaOption.find(item => item.id === e.detail)
                if (item) {
                    this.$set(this.filter, 'area', item.name)
                    this.$set(this.filter, 'areaId', item.id)
                    this.$set(this.filter, 'areaTitle', item.isAll ? '面积' : item.text)
                }
                this.emit()
            },
            select(item) {
                item.selected = !item.selected;
            },
            resetMorePanel() {
                this.unitTypes = this.get(this.list, 'UNIT_TYPE_SELECT')
                this.types = this.get(this.list, 'TYPE_SELECT')
                this.others = this.get(this.list, 'OTHER_SELECT')
            },
            confirm() {
                this.emit();
                this.selectComponent('#more').toggle();
            },
            getSelectedList() {
                const unitTypes = this.unitTypes.filter(item => item.selected).map(item => item.name);
                const types = this.types.filter(item => item.selected).map(item => item.name);
                const others = this.others.filter(item => item.selected).map(item => item.name);
                return [...unitTypes, ...types, ...others].join(',')
            },
            emit() {
                const filter = {
                    region: this.filter.region || '',
                    price: this.filter.price || '',
                    area: this.filter.area || '',
                    type: this.getSelectedList()
                }
                this.$emit('change', filter);
            }
        }
    }
</script>

<style lang="scss">

    .region-popup {
        .van-popup {
            width: 200rpx;
            left: 20rpx;
            padding-bottom: 100rpx;
        }
    }
    .price-popup {
        .van-popup {
            width: 200rpx;
            left: 220rpx;
            padding-bottom: 100rpx;
        }
    }
    .area-popup {
        .van-popup {
            width: 200rpx;
            left: 520rpx;
            padding-bottom: 100rpx;
        }
    }
    .more-popup-container {
        padding: 40rpx;
        background-color: #ffffff;
        padding-bottom: 120rpx;

        .more-item-label {
            margin-top: 20rpx;
            margin-bottom: 20rpx;
            font-size: 15px;
            font-weight: bold;
        }

        .select-group-container {
            display: flex;
            flex-direction: row;
            flex-wrap: wrap;
            .select-item-container {
                display: inline-block;
                margin-right: 5px;
                margin-bottom: 5px;
                .select-item-box {
                    padding: 10px 20px!important;
                    border-radius: 4px;
                    background-color: #EEEEEE;
                    text-align: center;
                    color:#000000;
                }
                .select-item-box.selected {
                    background-color: $basic-active-color;
                    color: #fff;
                }
            }

        }

        .action-button-container {
            display: flex;
            flex-direction: row;
            flex-wrap: nowrap;
            align-items: center;
            justify-content: space-between;
            margin-top: 20rpx;

            .action-button {
                width: 45%;
            }

            .sort-button {
                padding: 20rpx;
                margin-right: 10rpx;
                border-radius: 10rpx;
                background-color: #EEEEEE;
                text-align: center;
            }

            .sort-button.primary {
                background-color: $basic-active-color;
                color: #fff;
            }
        }

    }

    .filter-container {
        .van-cell {
            background-color: #efefef;
        }
        .van-dropdown-menu__title {
            background-color: #efefef !important;
            padding: 10rpx 50rpx 10rpx 50rpx !important;
        }

    }

</style>
