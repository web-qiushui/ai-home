<template>
  <el-dialog
    :title="auditType===1? '管理员审核' : '企业审核'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
             label-width="80px">
      <el-form-item label="企业" prop="firmId" v-if="auditType===1">
        <el-select v-model="dataForm.firmId" placeholder="请选择企业" style="width: 100%" value-key="id">
          <el-option
            v-for="item in firmList"
            :key="item.id"
            :label="item.name"
            :value="item.id">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="顾问Id" prop="id">
        <el-input v-model="dataForm.id" placeholder="顾问Id" disabled></el-input>
      </el-form-item>
      <el-form-item label="键值" size="mini" prop="status">
        <el-radio-group v-model="dataForm.status">
          <el-radio :label="true">同意</el-radio>
          <el-radio :label="false">拒绝</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="备注" prop="remark" v-if="!dataForm.status">
        <el-input v-model="dataForm.remark" placeholder="拒绝理由"></el-input>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
  export default {
    name: 'audit',
    data () {
      return {
        visible: false,
        dataForm: {
          firmId: '',
          id: '',
          status: null,
          remark: ''
        },
        dataRule: {
          firmId: [{required: true, message: '请选择企业', trigger: 'blur'}],
          remark: [{required: true, message: '请输入拒绝理由', trigger: 'blur'}]
        },
        firmList: [],
        auditType: null
      }
    },
    methods: {
      init (data) {
        this.auditType = data.auditType
        if (this.auditType === 1) {
          this.getFirmList()
        }
        this.visible = true
        this.dataForm.id = data.id || null
        this.dataForm.status = true
      },
      getFirmList () {
        this.$http({
          url: this.$http.adornUrl('/sys/api/getFirmList'),
          method: 'get',
          params: this.$http.adornParams({})
        }).then(({data}) => {
          if (data && data.code === 200) {
            this.firmList = data.data
          }
        })
      },
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.dataForm.status = this.dataForm.status ? 2 : 3
            this.$http({
              url: this.$http.adornUrl('/sys/api/auditConsultant'),
              method: 'post',
              data: this.$http.adornData({
                ...this.dataForm
              })
            }).then(({data}) => {
              if (data && data.code === 200) {
                this.$message({
                  message: '操作成功',
                  type: 'success',
                  duration: 1500,
                  onClose: () => {
                    this.visible = false
                    this.$emit('refreshData')
                  }
                })
              } else {
                this.$message.error(data.msg)
              }
            })
          }
        })
      }
    }
  }
</script>

<style scoped>

</style>
