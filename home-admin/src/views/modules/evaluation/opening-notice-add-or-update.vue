<template>
  <el-dialog
    title="修改"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
             label-width="80px">
      <el-form-item label="图片" prop="imageUrl">
        <div class="image-container">
          <el-upload class="image-uploader"
                     :action="imageUploadUrl"
                     :headers="uploadHeaders"
                     :show-file-list="false"
                     :on-success="fileUploadSuccess"
                     :before-upload="fileBeforeUpload"
                     :auto-upload="true">
            <img v-if="dataForm.imageUrl" :src="getImageUrl(dataForm.imageUrl)" class="image">
            <i v-else slot="default" class="el-icon-plus image-uploader-icon"></i>
          </el-upload>
        </div>
      </el-form-item>


      <el-form-item label="内容" prop="content">
        <el-input
          type="textarea"
          :autosize="{ minRows: 2, maxRows: 4}"
          placeholder="请输入内容"
          v-model="dataForm.description">
        </el-input>
      </el-form-item>
      <el-form-item label="状态" size="mini" prop="status" :class="{ 'is-required': true }">
        <el-radio-group v-model="dataForm.status">
          <el-radio :label="0">禁用</el-radio>
          <el-radio :label="1">启用</el-radio>
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
  import Vue from 'vue'

  export default {
    data () {
      return {
        visible: false,
        singlePage: {},
        dataForm: {
          id: null,
          description: '',
          imageUrl: '',
          status: 1
        },
        dataRule: {},
        tagInputVisible: false,
        tagInputValue: '',
        imageUploadUrl: this.$http.adornUrl('/sys/oss/upload/images'),
        uploadHeaders: {
          token: Vue.cookie.get('token')
        }
      }
    },
    components: {},
    created () {
    },
    methods: {
      init (id) {
        this.dataForm.id = id || null
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (id) {
            this.getDatadictInfo(id)
          }
        })
      },
      dataFormSubmit () {
        this.$confirm('确认保存？')
          .then(() => {
            this.saveDatadict()
          })
          .catch(() => {
          })
      },
      getDatadictInfo (id) {
        const that = this
        this.$http({
          url: this.$http.adornUrl('/sys/api/getOpeningNoticeInfo'),
          method: 'get',
          params: this.$http.adornParams({
            id: id
          })
        }).then(({data}) => {
          if (data && data.code === 200) {
            that.openingNotice = data.data
            this.dataForm = {
              ...this.openingNotice
            }
          }
        })
      },
      saveDatadict () {
        this.$http({
          url: this.$http.adornUrl(`/sys/api/saveOpeningNotice`),
          method: 'post',
          data: this.$http.adornData({
            ...this.dataForm
          })
        }).then(({data}) => {
          if (data && data.code === 200) {
            this.$message({
              message: '保存成功',
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
      },
      fileBeforeUpload (file) {
        console.log('upload file', file)
      },
      fileUploadSuccess (response, file, fileList) {
        console.log('upload file response', response)
        if (response.code === 200) {
          this.$nextTick(() => {
            this.dataForm.imageUrl = response.data
          })
        }
      }
    }
  }
</script>
<style>
  .selectWidth {
    width: 100%;
  }

  .el-tag + .el-tag {
    margin-left: 10px;
  }

  .button-new-tag {
    margin-left: 10px;
    height: 32px;
    line-height: 30px;
    padding-top: 0;
    padding-bottom: 0;
  }

  .input-new-tag {
    width: 90px;
    margin-left: 10px;
    vertical-align: bottom;
  }
  .image-container {
    display: flex;
    align-items: flex-end;
  }

  .image-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }

  .image-uploader .el-upload:hover {
    border-color: #409EFF;
  }

  .image-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
  }

  .image {
    width: 178px;
    height: 178px;
    display: block;
  }

</style>
