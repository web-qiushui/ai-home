<template>
  <div class="mod-user">
    <el-form
      :inline="true"
      :model="dataForm"
      @keyup.enter.native="getDataList()"
    >
      <el-form-item>
        <el-input v-model="dataForm.content" placeholder="聊天内容" clearable></el-input>
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
        prop="sendId"
        header-align="center"
        align="center"
        label="发送人"
      >
      </el-table-column>
      <el-table-column
        prop="receiveId"
        header-align="center"
        align="center"
        label="接收人"
      >
      </el-table-column>
      <el-table-column
        prop="createTime"
        header-align="center"
        align="center"
        label="发送时间"
      >
      </el-table-column>
      <el-table-column
        prop="content"
        width="450"
        header-align="center"
        align="center"
        label="消息"
      >
      </el-table-column>
      <el-table-column
        prop="status"
        header-align="center"
        align="center"
        label="状态"
      >
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === 0" size="small" type="info">?</el-tag>
          <el-tag v-if="scope.row.status === 1" size="small" type="info">未读</el-tag>
          <el-tag v-if="scope.row.status === 2" size="small" type="success">已读</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="type"
        header-align="center"
        align="center"
        label="类型"
      >
        <template slot-scope="scope">
          <el-tag v-if="scope.row.type === 1" size="small" type="info">文字</el-tag>
          <el-tag v-if="scope.row.type === 2" size="small" type="success">测评</el-tag>
          <el-tag v-if="scope.row.type === 3" size="small" type="info">购房报告</el-tag>
          <el-tag v-if="scope.row.type === 4" size="small" type="success">评价</el-tag>
          <el-tag v-if="scope.row.type === 5" size="small" type="info">名片</el-tag>
          <el-tag v-if="scope.row.type === 6" size="small" type="success">照片</el-tag>
          <el-tag v-if="scope.row.type === 7" size="small" type="info">快速回复</el-tag>
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
  </div>
</template>

<script>
export default {
  data () {
    return {
      dataForm: {
        asc: [],
        desc: [],
        endTime: '',
        id: 0,
        consultantUserId: 0,
        remark: '',
        startTime: '',
        content: ''
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
  components: {},
  created () {
  },
  activated () {
    this.dataForm.id = parseInt(this.$route.query.id)
    this.dataForm.consultantUserId = parseInt(this.$route.query.consultantUserId)
    this.getDataList()
  },
  methods: {
    // 获取数据列表
    getDataList () {
      this.dataListLoading = true
      this.$http({
        url: this.$http.adornUrl('/sys/api/getHistoricalNews'),
        method: 'get',
        params: this.$http.adornParams({
          'page': this.pageIndex,
          'size': this.pageSize,
          ...this.dataForm
        })
      }).then(({ data }) => {
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
    addOrUpdateHandle (form) {
      this.addOrUpdateVisible = true
      this.$nextTick(() => {
        this.$refs.addOrUpdate.init(form)
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
          }).then(({ data }) => {
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
        .catch(() => {})
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
