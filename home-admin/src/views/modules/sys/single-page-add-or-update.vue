<template>
  <el-dialog
    title="修改"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
             label-width="80px">
      <el-form-item label="标题" prop="title" :class="{ 'is-required': true }">
        <el-input v-model="dataForm.title" placeholder="页面标题"></el-input>
      </el-form-item>
      <el-form-item label="类型" prop="author" :class="{ 'is-required': true }">
        <el-input v-model="dataForm.type" placeholder="页面类型"></el-input>
      </el-form-item>
      <el-form-item label="编码" prop="code" :class="{ 'is-required': true }">
        <el-input v-model="dataForm.code" placeholder="页面编码"></el-input>
      </el-form-item>
      <el-form-item label="主图" prop="coverUrl" :class="{ 'is-required': true }">
        <div class="image-container">
          <el-upload class="image-uploader"
                     :action="imageUploadUrl"
                     :headers="uploadHeaders"
                     :show-file-list="false"
                     :on-success="fileUploadSuccess"
                     :before-upload="fileBeforeUpload"
                     :auto-upload="true">
            <img v-if="dataForm.coverUrl" :src="getImageUrl(dataForm.coverUrl)" class="image">
            <i v-else slot="default" class="el-icon-plus image-uploader-icon"></i>
          </el-upload>
        </div>
      </el-form-item>
      <el-form-item label="状态" size="mini" prop="status" :class="{ 'is-required': true }">
        <el-radio-group v-model="dataForm.status">
          <el-radio :label="0">隐藏</el-radio>
          <el-radio :label="1">显示</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="内容" prop="content" :class="{ 'is-required': true }">
        <WangEditor v-model="dataForm.content" v-bind:content="dataForm.content" @editorContent="getEditorContent"/>
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
  import WangEditor from '@/components/editor/WangEditor'
  export default {
    data () {
      return {
        visible: false,
        imageUploadUrl: this.$http.adornUrl('/sys/oss/upload/images'),
        uploadHeaders: {
          token: Vue.cookie.get('token')
        },
        singlePage: {},
        dataForm: {
          id: null,
          status: 0,
          title: '',
          type: '',
          code: '',
          content: '',
          coverUrl: ''
        },
        dataRule: {},
        tagInputVisible: false,
        tagInputValue: ''
      }
    },
    components: {
      WangEditor
    },
    created () {
    },
    methods: {
      init (id) {
        this.dataForm.id = id || null
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (id) {
            this.getSinglePageInfo(id)
          }
        })
      },
      dataFormSubmit () {
        this.$confirm('确认保存？')
          .then(() => {
            this.saveSinglePage()
          })
          .catch(() => {
          })
      },
      getSinglePageInfo (id) {
        const that = this
        this.$http({
          url: this.$http.adornUrl('/sys/singlePage/getSinglePageInfo'),
          method: 'get',
          params: this.$http.adornParams({
            id: id
          })
        }).then(({data}) => {
          if (data && data.code === 200) {
            that.singlePage = data.data
            this.dataForm = {
              ...this.singlePage
            }
          }
        })
      },
      saveSinglePage () {
        this.$http({
          url: this.$http.adornUrl(`/sys/singlePage/saveSinglePage`),
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
            this.dataForm.mainImage = response.data
          })
        }
      },
      // 获取编辑器内容
      getEditorContent (data) {
        this.dataForm.content = data
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
