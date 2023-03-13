<template>
  <div class="mod-user">
    <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
      <el-form-item label="姓名">
        <el-input v-model="dataForm.name" placeholder="姓名" clearable></el-input>
      </el-form-item>
      <el-form-item label="昵称">
        <el-input v-model="dataForm.nickName" placeholder="昵称" clearable></el-input>
      </el-form-item>
      <el-form-item label="手机号">
        <el-input v-model="dataForm.phone" placeholder="手机号" clearable></el-input>
      </el-form-item>
      <el-form-item label="身份证">
        <el-input v-model="dataForm.idCard" placeholder="身份证" clearable></el-input>
      </el-form-item>
      <el-form-item label="性别">
        <el-select v-model="dataForm.gender" clearable placeholder="请选择">
          <el-option
            v-for="item in genderOptions"
            :key="item.key"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <!-- <el-form-item label="状态">
        <el-select v-model="dataForm.status" clearable placeholder="请选择">
          <el-option
            v-for="item in statusOptions"
            :key="item.key"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item> -->
      <el-form-item label="开始时间">
        <el-date-picker v-model="dataForm.startTime" type="date" value-format="yyyy-MM-dd" placeholder="开始时间"></el-date-picker>
      </el-form-item>
      <el-form-item label="结束时间">
        <el-date-picker v-model="dataForm.endTime" type="date" value-format="yyyy-MM-dd"  placeholder="结束时间"></el-date-picker>
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
        label="注册时间">
      </el-table-column>
      <el-table-column
        prop="idCard"
        header-align="center"
        align="center"
        label="身份证号">
      </el-table-column>
      <el-table-column
        prop="name"
        header-align="center"
        align="center"
        label="姓名">
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
          <img class="avatar-url-img" :src="scope.row.avatarUrl" />
        </template>
      </el-table-column>
      <el-table-column
        prop="gender"
        header-align="center"
        align="center"
        :formatter="formatterGender"
        label="性别">
      </el-table-column>
      <el-table-column
        prop="role"
        header-align="center"
        align="center"
        :formatter="formatterRole"
        label="角色">
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
<!--    <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update>-->
  </div>
</template>

<script>
  // import AddOrUpdate from './evaluation-add-or-update'
  export default {
    data () {
      return {
        dataForm: {
          name: '',
          nickName: '',
          phone: '',
          idCard: '',
          role: '',
          gender: '',
          startTime: '',
          endTime: '',
          status: ''
        },
        genderOptions: [],
        roleOptions: [],
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
      // AddOrUpdate
    },
    activated () {
      this.initOptions()
      this.getDataList()
    },
    methods: {
      initOptions() {
        this.genderOptions = [{
          key: 2,
          label: '女',
          value: 2
        }, {
          key: 1,
          label: '男',
          value: 1
        }]
        this.statusOptions = [{
          key: 0,
          label: '锁定',
          value: 0
        }, {
          key: 1,
          label: '正常',
          value: 1
        }]
      },
      // 数据字典id替换名称
      formatterGender (row, column, cellValue, index) {
        if (cellValue != null && cellValue !== 0) {
          if (cellValue === 1) {
            return '男'
          } else {
            return '女'
          }
        } else {
          return '未知'
        }
      },
      formatterRole (row, column, cellValue, index) {
        if (cellValue === 0) {
          return '用户'
        }
        if (cellValue === 1) {
          return 'AI顾问'
        }
        if (cellValue === 2) {
          return '置业顾问'
        }
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
      // 获取数据列表
      getDataList () {
        this.dataListLoading = true
        this.$http({
          url: this.$http.adornUrl('/sys/api/getUserList'),
          method: 'get',
          params: this.$http.adornParams({
            ...this.dataForm,
            'page': this.pageIndex,
            'size': this.pageSize,
            'role': 1
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
      }
    }
  }
</script>
<style>
  .avatar-url-img {
    max-width: 50px;
    max-height: 50px;
  }

  .evaluation-tag {
      margin: 1px;
  }
</style>
