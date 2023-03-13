<template>
  <el-dialog
    :title="!dataForm.key ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm"
             label-width="80px" @keyup.enter.native="dataFormSubmit()">
      <el-form-item label="键名" prop="key">
        <el-input v-model="dataForm.key" placeholder="输入键名"></el-input>
      </el-form-item>
      <el-form-item label="名称" prop="name">
        <el-input v-model="dataForm.name" placeholder="输入名称"></el-input>
      </el-form-item>
      <el-form-item label="键值" size="mini" prop="value">
        <el-radio-group v-model="dataForm.value">
          <el-radio :label="true">开</el-radio>
          <el-radio :label="false">关</el-radio>
        </el-radio-group>
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
    name: 'switch-add-or-update',
    data () {
      return {
        visible: false,
        dataForm: {
          key: null,
          name: null,
          value: null
        },
        dataRule: {
          key: [
            {required: true, message: '键名不能为空', trigger: 'blur'}
          ],
          name: [{
            required: true, message: '名称不能为空', trigger: 'blur'
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
            key: null,
            name: null,
            value: null
          }
        }
      },
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.$http({
              url: this.$http.adornUrl('/sys/config/saveSwitchConfig'),
              method: 'post',
              data: this.$http.adornData({
                'key': this.dataForm.key,
                'name': this.dataForm.name,
                'value': this.dataForm.value
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
