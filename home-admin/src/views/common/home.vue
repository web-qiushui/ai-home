<template>
  <div class="mod-home" v-if="isShow()">
    <!-- 首页内容 -->
    <div class="home-banner">
      <el-row :gutter="10">
        <el-col :span="banner.span" v-for="(banner, index) in banners" :key="index">
          <div class="grid-content" :style="getBackground(banner.color)">
            <div class="grid-count">
              {{banner.count}}
            </div>
            <div class="grid-title">
              {{banner.name}}
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
    <div class="chart-panel">
      <el-card>
        <div class="select-panel">
          <el-select v-model="chart.type" placeholder="请选择" @change="chartChange">
            <el-option
              v-for="item in chart.options"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </div>
        <div id="chartBarBox" class="chart-box"></div>
      </el-card>
    </div>
  </div>
</template>

<script>
  import echarts from 'echarts'
  export default {
    name: 'home',
    data () {
      return {
        user: null,
        chartInstance: null,
        chart: {
          type: 'day',
          options: [{
            value: 'day',
            label: '今日'
          }, {
            value: 'month',
            label: '本月'
          }, {
            value: 'year',
            label: '总计'
          }]
        },
        banners: [{
          id: 'onlineCount',
          name: '在线人数',
          label: '在线人数',
          count: 0,
          color: '#2ec7c9',
          span: 3
        }, {
          id: 'commentCount',
          name: '点评数量',
          label: '点评数量',
          count: 0,
          color: '#b6a2de',
          span: 3
        }, {
          id: 'forwardCount',
          name: '转发数量',
          label: '转发数量',
          count: 0,
          color: '#5ab1ef',
          span: 3
        }, {
          id: 'collectCount',
          name: '收藏数量',
          label: '收藏数量',
          count: 0,
          color: '#ffb980',
          span: 3
        }, {
          id: 'demandCount',
          name: '发布购房需求数量',
          label: '发布购房需求数量',
          count: 0,
          color: '#d87a80',
          span: 4
        }, {
          id: 'evaluationCount',
          name: '发布测评数量',
          label: '发布测评数量',
          count: 0,
          color: '#8d98b3',
          span: 4
        }, {
          id: 'newsCount',
          name: '发布快讯数量',
          label: '发布快讯数量',
          count: 0,
          color: '#97b552',
          span: 4
        }]
      }
    },
    components: {
    },
    activated () {
        this.getUserInfo()
    },
    created () {
      // this.getTodayStatistics()
    },
    mounted () {
      this.getStatistics('day')
    },
    computed: {
      sidebarFold() {
        return this.$store.state.common.sidebarFold
      }
    },
    watch: {
      sidebarFold() {
        setTimeout(()=> {
          this.chartInstance.resize()
        }, 100)
      }
    },
    methods: {
      // 获取当前管理员信息
      getUserInfo () {
          this.$http({
              url: this.$http.adornUrl('/sys/user/info'),
              method: 'get',
              params: this.$http.adornParams()
          }).then(({data}) => {
              if (data && data.code === 0) {
                  this.user = data.user
              }
          })
      },
      isShow () {
          try {
              return this.user.roleIdList.some(item => [1, 2].includes(item))
          } catch (e) {
              return false
          }
      },
      chartChange(type) {
        console.log('type', type)
        this.getStatistics(type)
      },
      getTodayStatistics() {
        this.$http({
          url: this.$http.adornUrl('/sys/api/getStatistics'),
          method: 'get',
          params: this.$http.adornParams({
            type: 'day'
          })
        }).then(({data}) => {
          this.banners.forEach(banner => {
            banner.count = data.data[banner.id]
          })

        })
      },
      getStatistics(type = 'day') {
        this.$http({
          url: this.$http.adornUrl('/sys/api/getStatistics'),
          method: 'get',
          params: this.$http.adornParams({
            type: type
          })
        }).then(({data}) => {
          let prefix = ''
          let suffix = ''
          if (type === 'month') {
            prefix = '本月'
          } else if (type === 'day') {
            prefix = '今日'
          } else {
            suffix = '总计'
          }
          this.banners.forEach(banner => {
            banner.count = data.data[banner.id]
            banner.name = prefix + banner.label + suffix;
          })
          const dataset = this.initStatisticsDataset(data.data)
          console.log('dataset', dataset)
          this.initCharts(dataset)
        })
      },
      initStatisticsDataset(data) {
          const chartData = [
            // ['product', '在线人数', '点评数量', '转发数量', '收藏数量', '发布购房需求数量', '发布测评数量', '发布快讯数量'],
            ['product', ''],
            ['在线人数', data.onlineCount],
            ['点评数量', data.commentCount],
            ['转发数量', data.forwardCount],
            ['收藏数量', data.collectCount],
            ['发布购房需求数量', data.demandCount],
            ['发布测评数量', data.evaluationCount],
            ['发布快讯数量', data.newsCount],
          ]
          // const newdata =
          //   ['', data.onlineCount, data.commentCount, data.forwardCount, data.collectCount, data.demandCount, data.evaluationCount, data.newsCount]
          // chartData.push(newdata);
          return chartData;
      },
      getBackground(color) {
        return `background: ${color}`
      },
      initCharts(source) {
        const option = {
          tooltip: {
              trigger: 'axis',
              axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                  type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
              }
          },
          legend: {
              // data: ['在线人数', '点评数量', '转发数量', '收藏数量', '发布购房需求数量', '发布测评数量', '发布快讯数量']
          },
          color: ['#2ec7c9','#b6a2de', '#5ab1ef', '#ffb980', '#d87a80','#8d98b3',  '#97b552', '#bda29a','#6e7074', '#546570', '#c4ccd3'],
          grid: {
              left: '1%',
              right: '40',
              bottom: '10%',
              containLabel: true
          },
          dataset: {
            source: source
          },
          xAxis: {
            type: 'category',
            // data: ['在线人数', '点评数量', '转发数量', '收藏数量', '发布购房需求数量', '发布测评数量', '发布快讯数量']
          },
          yAxis: {},
          dataZoom: [
              // {
              //     show: true,
              //     start: 94,
              //     end: 100
              // },
              // {
              //     type: 'inside',
              //     start: 94,
              //     end: 100
              // },
              // {
              //     show: true,
              //     yAxisIndex: 0,
              //     filterMode: 'empty',
              //     width: 30,
              //     height: '80%',
              //     showDataShadow: false,
              //     right: '0'
              // }
          ],
          series: [
              {
                  type: 'bar',
                  itemStyle: {
                    normal: {
                        color: function(params) {
                            // build a color map as your need.
                            const colorList = ['#2ec7c9','#b6a2de', '#5ab1ef', '#ffb980', '#d87a80','#8d98b3',  '#97b552', '#bda29a','#6e7074', '#546570', '#c4ccd3']
                            return colorList[params.dataIndex]
                        }
                    }
                  }
              },
              // {
              //     type: 'bar'
              // },
              // {
              //     type: 'bar'
              // },
              // {
              //     type: 'bar'
              // },
              // {
              //     type: 'bar'
              // },
              // {
              //     type: 'bar'
              // },
              // {
              //     type: 'bar'
              // }
          ]
        };

        const dom = window.document.getElementById('chartBarBox')
        console.log('dom', dom)
        this.chartInstance = echarts.init(dom)
        this.chartInstance.setOption(option)
        window.addEventListener('resize', () => {
          this.chartInstance.resize()
        })
      }
    }

  }
</script>

<style lang="scss">
  .mod-home {
    line-height: 1.5;
  }

  .el-row {
    margin-bottom: 20px;
    &:last-child {
      margin-bottom: 0;
    }
  }
  .el-col {
    border-radius: 3px;
  }
  .bg-purple-dark {
    background: #99a9bf;
  }
  .bg-purple {
    background: #d3dce6;
  }
  .bg-purple-light {
    background: #e5e9f2;
  }

  .grid-content {
    height: 80px;
    text-align: center;
    border-radius: 3px;

    .grid-count {
      font-size: 35px;
      color: #fff;
    }

    .grid-title {
      color: #fff;
    }
  }
  .row-bg {
    padding: 10px 0;
    background-color: #f9fafc;
  }

  .chart-panel {
    margin-top: 30px;

    .select-panel {
      display: flex;
      flex-direction: row;
      flex-wrap: nowrap;
      justify-content: flex-end;
      align-items: center;
    }

    .chart-box {
      min-height: 400px;
    }
  }
</style>

