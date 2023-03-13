<template>
  <div class="mod-user">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
             label-width="200px">
      <el-form-item label="小程序appId" prop="appId">
        <el-input v-model="dataForm.appId" placeholder="小程序appId"></el-input>
      </el-form-item>
      <el-form-item label="小程序appSecret" prop="appSecret">
        <el-input v-model="dataForm.appSecret" placeholder="小程序appSecret" type="password"></el-input>
      </el-form-item>
    </el-form>
    <div style="text-align: center;">
      <el-button type="primary" @click="dataFormSubmit()">保存</el-button>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'wechat',
    data () {
      return {
        dataForm: {
          appId: '',
          appSecret: ''
        },
        dataRule: {
          appId: [
            {required: true, message: '小程序appId不能为空', trigger: 'blur'}
          ],
          appSecret: [
            {required: true, message: '小程序appSecret不能为空', trigger: 'blur'}
          ]
        }
      }
    },
    activated () {
      this.getWxConfig()
    },
    methods: {
      // 获取数据
      getWxConfig () {
        this.$http({
          url: this.$http.adornUrl('/sys/config/getWxConfig'),
          method: 'get'
        }).then(({data}) => {
          this.dataForm = data.data
        })
      },
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.$http({
              url: this.$http.adornUrl('/sys/config/saveWxConfig'),
              method: 'post',
              data: this.$http.adornData({
                'appId': this.dataForm.appId,
                'appSecret': this.dataForm.appSecret
              })
            }).then(({data}) => {
              if (data && data.code === 200) {
                this.$message({
                  type: 'success',
                  message: '操作成功',
                  duration: 1500
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
