<template>
  <div class="mod-user">
    <el-form
      :inline="true"
      :model="dataForm"
      @keyup.enter.native="getDataList()"
    >
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
      style="width: 100%"
    >
      <el-table-column
        prop="id"
        header-align="center"
        align="center"
        label="ID"
      >
      </el-table-column>
      <el-table-column
        prop="createTime"
        header-align="center"
        align="center"
        label="评论时间"
      >
      </el-table-column>
      <el-table-column
        prop="nickName"
        header-align="center"
        align="center"
        label="昵称"
      >
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
        prop="likeCount"
        header-align="center"
        align="center"
        label="点赞数"
      >
      </el-table-column>
      <el-table-column
        prop="comment"
        width="450"
        header-align="center"
        align="center"
        label="评论"
      >
      </el-table-column>
      <el-table-column
        prop="status"
        header-align="center"
        align="center"
        label="状态"
      >
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === 1" size="small" type="info">未审核</el-tag>
          <el-tag v-if="scope.row.status === 2" size="small" type="success">审核通过</el-tag>
          <el-tag v-if="scope.row.status === 3" size="small" type="danger">审核失败</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="150"
        label="操作"
      >
        <template slot-scope="scope">
          <el-button v-if="scope.row.status === 1" type="text" size="small" @click="throughHandle(scope.row.id)">通过
          </el-button>
          <el-button v-if="scope.row.status === 1" type="text" size="small" @click="addOrUpdateHandle(scope.row.id)">拒绝
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
      layout="total, sizes, prev, pager, next, jumper"
    >
    </el-pagination>
    <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update>
  </div>
</template>

<script>
  import AddOrUpdate from './audit-add-or-update'
  export default {
    data () {
      return {
        dataForm: {
          asc: [],
          desc: [],
          endTime: '',
          evaluationId: 0,
          remark: '',
          startTime: ''
        },
        dataList: [],
        pageIndex: 1,
        pageSize: 10,
        totalPage: 0,
        dataListLoading: false,
        dataListSelections: [],
        addOrUpdateVisible: false
      }
    },
    components: {
      AddOrUpdate
    },
    created () {
    },
    activated () {
      this.dataForm.evaluationId = parseInt(this.$route.query.id)
      this.getDataList()
    },
    methods: {
      // 获取数据列表
      getDataList () {
        this.dataListLoading = true
        this.$http({
          url: this.$http.adornUrl('/sys/api/getEvaluationComment'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.pageIndex,
            'size': this.pageSize,
            ...this.dataForm
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
      // 审核
      throughHandle (id) {
        this.$http({
          url: this.$http.adornUrl('/sys/api/auditEvaluationComment'),
          method: 'post',
          data: this.$http.adornData({
            'evaluationId': id,
            'status': 2
          })
        }).then(({data}) => {
          if (data && data.code === 200) {
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
      },
      // 删除
      deleteHandle (id) {
        var userIds = id
          ? [id]
          : this.dataListSelections.map(item => {
            return item.userId
          })
        this.$confirm(
          `确定对[id=${userIds.join(',')}]进行[${id ? '删除' : '批量删除'}]操作?`,
          '提示',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }
        )
          .then(() => {
            this.$http({
              url: this.$http.adornUrl('/sys/user/delete'),
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
          })
          .catch(() => {
          })
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
