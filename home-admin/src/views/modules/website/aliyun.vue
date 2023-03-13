<template>
  <div class="mod-user">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
             label-width="200px">
          <el-form-item label="阿里云accessKey" prop="accessKey">
            <el-input v-model="dataForm.accessKey" placeholder="阿里云accessKey"></el-input>
          </el-form-item>
          <el-form-item label="	阿里云accessSecret" prop="accessSecret">
            <el-input v-model="dataForm.accessSecret" placeholder="阿里云accessSecret" type="password"></el-input>
          </el-form-item>
          <el-form-item label="短信签名" prop="sign">
            <el-input v-model="dataForm.sign" placeholder="短信签名"></el-input>
          </el-form-item>
          <el-form-item label="状态" size="mini" prop="status">
            <el-radio-group v-model="dataForm.status">
              <el-radio :label="0">禁用</el-radio>
              <el-radio :label="1">正常</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="模版Id" prop="templateCode">
            <el-input v-model="dataForm.templateCode" placeholder="模版Id"></el-input>
          </el-form-item>
          <el-form-item label="阿里云API地址" prop="url">
            <el-input v-model="dataForm.url" placeholder="阿里云API地址"></el-input>
          </el-form-item>
    </el-form>
    <div style="text-align: center;">
      <el-button type="primary" @click="dataFormSubmit()">保存</el-button>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'aliyun',
    data () {
      return {
        dataForm: {
          accessKey: '',
          accessSecret: '',
          sign: '',
          status: 1,
          templateCode: '',
          url: ''
        },
        dataRule: {
          accessKey: [
            {required: true, message: '阿里云accessKey不能为空', trigger: 'blur'}
          ],
          accessSecret: [
            {required: true, message: '阿里云accessSecret不能为空', trigger: 'blur'}
          ],
          sign: [
            {required: true, message: '短信签名不能为空', trigger: 'blur'}
          ],
          templateCode: [
            {required: true, message: '模版Id不能为空', trigger: 'blur'}
          ],
          url: [
            {required: true, message: '阿里云API地址不能为空', trigger: 'blur'}
          ]
        }
      }
    },
    activated () {
      this.getAliyunSmsConfig()
    },
    methods: {
      // 获取数据
      getAliyunSmsConfig () {
        this.$http({
          url: this.$http.adornUrl('/sys/config/getAliyunSmsConfig'),
          method: 'get'
        }).then(({data}) => {
          this.dataForm = data.data
        })
      },
      // 新增 / 修改
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.$http({
              url: this.$http.adornUrl('/sys/config/saveAliyunSmsConfig'),
              method: 'post',
              data: this.$http.adornData({
                'accessKey': this.dataForm.accessKey,
                'accessSecret': this.dataForm.accessSecret,
                'sign': this.dataForm.sign,
                'templateCode': this.dataForm.templateCode,
                'status': this.dataForm.status,
                'url': this.dataForm.url
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

<style>
</style>
