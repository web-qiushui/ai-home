Component({
    properties: {
        disabled: Boolean,
        unsafe: {
            type: Boolean,
            value: false
        }
    },
    data: {
        selected: 0,
        color: "#7A7E83",
        selectedColor: "#007AFF",
        list: [{
                "pagePath": "/pages/home/home",
                "iconPath": "/static/ai-icon-home.png",
                "selectedIconPath": "/static/ai-icon-home-active.png",
                "text": "首页",
                "icon": 'home-o',
            },
            {
                "pagePath": "/pages/anouncement/anouncement",
                "iconPath": "/static/ai-icon-add.png",
                "selectedIconPath": "/static/ai-icon-add-active.png",
                "text": "发布",
                "icon": 'add-o',
            },
            {
                "pagePath": "/pages/message/message",
                "iconPath": "/static/ai-icon-msg.png",
                "selectedIconPath": "/static/ai-icon-msg-active.png",
                "text": "消息",
                "icon": 'chat-o',
            },
            {
                "pagePath": "/pages/me/me",
                "iconPath": "/static/ai-icon-me.png",
                "selectedIconPath": "/static/ai-icon-me-active.png",
                "text": "我的",
                "icon": 'manager-o',
            }]
    },
    attached() {
    },
    methods: {
        onChange(event) {
            if (this.properties.disabled) {
                return;
            }
            const token = wx.getStorageSync('token');
            const url = this.data.list[event.detail].pagePath;
            if (event.detail === 0) {
                wx.switchTab({url})
            } else {
                if (token) {
                    wx.switchTab({url})
                } else {
                    wx.navigateTo({
                        url: '/pages/login/login',
                    })
                }
            }
        },
        switchTab(e) {
            const token = wx.getStorageSync('token');
            const data = e.currentTarget.dataset
            const url = data.path
            if (data.index === 0) {
                wx.switchTab({url})
            } else {
                if (token) {
                    wx.switchTab({url})
                } else {
                    wx.navigateTo({
                        url: '/pages/login/login',
                    })
                }
            }
        }
    }
})
