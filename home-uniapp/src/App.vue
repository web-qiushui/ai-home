<script>
    import apiservice from './api/apiservice'
    export default {
        onLaunch: function () {
            console.log('App Launch');
            // #ifdef APP-NVUE
            plus.screen.lockOrientation('portrait-primary');

            let appid = plus.runtime.appid;
            if (appid && appid.toLocaleLowerCase() != "hbuilder") {
                uni.request({
                    url: 'https://uniapp.dcloud.io/update', //检查更新的服务器地址
                    data: {
                        appid: plus.runtime.appid,
                        version: plus.runtime.version
                    },
                    success: (res) => {
                        console.log('success', res);
                        if (res.statusCode == 200 && res.data.isUpdate) {
                            let openUrl = plus.os.name === 'iOS' ? res.data.iOS : res.data.Android;
                            // 提醒用户更新
                            uni.showModal({
                                title: '更新提示',
                                content: res.data.note ? res.data.note : '是否选择更新',
                                success: (showResult) => {
                                    if (showResult.confirm) {
                                        plus.runtime.openURL(openUrl);
                                    }
                                }
                            })
                        }
                    }
                })
            }

            var domModule = weex.requireModule('dom');
            domModule.addRule('fontFace', {
                'fontFamily': "texticons",
                'src': "url('./static/text-icon.ttf')"
            });
            // #endif
        },
        onShow: function () {
            console.log('App Show')
            // uni.clearStorageSync();
            this.clearStorages();
            this.preload()
        },
        onHide: function () {
            console.log('App Hide')
        },
        onLaunch() {
            console.log('App on Lunch')
            this.checkToken().then(res => {
                this.socketConnect();
            }, err => {
                console.log('error')
            })
        },
        data() {
            return {
                $imageHost: this.$imageHost
            }
        },
        methods: {
            preload() {
                this.getWebsiteConfig();
                this.getSwipers();
                this.getKingKong();
                this.getDictData();
                this.getGuideConfig();
            },
            getWebsiteConfig() {
                apiservice.getWebsiteConfig().then(data => {
                    console.log('web config', data)
                    uni.setStorageSync('websiteConfig', data);
                });
            },
            getSwipers() {
                apiservice.getCarouselList().then(data => {
                    console.log('carousel data', data)
                    uni.setStorageSync('swipers', data);
                })
            },
            getKingKong() {
                apiservice.getKingKongConfig().then(data => {
                    console.log('king kong config', data)
                    uni.setStorageSync('kingkong', data);
                })
            },
            getDictData() {
                apiservice.getDataDict().then(data => {
                    console.log('data dict', data)
                    uni.setStorageSync('dataDict', data);
                });
            },
            getGuideConfig() {
                apiservice.getNewComerGuideConfig().then(data => {
                    console.log('guide config', data);
                    uni.setStorageSync('guideConfig', data);
                })
            },
            socketConnect() {
                console.log('socket connect in app')
                const token = uni.getStorageSync('token')
                if (token) {
                    this.$store.dispatch('message/init', this.$socketHost)
                }
            },
            clearStorages() {
                uni.removeStorageSync('websiteConfig');
                uni.removeStorageSync('kingkong')
                uni.removeStorageSync('swipers')
                uni.removeStorageSync('guideConfig');
                // uni.removeStorageSync('dataDict');
                // uni.removeStorageSync('token');
            },
            checkToken() {
                const token = uni.getStorageSync('token')
                return new Promise((resolve, reject) => {
                    if (token) {
                        return apiservice.refreshToken({
                            token: token
                        }).then(res => {
                            console.log('refresh token success', res)
                            uni.setStorageSync('token', token)
                            resolve()
                        }, err => {
                            uni.removeStorageSync('token');
                            console.log('refresh token err', err)
                            reject()
                        })
                    } else {
                        reject()
                    }
                });
            }
        }
    }
</script>

<style lang='scss'>
    /*每个页面公共css */
    @import '/wxcomponents/@vant/weapp/common/index.wxss';
    page {
        font-family: $basic-regular-font;
    }
</style>
