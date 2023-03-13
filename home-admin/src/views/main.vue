<template>
  <div
    class="site-wrapper"
    :class="{ 'site-sidebar--fold': sidebarFold }"
    v-loading.fullscreen.lock="loading"
    element-loading-text="拼命加载中">
    <template v-if="!loading">
      <main-navbar />
      <main-sidebar />
      <div class="site-content__wrapper" :style="{ 'min-height': documentClientHeight + 'px' }">
        <main-content v-if="!$store.state.common.contentIsNeedRefresh" />
      </div>
    </template>
  </div>
</template>

<script>
  import MainNavbar from './main-navbar'
  import MainSidebar from './main-sidebar'
  import MainContent from './main-content'
  export default {
    provide () {
      return {
        // 刷新
        refresh () {
          this.$store.commit('common/updateContentIsNeedRefresh', true)
          this.$nextTick(() => {
            this.$store.commit('common/updateContentIsNeedRefresh', false)
          })
        }
      }
    },
    data () {
      return {
        loading: true
      }
    },
    components: {
      MainNavbar,
      MainSidebar,
      MainContent
    },
    computed: {
      documentClientHeight: {
        get () { return this.$store.state.common.documentClientHeight },
        set (val) { this.$store.commit('common/updateDocumentClientHeight', val) }
      },
      sidebarFold: {
        get () { return this.$store.state.common.sidebarFold }
      },
      userId: {
        get () { return this.$store.state.user.id },
        set (val) { this.$store.commit('user/updateId', val) }
      },
      userName: {
        get () { return this.$store.state.user.name },
        set (val) { this.$store.commit('user/updateName', val) }
      },
      datadict: {
        get () { return this.$store.state.common.datadict },
        set (val) { this.$store.commit('common/updateDatadict', val) }
      },
      datadictType: {
        get () { return this.$store.state.common.datadictType },
        set (val) { this.$store.commit('common/updateDatadictType', val) }
      }
    },
    created () {
      this.getUserInfo()
      this.getDataDitcById()
    },
    mounted () {
      this.resetDocumentClientHeight()
    },
    methods: {
      // 重置窗口可视高度
      resetDocumentClientHeight () {
        this.documentClientHeight = document.documentElement['clientHeight']
        window.onresize = () => {
          this.documentClientHeight = document.documentElement['clientHeight']
        }
      },
      // 获取当前管理员信息
      getUserInfo () {
        this.$http({
          url: this.$http.adornUrl('/sys/user/info'),
          method: 'get',
          params: this.$http.adornParams()
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.loading = false
            this.userId = data.user.userId
            this.userName = data.user.username
          }
        })
      },
       // 获取数据字典
      getDataDitcById () {
        this.$http({
          url: this.$http.adornUrl('/sys/datadict/anon/getDatadict'),
          method: 'get'
        }).then(({data}) => {
          if (data && data.code === 200) {
            let database = {}
            let statusType = {}
            data.data.forEach((res) => {
              database[res.id] = res
              if (statusType[res.type] == null) {
                let type = []
                type.push(res)
                statusType[res.type] = type
              } else {
                statusType[res.type].push(res)
              }
            })
            this.datadict = database
            this.datadictType = statusType
          }
        })
      }
    }
  }
</script>
