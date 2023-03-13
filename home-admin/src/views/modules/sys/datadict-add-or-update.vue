<template>
  <el-dialog
    :title="datadictName"
    :close-on-click-modal="false"
    :visible.sync="visible"
    append-to-body>
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
             label-width="80px">
      <el-form-item label="字典类型" prop="type" :class="{ 'is-required': true }">
        <el-input v-model="dataForm.type" placeholder="字典类型" :disabled="isDisabled"></el-input>
      </el-form-item>
      <el-form-item label="字典名" prop="name" :class="{ 'is-required': true }">
        <el-input v-model="dataForm.name" placeholder="字典名"></el-input>
      </el-form-item>
      <el-form-item label="字典值" prop="value" :class="{ 'is-required': true }" v-if="isShow">
        <el-input v-model="dataForm.value" placeholder="字典值"></el-input>
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
  export default {
    data () {
      return {
        visible: false,
        datadictName: '',
        singlePage: {},
        dataForm: {
          id: null,
          type: '',
          name: '',
          value: '',
          n1: '',
          n4: '',
          status: 1
        },
        dataRule: {},
        isShow: true,
        isDisabled: true,
        tagInputVisible: false,
        tagInputValue: ''
      }
    },
    components: {
    },
    created () {
    },
    methods: {
      init (id, type) {
        if (type == null) {
          this.isShow = false
          this.isDisabled = true
          this.dataForm.n1 = ''
          this.dataForm.n4 = 1
        } else {
          this.isShow = true
          this.isDisabled = false
          this.dataForm.n1 = type
          this.dataForm.n4 = 2
        }
        this.dataForm.id = id || null
        this.dataForm.id = id || null
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (id) {
            this.getDatadictInfo(id)
            this.datadictName = '修改'
          } else {
            this.datadictName = '新增'
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
          url: this.$http.adornUrl('/sys/datadict/getDatadictInfo'),
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
      saveDatadict () {
        this.$http({
          url: this.$http.adornUrl(`/sys/datadict/saveDatadict`),
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
