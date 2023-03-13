<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
             label-width="80px">
      <el-form-item label="编码" prop="code">
        <el-input v-model="dataForm.code" readonly disabled placeholder="编码"></el-input>
      </el-form-item>
      <el-form-item label="名称" prop="name">
        <el-input v-model="dataForm.name" placeholder="名称"></el-input>
      </el-form-item>
      <el-form-item label="角色" prop="auth">
        <el-select v-model="dataForm.auth" placeholder="请选择">
          <el-option
            v-for="item in options"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="排序" prop="sort">
        <el-input v-model="dataForm.sort" type="number"></el-input>
      </el-form-item>
      <el-form-item label="状态" size="mini" prop="status">
        <el-radio-group v-model="dataForm.status">
          <el-radio :label="0">禁用</el-radio>
          <el-radio :label="1">正常</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="图标" prop="imagesUrl">
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
         options: [
          {label: '全部', value: 0},
          {label: '用户', value: 1},
          {label: 'AI顾问', value: 2},
          {label: '置业顾问', value: 3},
          {label: '顾问', value: 4},
        ],
        dataRule: {}
      }
    },
    created () {
    },
    methods: {
      init (form) {
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          this.dataForm = {
            ...form
          }
        })
      },
      // 表单提交
      dataFormSubmit () {
        this.$http({
          url: this.$http.adornUrl(`/sys/config/saveChatCardConfig`),
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
