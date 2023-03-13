<template>
  <el-dialog
    :title="!dataForm.status ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm"
             label-width="80px" @keyup.enter.native="dataFormSubmit()">
      <el-form-item label="说明" prop="explanation">
        <el-input v-model="dataForm.explanation" placeholder="输入说明"></el-input>
      </el-form-item>
      <el-form-item label="状态" size="mini" prop="status">
        <el-radio-group v-model="dataForm.status">
          <el-radio :label="0">禁用</el-radio>
          <el-radio :label="1">正常</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="类型" prop="type">
        <el-input v-model="dataForm.type" placeholder="输入类型"></el-input>
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
    name: 'newcomer-add-or-update',
    data () {
      return {
        visible: false,
        dataForm: {
          explanation: null,
          status: null,
          type: null
        },
        dataRule: {
          explanation: [
            {required: true, message: '说明不能为空', trigger: 'blur'}
          ],
          type: [{
            required: true, message: '类型不能为空', trigger: 'blur'
          }]
        }
      }
    },
    methods: {
      init (item) {
        this.visible = true
        if (item) {
          this.dataForm = item
        } else {
          this.dataForm = {
            explanation: null,
            status: null,
            type: null
          }
        }
      },
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.$http({
              url: this.$http.adornUrl('/sys/config/saveNewcomerGuideConfig'),
              method: 'post',
              data: this.$http.adornData({
                'explanation': this.dataForm.explanation,
                'status': this.dataForm.status,
                'type': this.dataForm.type
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
          }
        })
      }
    }
  }
</script>

<style scoped>

</style>
