<template>
  <div class="evaluation-form">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" label-width="80px">
      <el-form-item label="标题" prop="title" :class="{ 'is-required': true }">
        <el-input v-model="dataForm.title" placeholder="标题"></el-input>
      </el-form-item>
      <el-form-item label="作者" prop="author" :class="{ 'is-required': true }">
        <el-input v-model="dataForm.author"></el-input>
      </el-form-item>
      <el-form-item label="状态" size="mini" prop="status">
        <el-radio-group v-model="dataForm.status">
          <el-radio :label="0">隐藏</el-radio>
          <el-radio :label="1">显示</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="内容" prop="content">
        <el-input
          type="textarea"
          :autosize="{ minRows: 2, maxRows: 4}"
          placeholder="请输入内容"
          v-model="dataForm.content">
        </el-input>
      </el-form-item>
    </el-form>
    <div class="evaluation-form-btns">
      <el-button @click="cancel()">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </div>
    <el-dialog
      title="内容"
      :visible.sync="dialogVisible"
      :append-to-body="true">
      {{ ueContent }}
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="dialogVisible = false">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
  import ueditor from 'ueditor'
  import Vue from 'vue'

  export default {
    data () {
      return {
        id: null,
        type: 1,
        evaluation: {},
        ue: null,
        ueId: `J_ueditorBox_${new Date().getTime()}`,
        ueContent: '',
        dialogVisible: false,
        dataForm: {
          id: null,
          title: '',
          author: '',
          region: '',
          price: '',
          area: '',
          label: [],
          content: '',
          type: 2
        },
        dataRule: {},
        regionOptions: [],
        priceOptions: [],
        areaOptions: [],
        tagOptions: [],
        imageUploadUrl: this.$http.adornUrl('/sys/oss/upload/images'),
        uploadHeaders: {
          token: Vue.cookie.get('token')
        },
        value: '',
        tagInputVisible: false,
        tagInputValue: ''
      }
    },
    activated () {
      this.id = this.$route.query.id
      if (this.id) {
        this.getEvaluation(this.id)
      } else {
        this.dataForm = {
          id: null,
          title: '',
          author: '',
          region: '',
          price: '',
          area: '',
          label: [],
          content: '',
          type: 2,
          status: 1
        }
      }
    },
    computed: {
      uploadFileList () {
        if (this.dataForm.mainImage) {
          return [{
            name: '',
            url: this.getImageUrl(this.dataForm.mainImage)
          }]
        }

        return []
      }
    },
    mounted () {
      this.ue = ueditor.getEditor(this.ueId, {
        // serverUrl: '', // 服务器统一请求接口路径
        zIndex: 3000
      })
    },
    methods: {
      initForm (evaluation) {
        this.dataForm = {
          ...evaluation
        }
        this.initOptions()
      },
      initOptions () {
        const datadict = Object.assign([], this.$store.state.common.datadict)
        console.log(datadict)
        this.regionOptions = this.getForDropDown(datadict, 'REGION_SELECT')
        this.priceOptions = this.getForDropDown(datadict, 'PRICE_SELECT')
        this.areaOptions = this.getForDropDown(datadict, 'AREA_SELECT')

        const unitTypes = this.get(datadict, 'UNIT_TYPE_SELECT')
        const types = this.get(datadict, 'TYPE_SELECT')
        const others = this.get(datadict, 'OTHER_SELECT')

        this.tagOptions = [...unitTypes, ...types, ...others]
      },
      getForDropDown (list, key) {
        return list.filter(item => item.n1 === key || item.type === key).map(item => {
          return {
            id: item.id,
            label: item.type === key ? '全部' : item.name,
            value: item.id,
            type: item.type === key ? '' : item.type,
            isAll: item.type === key
          }
        })
      },
      get (list, key) {
        return list.filter(item => item.n1 === key).map(item => {
          return {
            id: item.id,
            label: item.name,
            value: item.name,
            type: item.type
          }
        })
      },
      querySearch (queryString, cb) {
        var tags = this.tagOptions
        var results = queryString ? tags.filter(this.createFilter(queryString)) : tags
        // 调用 callback 返回建议列表的数据
        cb(results)
      },
      createFilter (queryString) {
        return (tags) => {
          return (tags.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0)
        }
      },
      dataFormSubmit () {
        this.$confirm('确认保存？')
          .then(() => {
            this.saveEvaluation()
          })
          .catch(() => {
          })
      },
      getEvaluation (id) {
        const that = this
        this.$http({
          url: this.$http.adornUrl('/sys/api/getEvaluationInfo'),
          method: 'get',
          params: this.$http.adornParams({
            id: id
          })
        }).then(({data}) => {
          if (data && data.code === 200) {
            that.evaluation = data.data
            that.initForm(that.evaluation)
          } else {
            this.evaluation = {}
          }
        })
      },
      saveEvaluation () {
        this.$http({
          url: this.$http.adornUrl(`/sys/api/saveEvaluation`),
          method: 'post',
          data: this.$http.adornData({
            ...this.dataForm
          })
        }).then(({data}) => {
          if (data && data.code === 200) {
            this.$message({
              message: '保存成功',
              type: 'success',
              duration: 1500
            })
            this.toEvaluationList()
          } else {
            this.$message.error(data.msg)
          }
        })
      },
      cancel () {
        this.toEvaluationList()
      },
      toEvaluationList () {
        this.$router.back(-1)
      },
      getContent () {
        this.dialogVisible = true
        this.ue.ready(() => {
          this.ueContent = this.ue.getContent()
        })
      },

      // 标签
      handleClose (tag) {
        this.dataForm.label.splice(this.dataForm.label.indexOf(tag), 1)
      },

      showInput () {
        this.tagInputVisible = true
        this.$nextTick(_ => {
          this.$refs.saveTagInput.focus()
        })
      },
      handleTagSelect (e) {
        console.log('handle tag select', e)
        this.tagInputValue = e.value
        this.handleInputConfirm()
      },
      handleInputConfirm () {
        let inputValue = this.tagInputValue
        if (inputValue) {
          this.dataForm.label.push(inputValue)
        }
        this.tagInputVisible = false
        this.tagInputValue = ''
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
      }
    }
  }
</script>

<style lang="scss">
  .evaluation-form {
    position: relative;
    z-index: 510;

    > .el-alert {
      margin-bottom: 10px;
    }

    .ueditor-box {
      width: calc(100% - 80px) !important;
      margin-top: -50px;
      margin-left: 80px;
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

      .evaluation-img {
        margin-right: 10px;

        img {
          width: 320px;
          height: 240px;
        }
      }

    }


    .evaluation-form-btns {
      margin-top: 50px;
      display: flex;
      flex-direction: row;
      flex-wrap: nowrap;
      justify-content: flex-end;
      align-items: flex-end;
    }
  }
</style>
