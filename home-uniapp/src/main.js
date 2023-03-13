import Vue from 'vue'
import App from './App'
import config from './common/config'
import store from './store'

if (process.env.NODE_ENV === 'development') {
    import('./mock')
}

if (config.IS_MOCK) {
    uni.setStorageSync("token", 'mockTOkensss');
}

Vue.config.productionTip = false

Vue.prototype.$host = config.host;
Vue.prototype.$imageHost = config.imageHost;
Vue.prototype.$socketHost = config.socketHost;

Vue.mixin({
    methods: {
        setTabBarIndex(index) {
            if (typeof this.$mp.page.getTabBar === 'function' &&
                this.$mp.page.getTabBar()) {
                this.$mp.page.getTabBar().setData({
                    selected: index
                })
            }
        },
        getImageUrl(url) {
            if (url) {
                if (url.indexOf('http') > -1) {
                    return url;
                }
                return config.imageHost + url;
            }
            return url;
        },
        guid() {
            return (this.S4() + this.S4() + "-" + this.S4() + "-" + this.S4() + "-" + this.S4() + "-" + this.S4() + this.S4() + this.S4());
        },
        S4() {
            return (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1);
        }
    }
})

App.mpType = 'app'

const app = new Vue({
    ...App,
    store
})
app.$mount()
