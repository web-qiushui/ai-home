<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
             label-width="80px">
      <el-form-item label="名称" prop="content">
        <el-input v-model="dataForm.content" placeholder="名称"></el-input>
      </el-form-item>
      <el-form-item label="权限" prop="type">
        <el-select v-model="dataForm.type" placeholder="请选择">
          <el-option
            v-for="item in options"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="状态" size="mini" prop="status">
        <el-radio-group v-model="dataForm.status">
          <el-radio :label="0">禁用</el-radio>
          <el-radio :label="1">正常</el-radio>
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
        imageUploadUrl: this.$http.adornUrl('/sys/oss/upload/images'),
        uploadHeaders: {
          token: Vue.cookie.get('token')
        },
        options: [
          {label: '全部', value: 0},
          {label: '用户', value: 1},
          {label: 'AI顾问', value: 2},
          {label: '置业顾问', value: 3},
        ],
        dataForm: {
          id: '',
          content: '',
          type: '',
          status: 1
        },
        dataRule: {}
      }
    },
    created () {
    },
    computed: {
      uploadFileList () {
        if (this.dataForm.imagesUrl) {
          return [{
            name: '',
            url: this.getImageUrl(this.dataForm.imagesUrl)
          }]
        }

        return []
      }
    },
    methods: {
      init (form) {
        this.visible = true
        this.$nextTick(() => {
          if (form.id != null) {
            this.$refs['dataForm'].resetFields()
            this.dataForm = {
              ...form
            }
          }
        })
      },
      // 表单提交
      dataFormSubmit () {
        this.$http({
          url: this.$http.adornUrl(`/sys/config/saveQuickReplyConfig`),
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
<style>
  .selectWidth {
    width: 100%;
  }
</style>
