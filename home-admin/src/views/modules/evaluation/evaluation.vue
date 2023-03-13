<template>
  <div class="mod-evaluation">
    <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
      <el-form-item class="data-form" label="搜索">
        <el-input v-model="dataForm.search" placeholder="搜索" clearable></el-input>
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
      <el-form-item label="状态" prop="status" >
        <el-select v-model="dataForm.status" clearable placeholder="请选择">
          <el-option
            v-for="item in statusOptions"
            :key="item.key"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="开始时间">
        <el-date-picker v-model="dataForm.startTime" type="date" value-format="yyyy-MM-dd" placeholder="开始时间"></el-date-picker>
      </el-form-item>
      <el-form-item label="结束时间">
        <el-date-picker v-model="dataForm.endTime" type="date" value-format="yyyy-MM-dd"  placeholder="结束时间"></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()">查询</el-button>
        <el-button v-if="isAuth('sys:user:save')" type="primary" @click="addOrUpdateHandle()">新增</el-button>
        <!-- <el-button v-if="isAuth('sys:user:delete')" type="danger" @click="deleteHandle()" :disabled="dataListSelections.length <= 0">批量删除</el-button> -->
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
        label="创建时间">
      </el-table-column>
      <el-table-column
        prop="title"
        header-align="center"
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
        sortable="custom"
        label="点赞数">
      </el-table-column>
      <el-table-column
        prop="forwardCount"
        header-align="center"
        align="center"
        sortable="custom"
        label="转发数">
      </el-table-column>
      <el-table-column
        prop="commentCount"
        header-align="center"
        align="center"
        sortable="custom"
        label="评论数">
      </el-table-column>
      <el-table-column
        prop="collectCount"
        header-align="center"
        align="center"
        sortable="custom"
        label="收藏数">
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
        prop="status"
        header-align="center"
        align="center"
        label="状态">
        <template slot-scope="scope">
          <el-tag class="active-tag" v-if="!scope.row.status" size="small" type="danger" @click="updateStatus(scope.row)">隐藏</el-tag>
          <el-tag class="active-tag" v-else size="small" @click="updateStatus(scope.row)">显示</el-tag>
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
          <el-button type="text" size="small" @click="toAudit(scope.row.id)">查看评论</el-button>
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
</template>

<script>
  export default {
    data () {
      return {
        dataForm: {
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
        regionOptions: [],
        priceOptions: [],
        areaOptions: [],
        tagOptions: [],
        statusOptions: [],
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
    },
    activated () {
      this.initOptions()
      this.getDataList()
    },
    methods: {
      sortChange (column) {
        if (column.order === 'ascending') {
          if (column.prop === 'likeCount') {
            this.dataForm.asc = 'like_count'
          }
          if (column.prop === 'commentCount') {
            this.dataForm.asc = 'comment_count'
          }
          if (column.prop === 'collectCount') {
            this.dataForm.asc = 'collect_count'
          }
          if (column.prop === 'forwardCount') {
            this.dataForm.asc = 'forward_count'
          }
          if (column.prop === 'createTime') {
            this.dataForm.asc = 'create_time'
          }
          this.dataForm.desc = ''
        } else if (column.order === 'descending') {
          if (column.prop === 'likeCount') {
            this.dataForm.desc = 'like_count'
          }
          if (column.prop === 'commentCount') {
            this.dataForm.desc = 'comment_count'
          }
          if (column.prop === 'collectCount') {
            this.dataForm.desc = 'collect_count'
          }
          if (column.prop === 'forwardCount') {
            this.dataForm.desc = 'forward_count'
          }
          if (column.prop === 'createTime') {
            this.dataForm.desc = 'create_time'
          }
          this.dataForm.asc = ''
        } else {
          this.dataForm.desc = 'create_time'
          this.dataForm.asc = ''
        }
        this.getDataList()
      },
      // 数据字典id替换名称
      datadictName (row, column, cellValue, index) {
        if (cellValue != null) {
          if (this.$store.state.common.datadict[cellValue] != null) {
            return this.$store.state.common.datadict[cellValue].name
          }
          return ''
        } else {
          return ''
        }
      },
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

        this.statusOptions = [{
          key: 0,
          label: '隐藏',
          value: 0
        }, {
          key: 1,
          label: '显示',
          value: 1
        }]
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
      // 新增 / 修改
      addOrUpdateHandle (id) {
        this.addOrUpdateVisible = true
        this.$nextTick(() => {
        //   this.$refs.addOrUpdate.init(id)
          this.$router.push({name: 'evaluation-editor', query: {id: id, type: 1}})
        })
      },
      toAudit (id) {
        this.$router.push({name: 'evaluation-audit', query: {id: id}})
      },
      // 删除
      deleteHandle (id) {
        var ids = id ? [id] : this.dataListSelections.map(item => {
          return item.id
        })
        this.$confirm(`确定对[id=${ids.join(',')}]进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('/sys/evaluation/delete'),
            method: 'post',
            data: this.$http.adornData(ids, false)
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
        }).catch(() => {})
      }
    }
  }
</script>
<style>
  .evaluation-img {
    max-width: 320px;
    max-height: 240px;
  }

  .evaluation-tag {
      margin: 1px;
  }

  .data-form {
    display: flex;
    flex-direction: row;
  }

  .active-tag {
    cursor: pointer;
  }
</style>
