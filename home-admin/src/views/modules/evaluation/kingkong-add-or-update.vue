<template>
  <el-dialog
    title="修改"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
             label-width="80px">
      <el-form-item label="名称" prop="name">
        <el-input v-model="dataForm.name" placeholder="名称"></el-input>
      </el-form-item>
      <el-form-item label="类型" prop="type">
        <el-input v-model="dataForm.type" readonly disabled></el-input>
      </el-form-item>
      <el-form-item label="状态" size="mini" prop="status">
        <el-radio-group v-model="dataForm.status">
          <el-radio :label="0">禁用</el-radio>
          <el-radio :label="1">正常</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="背景图" prop="imagesUrl">
        <div class="image-container">
          <el-upload class="image-uploader"
            :action="imageUploadUrl"
            :headers="uploadHeaders"
            :show-file-list="false"
            :on-success="fileUploadSuccess"
            :before-upload="fileBeforeUpload"
            :auto-upload="true">
            <img v-if="dataForm.imagesUrl" :src="getImageUrl(dataForm.imagesUrl)" class="image">
            <i v-else slot="default" class="el-icon-plus image-uploader-icon"></i>
          </el-upload>
        </div>
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
        imageUploadUrl: this.$http.adornUrl('/sys/oss/upload/images'),
        uploadHeaders: {
          token: Vue.cookie.get('token')
        },
        dataForm: {
          name: '',
          type: '',
          status: 1,
          imagesUrl: '',
          evaluationIds: []
        },
        dataRule: {}
      }
    },
    created () {
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
      // 表单提交
      dataFormSubmit () {
        this.$http({
          url: this.$http.adornUrl(`/sys/config/saveKingKongConfig`),
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
      },
      fileBeforeUpload (file) {
        console.log('upload file', file)
      },
      fileUploadSuccess (response, file, fileList) {
        if (response.code === 200) {
          this.$nextTick(() => {
            this.dataForm.imagesUrl = response.data
          })
        }
      }
    }
  }
</script>
<style lang="scss" scoped>
  .selectWidth {
    width: 100%;
  }
  .image-container {
    display: flex;
    align-items: flex-end;

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

  }
</style>
