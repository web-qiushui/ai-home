<template>
  <el-dialog
    title="修改"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
             label-width="80px">
      <el-form-item label="标题" prop="title" :class="{ 'is-required': true }">
        <el-input v-model="dataForm.title" placeholder="登录帐号"></el-input>
      </el-form-item>
      <el-form-item label="作者" prop="author" :class="{ 'is-required': true }">
        <el-input v-model="dataForm.author"></el-input>
      </el-form-item>
      <el-form-item label="描述" prop="description" :class="{ 'is-required': true }">
        <el-input v-model="dataForm.description"></el-input>
      </el-form-item>
      <el-form-item label="区域" prop="region">
        <el-select v-model="dataForm.region" placeholder="请选择">
          <el-option
            v-for="item in regionOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="价格" prop="price">
        <el-select v-model="dataForm.price" placeholder="请选择">
          <el-option
            v-for="item in priceOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="面积" prop="area">
        <el-select v-model="dataForm.area" placeholder="请选择">
          <el-option
            v-for="item in areaOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="标签" prop="label">
        <el-tag
          :key="tag"
          v-for="tag in dataForm.label"
          closable
          :disable-transitions="false"
          @close="handleClose(tag)">
          {{tag}}
        </el-tag>
        <el-autocomplete
          class="input-new-tag"
          v-if="tagInputVisible"
          v-model="tagInputValue"
          :fetch-suggestions="querySearch"
          ref="saveTagInput"
          size="small"
          @select="handleTagSelect"
          @keyup.enter.native="handleInputConfirm"
          @change="handleInputConfirm"
        ></el-autocomplete>
        <el-button v-else class="button-new-tag" size="small" @click="showInput">+ New Tag</el-button>
      </el-form-item>
      <el-form-item label="背景图" prop="mainImage">
        <div class="image-container">
          <el-upload class="image-uploader"
            :action="imageUploadUrl"
            :headers="uploadHeaders"
            :show-file-list="false"
            :on-success="fileUploadSuccess"
            :before-upload="fileBeforeUpload"
            :auto-upload="true">
            <img v-if="dataForm.mainImage" :src="getImageUrl(dataForm.mainImage)" class="image">
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
        evaluation: {},
        dataForm: {
          id: null,
          title: '',
          author: '',
          description: '',
          region: '',
          price: '',
          area: '',
          label: [],
          content: '',
          type: 0
        },
        dataRule: {},
        regionOptions: [],
        priceOptions: [],
        areaOptions: [],
        tagOptions: [],
        tagInputVisible: false,
        tagInputValue: ''
      }
    },
    created () {
    },
    methods: {
      init (id) {
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (id) {
            this.getEvaluation(id)
          }
          this.initOptions()
        })
      },
      initOptions () {
        const datadict = Object.assign([], this.$store.state.common.datadict)
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
            this.dataForm = {
              ...this.evaluation
            }
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
</style>
