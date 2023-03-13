<template>
  <div class="mod-user">
    <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
      <el-form-item>
        <el-input v-model="dataForm.userName" placeholder="用户名" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()">查询</el-button>
      </el-form-item>
    </el-form>
    <el-table
      :data="dataList"
      border
      v-loading="dataListLoading"
      @selection-change="selectionChangeHandle"
      @sort-change="sortChange"
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
        sortable="custom"
        label="认证时间">
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
        prop="name"
        header-align="center"
        align="center"
        label="姓名">
      </el-table-column>
      <el-table-column
        prop="phone"
        header-align="center"
        align="center"
        label="手机号">
      </el-table-column>
      <el-table-column
        prop="star"
        header-align="center"
        align="center"
        width="150"
        sortable="custom"
        label="服务星级">
        <template slot-scope="scope">
          <el-rate
            v-model="scope.row.star"
            disabled
            text-color="#ff9900"
            score-template="scope.row.star">
          </el-rate>
        </template>
      </el-table-column>
      <el-table-column
        prop="advisoryCount"
        header-align="center"
        align="center"
        sortable="custom"
        label="咨询次数">
      </el-table-column>
      <el-table-column
        prop="buyHomeCount"
        header-align="center"
        align="center"
        sortable="custom"
        label="推荐置业次数">
      </el-table-column>
      <el-table-column
        prop="projectName"
        header-align="center"
        align="center"
        label="所属项目">
      </el-table-column>
      <el-table-column
        prop="status"
        header-align="center"
        align="center"
        label="状态">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === 1" size="small" type="info">认证中</el-tag>
          <el-tag v-if="scope.row.status === 2" size="small" type="success">认证成功</el-tag>
          <el-tag v-if="scope.row.status === 3" size="small" type="danger">认证失败</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="150"
        label="操作">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="addOrUpdateHandle(scope.row.id)">修改</el-button>
          <el-button type="text" size="small" @click="toComment(scope.row.id)">查看评价</el-button>
          <el-button type="text" v-if="isShow(scope.row)" size="small" @click="auditHandle(scope.row)">审核</el-button>
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
    <audit v-if="auditVisible" ref="audit" @refreshData="getDataList"></audit>
  </div>
</template>

<script>
  import AddOrUpdate from './consultant-add-or-update'
  import Audit from './audit'

  export default {
    name: 'consultant',
    data () {
      return {
        dataForm: {
          userName: '',
          desc: 'a.create_time',
          asc: ''
        },
        dataList: [],
        pageIndex: 1,
        pageSize: 10,
        totalPage: 0,
        dataListLoading: false,
        dataListSelections: [],
        addOrUpdateVisible: false,
        auditVisible: false,
        user: null
      }
    },
    components: {
      AddOrUpdate,
      Audit
    },
    activated () {
      this.getDataList()
      this.getUserInfo()
    },
    methods: {
      sortChange (column) {
        if (column.order === 'ascending') {
          if (column.prop === 'star') {
            this.dataForm.asc = 'a.star'
          }
          if (column.prop === 'buyHomeCount') {
            this.dataForm.asc = 'a.buy_home_count'
          }
          if (column.prop === 'advisoryCount') {
            this.dataForm.asc = 'a.advisory_count'
          }
          if (column.prop === 'createTime') {
            this.dataForm.asc = 'a.create_time'
          }
          this.dataForm.desc = ''
        } else if (column.order === 'descending') {
          if (column.prop === 'star') {
            this.dataForm.desc = 'a.star'
          }
          if (column.prop === 'buyHomeCount') {
            this.dataForm.desc = 'a.buy_home_count'
          }
          if (column.prop === 'advisoryCount') {
            this.dataForm.desc = 'a.advisory_count'
          }
          if (column.prop === 'createTime') {
            this.dataForm.desc = 'a.create_time'
          }
          this.dataForm.asc = ''
        } else {
          this.dataForm.desc = 'a.create_time'
          this.dataForm.asc = ''
        }
        this.getDataList()
      },
      isShow (data) {
        if (data.status === 1) {
          if (data.auditType === 1) {
            return this.user.roleIdList.some(item => [1, 2].includes(item))
          }
          if (data.auditType === 2) {
            if (this.user.userId === data.firmId) {
              return true
            }
          }
        }
        return false
      },
      // 获取数据列表
      getDataList () {
        this.dataListLoading = true
        this.$http({
          url: this.$http.adornUrl('/sys/api/getConsultantList'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.pageIndex,
            'size': this.pageSize,
            'desc': this.dataForm.desc,
            'asc': this.dataForm.asc,
            'type': 2
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
      auditHandle (data) {
        if (data.auditType === 1) {
          let isAdministrator = this.user.roleIdList.some(item => [1, 2].includes(item))
          if (isAdministrator) {
            this.auditVisible = true
            this.$nextTick(() => {
              this.$refs.audit.init(data)
            })
          } else {
            this.$message({
              message: '对不起，你没有操作权限',
              type: 'error',
              duration: 1500
            })
          }
        }
        if (data.auditType === 2) {
          if (this.user.userId === data.firmId) {
            this.auditVisible = true
            this.$nextTick(() => {
              this.$refs.audit.init(data)
            })
          } else {
            this.$message({
              message: '对不起，你没有操作权限',
              type: 'error',
              duration: 1500
            })
          }
        }
      },
      toComment (id) {
        this.$router.push({name: 'consultant-comment', query: {id: id}})
      }
    }
  }
</script>

<style>
  .avatar-url-img {
    max-width: 50px;
    max-height: 50px;
  }
</style>
