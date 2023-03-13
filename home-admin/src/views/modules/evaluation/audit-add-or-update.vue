<template>
  <el-dialog
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
             label-width="80px">
      <el-form-item label="备注" prop="remark">
        <el-input v-model="dataForm.remark" placeholder="请输入拒绝理由"></el-input>
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
    data () {
      return {
        visible: false,
        dataForm: {
          evaluationId: '',
          remark: '',
          status: 3
        },
        dataRule: {}
      }
    },
    methods: {
      init (id) {
        this.visible = true
        this.dataForm.evaluationId = id
      },
      dataFormSubmit () {
        this.$http({
          url: this.$http.adornUrl('/sys/api/auditEvaluationComment'),
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

<style scoped>

</style>
