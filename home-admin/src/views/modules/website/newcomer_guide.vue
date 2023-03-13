<template>
  <div>
    <el-form :inline="true">
      <el-form-item>
        <el-button type="primary" @click="addOrUpdateHandle()">新增</el-button>
      </el-form-item>
    </el-form>
    <el-table
      :data="dataGuideConfig"
      border
      v-loading="dataListLoading"
      style="width: 100%;">
      <el-table-column
        prop="explanation"
        header-align="center"
        align="center"
        label="说明">
      </el-table-column>
      <el-table-column
        prop="status"
        header-align="center"
        align="center"
        label="状态">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === 0" size="small" type="danger">禁用</el-tag>
          <el-tag v-else size="small">正常</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="type"
        header-align="center"
        align="center"
        label="类型">
      </el-table-column>
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="150"
        label="操作">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="addOrUpdateHandle(scope.row)">修改
          </el-button>
          <el-button type="text" size="small" @click="deleteHandle(scope.row.type)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate"
                   @refreshDataList="getNewcomerGuideConfig"></add-or-update>
  </div>
</template>

<script>
  import AddOrUpdate from './newcomer-add-or-update'

  export default {
    name: 'newcomer_guide',
    data () {
      return {
        dataGuideConfig: [],
        dataListLoading: false,
        addOrUpdateVisible: false
      }
    },
    components: {
      AddOrUpdate
    },
    activated () {
      this.getNewcomerGuideConfig()
    },
    methods: {
      getNewcomerGuideConfig () {
        this.dataListLoading = true
        this.$http({
          url: this.$http.adornUrl('/sys/config/anon/getNewcomerGuideConfig'),
          method: 'get'
          // params: this.$http.adornParams({'status': '0'})
        }).then(({data}) => {
          if (data && data.data) {
            this.dataGuideConfig = data.data
          }
          this.dataListLoading = false
        })
      },
      addOrUpdateHandle (item) {
        this.addOrUpdateVisible = true
        this.$nextTick(() => {
          this.$refs.addOrUpdate.init(item)
        })
      },
      deleteHandle (type) {
        this.$http({
          url: this.$http.adornUrl('/sys/config/removeNewcomerGuideConfig'),
          method: 'get',
          params: this.$http.adornParams({
            'type': type
          })
        }).then(({data}) => {
          if (data && data.code === 200) {
            this.$message({
              type: 'success',
              message: '操作成功',
              duration: 1500,
              onClose: () => {
                this.getNewcomerGuideConfig()
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
      }
    }
  }
</script>

<style scoped>

</style>
