<template>
  <el-dialog
    title="选择测评"
    :close-on-click-modal="false"
    custom-class="evaluation-list-dialog"
    width="1000px"
    :visible.sync="visible">
    <div class="mod-evaluation">
      <el-form :inline="true" :model="searchForm" @keyup.enter.native="getDataList()">
        <el-form-item class="search-form">
          <el-input v-model="searchForm.search" placeholder="搜索" clearable></el-input>
        </el-form-item>
        <!-- <el-form-item>
          <el-date-picker v-model="searchForm.startTime" type="date" value-format="yyyy-MM-dd" placeholder="开始时间"></el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-date-picker v-model="searchForm.endTime" type="date" value-format="yyyy-MM-dd"  placeholder="结束时间"></el-date-picker>
        </el-form-item> -->
        <el-form-item>
          <el-button @click="getDataList()">查询</el-button>
        </el-form-item>
        <el-form-item>
          <el-button @click="clearAll()">清除全部</el-button>
        </el-form-item>
      </el-form>
      <el-table
        ref="tableRef"
        :data="dataList"
        border
        stripe
        v-loading="dataListLoading"
        @selection-change="selectionChangeHandle"
        @select="selectHandle"
        @select-all="selectAllHandle"
        max-height="500"
        style="width: 100%;">
        <el-table-column
          fixed="left"
          type="selection"
          header-align="center"
          align="center"
          width="50">
        </el-table-column>
        <el-table-column
          fixed="left"
          prop="id"
          header-align="center"
          align="center"
          width="50"
          label="ID">
        </el-table-column>
        <el-table-column
          prop="title"
          header-align="center"
          show-overflow-tooltip
          width="400"
          align="center"
          label="标题">
        </el-table-column>
        <el-table-column
          prop="author"
          header-align="center"
          align="center"
          label="作者">
        </el-table-column>
        <el-table-column
          prop="likeCount"
          header-align="center"
          align="center"
          label="点赞数">
        </el-table-column>
        <el-table-column
          prop="forwardCount"
          header-align="center"
          align="center"
          label="转发数">
        </el-table-column>
        <el-table-column
          prop="commentCount"
          header-align="center"
          align="center"
          label="评论数">
        </el-table-column>
        <el-table-column
          prop="collectCount"
          header-align="center"
          align="center"
          label="收藏数">
        </el-table-column>
        <el-table-column
          prop="label"
          header-align="center"
          show-overflow-tooltip
          width="300"
          align="center"
          label="标签">
          <template slot-scope="scope">
            <el-tag class="evaluation-tag" v-for="(tag, index) in scope.row.label" :key="index" size="small">{{tag}}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="createTime"
          header-align="center"
          show-overflow-tooltip
          width="150"
          align="center"
          label="创建时间">
        </el-table-column>
        <el-table-column
          fixed="right"
          prop="status"
          header-align="center"
          align="center"
          label="状态">
          <template slot-scope="scope">
            <el-tag class="active-tag" v-if="!scope.row.status" size="small" type="danger"
                    @click="updateStatus(scope.row)">隐藏
            </el-tag>
            <el-tag class="active-tag" v-else size="small" @click="updateStatus(scope.row)">显示</el-tag>
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
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
  export default {
    data () {
      return {
        searchForm: {
          search: '',
          startTime: '',
          endTime: '',
          desc: 'create_time',
          asc: '',
          region: '',
          price: '',
          area: '',
          label: []
        },
        dataForm: null,
        dataList: [],
        pageIndex: 1,
        pageSize: 10,
        totalPage: 0,
        dataListLoading: false,
        dataListSelections: [],
        currentIds: [],
        selectedIds: [],
        visible: false
      }
    },
    components: {},
    activated () {
    },
    methods: {
      init (form) {
        const dataForm = JSON.parse(JSON.stringify(form))
        this.visible = true
        this.dataForm = dataForm
        this.selectedIds = [...dataForm.evaluationIds]
        this.pageIndex = 1
        this.getDataList()
      },
      checkDefaultItem () {
        this.toggleSelection(this.dataList)
      },
      toggleSelection (rows) {
        if (rows) {
          rows.forEach(row => {
            if (this.isDefaultChecked(row)) {
              this.$refs.tableRef.toggleRowSelection(row)
            }
          })
        } else {
          this.$refs.tableRef.clearSelection()
        }
      },
      isDefaultChecked (row) {
        return this.selectedIds.includes(row.id)
      },
      // 获取数据列表
      getDataList () {
        this.dataListLoading = true
        this.$http({
          url: this.$http.adornUrl('/sys/api/getEvaluationList'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.pageIndex,
            'size': this.pageSize,
            'type': 1,
            ...this.searchForm
          })
        }).then(({data}) => {
          if (data && data.code === 200) {
            this.dataList = data.data.list
            this.totalPage = data.data.total
          } else {
            this.dataList = []
            this.totalPage = 0
          }
          this.$nextTick(() => {
            this.checkDefaultItem()
          })
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
      // 单选
      selectHandle (selection, row) {
        // console.log(selection, row)
        // this.currentIds = selection.map(item => item.id)
        // console.log(selection, row, this.currentIds)
        if (this.selectedIds.includes(row.id)) {
          // 删除
          this.selectedIds = this.selectedIds.filter(id => row.id !== id)
        } else {
          // 添加
          this.selectedIds.push(row.id)
        }
      },
      selectAllHandle (selection) {
        if (selection.length > 0) {
          this.selectedIds = this.selectedIds.concat(selection.map(item => item.id))
        } else {
          const ids = this.dataList.map(item => item.id)
          this.selectedIds = this.selectedIds.filter(id => !ids.includes(id))
        }
      },
      // 多选
      selectionChangeHandle (val) {
        this.dataListSelections = val
      },
      clearAll () {
        this.selectedIds = []
        this.$refs.tableRef.clearSelection()
      },
      updateStatus (row) {
        this.$http({
          url: this.$http.adornUrl('/sys/api/updateEvaluationStatus'),
          method: 'get',
          params: {
            id: row.id
          }
        }).then(({data}) => {
          if (data && data.code === 200) {
            this.$message({
              message: '操作成功',
              type: 'success',
              duration: 1500,
              onClose: () => {
                // this.getDataList()
              }
            })
            row.status = !row.status
          } else {
            this.$message.error(data.msg)
          }
        })
      },
      toAudit (id) {
        this.$router.push({name: 'evaluation-audit', query: {id: id}})
      },
      // 表单提交
      dataFormSubmit () {
        var ids = this.dataListSelections.map(item => {
          return item.id
        })
        this.dataForm.evaluationIds = this.selectedIds
        this.$http({
          url: this.$http.adornUrl(`/sys/config/saveKingKongConfig`),
          method: 'post',
          data: this.$http.adornData(this.dataForm)
        }).then(({data}) => {
          if (data && data.code === 200) {
            this.$message({
              message: '操作成功',
              type: 'success',
              duration: 1500,
              onClose: () => {
                this.visible = false
                this.$emit('refreshDataList')
              }
            })
          } else {
            this.$message.error(data.msg)
          }
        })
      }
    }
  }
</script>
<style lang="scss">
  .evaluation-list-dialog {
    .el-dialog__body {
      padding: 10px 20px;
    }
  }

  .evaluation-img {
    max-width: 320px;
    max-height: 240px;
  }

  .evaluation-tag {
    margin: 1px;
  }

  .search-form {
    display: flex;
    flex-direction: row;
  }

  .active-tag {
    cursor: pointer;
  }
</style>
