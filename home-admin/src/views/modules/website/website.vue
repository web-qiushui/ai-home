<template>
  <div class="mod-user">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
             label-width="200px">
      <el-form-item label="地址" prop="address">
        <el-input v-model="dataForm.address" placeholder="地址"></el-input>
      </el-form-item>
      <el-form-item label="网站Logo">
        <el-upload
          class="avatar-uploader"
          action="https://www.029aijia.cn/home-service/sys/oss/upload/images"
          :show-file-list="false"
          :headers="headers"
          :on-success="(res,file)=>{return handleAvatarSuccess(res,file,'appLogo')}">
          <img v-if="dataForm.appLogo" :src="imgUrl+dataForm.appLogo" class="avatar">
          <i v-else class="el-icon-plus avatar-uploader-icon"></i>
        </el-upload>
      </el-form-item>
      <el-form-item label="网站主Logo">
        <el-upload
          class="avatar-uploader"
          action="https://www.029aijia.cn/home-service/sys/oss/upload/images"
          :show-file-list="false"
          :headers="headers"
          :on-success="(res,file)=>{return handleAvatarSuccess(res,file,'appMainLogo')}">
          <img v-if="dataForm.appMainLogo" :src="imgUrl+dataForm.appMainLogo" class="avatar">
          <i v-else class="el-icon-plus avatar-uploader-icon"></i>
        </el-upload>
      </el-form-item>
      <el-form-item label="网站名称" prop="appName">
        <el-input v-model="dataForm.appName" placeholder="网站名称"></el-input>
      </el-form-item>
      <el-form-item label="客服链接" prop="consumerLink">
        <el-input v-model="dataForm.consumerLink" placeholder="客服链接"></el-input>
      </el-form-item>
      <el-form-item label="客服电话" prop="consumerPhone">
        <el-input v-model="dataForm.consumerPhone" placeholder="客服电话"></el-input>
      </el-form-item>
      <el-form-item label="版权信息" prop="copyright">
        <el-input v-model="dataForm.copyright" placeholder="版权信息"></el-input>
      </el-form-item>
      <el-form-item label="办公时间">
        <el-time-picker
          is-range
          v-model="value1"
          range-separator="至"
          start-placeholder="开始时间"
          end-placeholder="结束时间"
          placeholder="选择时间范围"
          value-format="HH:mm"
          format='HH:mm'
          @change="getofficeHours"
        >
        </el-time-picker>
      </el-form-item>
      <el-form-item label="备案号" prop="record">
        <el-input v-model="dataForm.record" placeholder="备案号"></el-input>
      </el-form-item>
      <el-form-item label="启动页图片">
        <el-upload
          class="avatar-uploader"
          action="https://www.029aijia.cn/home-service/sys/oss/upload/images"
          :show-file-list="false"
          :headers="headers"
          :on-success="(res,file)=>{return handleAvatarSuccess(res,file,'startPage')}">
          <img v-if="dataForm.startPage" :src="imgUrl+dataForm.startPage" class="avatar">
          <i v-else class="el-icon-plus avatar-uploader-icon"></i>
        </el-upload>
      </el-form-item>
      <el-form-item label="公众号二维码">
        <el-upload
          class="avatar-uploader"
          action="https://www.029aijia.cn/home-service/sys/oss/upload/images"
          :show-file-list="false"
          :headers="headers"
          :on-success="(res,file)=>{return handleAvatarSuccess(res,file,'thePublicQrCode')}">
          <img v-if="dataForm.thePublicQrCode" :src="imgUrl+dataForm.thePublicQrCode" class="avatar">
          <i v-else class="el-icon-plus avatar-uploader-icon"></i>
        </el-upload>
      </el-form-item>
    </el-form>
    <div style="text-align: center;">
      <el-button type="primary" @click="dataFormSubmit()">保存</el-button>
    </div>
  </div>
</template>

<script>
  import {phoneNum, isURL} from '../../../utils/validate'

  export default {
    name: 'website',
    data () {
      var validateMobile = (rule, value, callback) => {
        if (!phoneNum(value)) {
          callback(new Error('手机号格式错误'))
        } else {
          callback()
        }
      }
      var validateIsUrl = (rule, value, callback) => {
        if (!isURL(value)) {
          callback(new Error('手机号格式错误'))
        } else {
          callback()
        }
      }
      let startTime = new Date()
      let endTime = new Date()
      startTime.setHours(7)
      startTime.setMinutes(0)
      startTime.setSeconds(0)
      endTime.setHours(21)
      endTime.setMinutes(0)
      endTime.setSeconds(0)
      return {
        dataForm: {
          address: '',
          appLogo: '',
          appMainLogo: '',
          appName: '',
          consumerLink: '',
          consumerPhone: '',
          copyright: '',
          officeHours: '',
          record: '',
          startPage: '',
          thePublicQrCode: ''
        },
        dataRule: {
          address: [
            {required: true, message: '地址不能为空', trigger: 'blur'}
          ],
          appName: [
            {required: true, message: '网站名称不能为空', trigger: 'blur'}
          ],
          consumerLink: [
            {required: true, message: '客服链接不能为空', trigger: 'blur'},
            {validator: validateIsUrl, trigger: 'blur'}
          ],
          consumerPhone: [
            {required: true, message: '客服电话不能为空', trigger: 'blur'},
            {validator: validateMobile, trigger: 'blur'}
          ],
          copyright: [
            {required: true, message: '版权信息不能为空', trigger: 'blur'}
          ],
          record: [
            {required: true, message: '备案号不能为空', trigger: 'blur'}
          ]
        },
        startTime: '',
        endTime: '',
        imgUrl: null,
        value1: [startTime, endTime]
      }
    },
    activated () {
      this.getWebsiteConfig()
      this.imgUrl = window.SITE_CONFIG.imageUrl
    },
    computed: {
      headers () {
        return {
          'token': this.$cookie.get('token')
        }
      }
    },
    methods: {
      getofficeHours (e) {
        this.dataForm.officeHours = e[0] + '-' + e[1]
      },
      handleAvatarSuccess (res, file, name) {
        if (res && res.code === 200) {
          this.dataForm[name] = res.data
        }
      },
      getWebsiteConfig () {
        this.$http({
          url: this.$http.adornUrl('/sys/config/anon/getWebsiteConfig'),
          method: 'get'
        }).then(({data}) => {
          if (data && data.code === 200) {
            this.value1 = data.data.officeHours.split('-')
            this.dataForm = data.data
          }
        })
      },
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.$http({
              url: this.$http.adornUrl('/sys/config/saveWebsiteConfig'),
              method: 'post',
              data: this.$http.adornData({
                ...this.dataForm
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
  .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }

  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }

  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
  }

  .avatar {
    width: 178px;
    height: 178px;
    display: block;
  }
</style>
