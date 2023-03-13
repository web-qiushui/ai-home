<template>
  <div class="mod-user">
    <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
      <el-form-item>
        <el-input v-model="dataForm.phone" placeholder="手机号" clearable></el-input>
      </el-form-item>
      <el-form-item label="区域" prop="region">
        <el-select v-model="dataForm.region" clearable placeholder="请选择">
          <el-option
            v-for="item in regionOptions"
            :key="item.key"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="价格" prop="price" >
        <el-select v-model="dataForm.price" clearable placeholder="请选择">
          <el-option
            v-for="item in priceOptions"
            :key="item.key"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="面积" prop="area" >
        <el-select v-model="dataForm.area" clearable placeholder="请选择">
          <el-option
            v-for="item in areaOptions"
            :key="item.key"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()">查询</el-button>
        <el-button v-if="isAuth('sys:user:save')" type="primary" @click="addOrUpdateHandle()">新增</el-button>
      </el-form-item>
    </el-form>
    <el-table
      :data="dataList"
      border
      v-loading="dataListLoading"
      @selection-change="selectionChangeHandle"
      style="width: 100%;">
      <el-table-column
        prop="id"
        header-align="center"
        align="center"
        width="80"
        label="ID">
      </el-table-column>
      <el-table-column
        prop="createTime"
        header-align="center"
        align="center"
        label="创建时间">
      </el-table-column>
      <el-table-column
        prop="nickName"
        header-align="center"
        align="center"
        label="昵称">
      </el-table-column>
      <el-table-column
        prop="avatarUrl"
        header-align="center"
        align="center"
        label="头像">
        <template slot-scope="scope">
          <img class="avatar-url-img" :src="scope.row.avatarUrl"/>
        </template>
      </el-table-column>
      <el-table-column
        prop="phone"
        header-align="center"
        align="center"
        label="手机号">
      </el-table-column>
      <el-table-column
        prop="region"
        header-align="center"
        align="center"
        label="区域">
      </el-table-column>
      <el-table-column
        prop="price"
        header-align="center"
        align="center"
        label="价格">
      </el-table-column>
      <el-table-column
        prop="area"
        header-align="center"
        align="center"
        label="面积">
      </el-table-column>
      <el-table-column
        prop="label"
        header-align="center"
        align="center"
        label="标签">
        <template slot-scope="scope">
          <el-tag class="evaluation-tag" v-for="(tag, index) in scope.row.label" :key="index" size="small">{{tag}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="intentions"
        header-align="center"
        align="center"
        label="意向楼盘">
      </el-table-column>
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="150"
        label="操作">
        <template slot-scope="scope">
          <el-button type="text" size="small"
                     @click="addOrUpdateHandle(scope.row.id)">修改
          </el-button>
          <el-button type="text" size="small"
                     @click="viewHandle(scope.row.id)">查看
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      @size-change="sizeChangeHandle"
      @current-change="currentChangeHandle"
      :current-page="pageIndex"
      :page-sizes="[10, 20, 50, 100]"
      :page-size="pageSize"
      :total="totalPage"
      layout="total, sizes, prev, pager, next, jumper">
    </el-pagination>
    <!-- 弹窗, 新增 / 修改 -->
    <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update>
    <report-view v-if="viewVisible" ref="reportView"></report-view>
  </div>
</template>

<script>
  import AddOrUpdate from './report-add-or-update'
  import ReportView from './report-view'

  export default {
    name: 'report',
    data () {
      return {
        dataForm: {
          // address: "",
          // asc: [],
          // brand: "",
          // copyright: "",
          // desc: [],
          // disclaimer: "",
          // discount: "",
          // homeConsultantId: 0,
          // homeDemandId: 0,
          // homeUserId: 0,
          // imageUrl: "",
          // page: 0,
          // price: "",
          // projectAdvantage: "",
          // projectDisadvantage: "",
          // realEstate: "",
          // relatedSuggestion: "",
          // size: 0,
          // startTime: "",
          // endTime: "",
          // suitability: ""
        },
        dataList: [],
        regionOptions: [],
        priceOptions: [],
        areaOptions: [],
        tagOptions: [],
        pageIndex: 1,
        pageSize: 10,
        totalPage: 0,
        dataListLoading: false,
        dataListSelections: [],
        addOrUpdateVisible: false,
        viewVisible: false
      }
    },
    components: {
      AddOrUpdate,
      ReportView
    },
    activated () {
      this.initOptions()
      this.getDataList()
    },
    methods: {
      initOptions () {
        const datadict = Object.assign([], this.$store.state.common.datadict)
        console.log(datadict)
        this.regionOptions = this.getForDropDown(datadict, 'REGION_SELECT')
        this.priceOptions = this.getForDropDown(datadict, 'PRICE_SELECT')
        this.areaOptions = this.getForDropDown(datadict, 'AREA_SELECT')

        const unitTypes = this.get(datadict, 'UNIT_TYPE_SELECT')
        const types = this.get(datadict, 'TYPE_SELECT')
        const others = this.get(datadict, 'OTHER_SELECT')

        this.tagOptions = [...unitTypes, ...types, ...others]
      },
      getForDropDown (list, key) {
        return list.filter(item => item.n1 === key).map(item => {
          return {
            id: item.id,
            key: item.id,
            label: item.name,
            value: item.name,
            type: item.type
          }
        })
      },
      get (list, key) {
        return list.filter(item => item.n1 === key).map(item => {
          return {
            id: item.id,
            label: item.name,
            value: item.name,
            type: item.type
          }
        })
      },
      formatterProject (row, column, cellValue, index) {
        if (cellValue != null) {
          return cellValue.name
        } else {
          return ''
        }
      },
      // 获取数据列表
      getDataList () {
        this.dataListLoading = true
        this.$http({
          url: this.$http.adornUrl('/sys/api/getHomeReportList'),
          method: 'get',
          params: this.$http.adornParams({
            ...this.dataForm,
            'page': this.pageIndex,
            'size': this.pageSize
          })
        }).then(({data}) => {
          if (data && data.code === 200) {
            this.dataList = data.data.list
            this.totalPage = data.data.total
          } else {
            this.dataList = []
            this.totalPage = 0
          }
          this.dataListLoading = false
        })
      },
      // 每页数
      sizeChangeHandle (val) {
        this.pageSize = val
        this.pageIndex = 1
        this.getDataList()
      },
      // 当前页
      currentChangeHandle (val) {
        this.pageIndex = val
        this.getDataList()
      },
      // 多选
      selectionChangeHandle (val) {
        this.dataListSelections = val
      },
      // 新增 / 修改
      addOrUpdateHandle (id) {
        this.addOrUpdateVisible = true
        this.$nextTick(() => {
          this.$refs.addOrUpdate.init(id)
        })
      },
      // 查看
      viewHandle (id) {
        this.viewVisible = true
        this.$nextTick(() => {
          this.$refs.reportView.init(id)
        })
      },
      // 删除
      deleteHandle (id) {
        var ids = id ? [id] : this.dataListSelections.map(item => {
          return item.userId
        })
        this.$confirm(`确定对[id=${ids.join(',')}]进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('/sys/report/delete'),
            method: 'post',
            data: this.$http.adornData(userIds, false)
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.$message({
                message: '操作成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  this.getDataList()
                }
              })
            } else {
              this.$message.error(data.msg)
            }
          })
        }).catch(() => {})
      }
    }
  }
</script>

<style scoped>
  .avatar-url-img {
    max-width: 50px;
    max-height: 50px;
  }
</style>
