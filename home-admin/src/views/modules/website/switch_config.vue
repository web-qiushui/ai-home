<template>
  <div class="mod-user">
    <el-form :inline="true" :model="dataForm" @keyup.enter.native="getSwitchConfig()">
      <el-form-item>
        <el-input v-model="dataForm.userName" placeholder="键名" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="getSwitchConfig()">查询</el-button>
        <el-button type="primary" @click="addOrUpdateHandle()">新增</el-button>
      </el-form-item>
    </el-form>
    <el-table
      :data="dataList"
      border
      v-loading="dataListLoading"
      @selection-change="selectionChangeHandle"
      style="width: 100%;">
      <el-table-column
        type="selection"
        header-align="center"
        align="center"
        width="50">
      </el-table-column>
      <el-table-column
        prop="key"
        header-align="center"
        align="center"
        label="键名">
      </el-table-column>
      <el-table-column
        prop="name"
        header-align="center"
        align="center"
        label="名称">
      </el-table-column>
      <el-table-column
        prop="value"
        header-align="center"
        align="center"
        label="键值">
        <template slot-scope="scope">
          <el-tag v-if="!scope.row.value" size="small" type="danger">关</el-tag>
          <el-tag v-else size="small">开</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="150"
        label="操作">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="addOrUpdateHandle(scope.row)">修改</el-button>
          <el-button type="text" size="small" @click="deleteHandle(scope.row.key)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- <el-pagination
      @size-change="sizeChangeHandle"
      @current-change="currentChangeHandle"
      :current-page="pageIndex"
      :page-sizes="[10, 20, 50, 100]"
      :page-size="pageSize"
      :total="totalPage"
      layout="total, sizes, prev, pager, next, jumper">
    </el-pagination> -->
    <!-- 弹窗, 新增 / 修改 -->
    <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getSwitchConfig"></add-or-update>
  </div>
</template>

<script>
  import AddOrUpdate from './switch-add-or-update'

  export default {
    name: 'switch_config',
    data () {
      return {
        dataForm: {
          userName: ''
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
    components: {AddOrUpdate},
    activated () {
      this.getSwitchConfig()
    },
    methods: {
      getSwitchConfig () {
        this.$http({
          url: this.$http.adornUrl('/sys/config/getSwitchConfig'),
          method: 'get'
        }).then(({data}) => {
          if (data && data.code === 200) {
            this.dataList = data.data
          }
        })
      },
      // 每页数
      sizeChangeHandle (val) {
        this.pageSize = val
        this.pageIndex = 1
        this.getSwitchConfig()
      },
      // 当前页
      currentChangeHandle (val) {
        this.pageIndex = val
        this.getSwitchConfig()
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
      deleteHandle (key) {
        this.$confirm(`确定对[key=${key}]进行[${key ? '删除' : '批量删除'}]操作?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('/sys/config/removeSwitchConfig'),
            method: 'get',
            params: this.$http.adornParams({
              key: key
            })
          }).then(({data}) => {
            if (data && data.code === 200) {
              this.$message({
                type: 'success',
                message: '操作成功',
                duration: 1500,
                onClose: () => {
                  this.visible = false
                  this.$emit('refreshDataList')
                }
              })
            } else {
              this.$message({
                type: 'error',
                message: '操作失败',
                duration: 1500
              })
            }
          })
        })
      }
    }
  }
</script>

<style scoped>

</style>
