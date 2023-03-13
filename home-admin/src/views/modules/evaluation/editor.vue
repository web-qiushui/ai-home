<template>
  <div class="evaluation-form">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" label-width="80px">
      <el-form-item label="标题" prop="title">
        <el-input v-model="dataForm.title" placeholder="标题"></el-input>
      </el-form-item>
      <el-form-item label="作者" prop="author" >
        <el-input v-model="dataForm.author"></el-input>
      </el-form-item>
      <el-form-item label="原创" prop="author" >
        <el-input v-model="dataForm.original"></el-input>
      </el-form-item>
      <el-form-item label="公众号" prop="author" >
        <el-input v-model="dataForm.thePublicName"></el-input>
      </el-form-item>
      <el-form-item label="描述" prop="description">
        <el-input v-model="dataForm.description"></el-input>
      </el-form-item>
      <el-form-item label="区域" prop="region">
        <el-select v-model="dataForm.region" clearable placeholder="请选择">
          <el-option
            v-for="item in regionOptions"
            :key="item.key"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="价格" prop="price" >
        <el-select v-model="dataForm.price" clearable placeholder="请选择">
          <el-option
            v-for="item in priceOptions"
            :key="item.key"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="面积" prop="area" >
        <el-select v-model="dataForm.area" clearable placeholder="请选择">
          <el-option
            v-for="item in areaOptions"
            :key="item.key"
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
      <el-form-item label="状态" size="mini" prop="status">
        <el-radio-group v-model="dataForm.status">
          <el-radio :label="0">隐藏</el-radio>
          <el-radio :label="1">显示</el-radio>
        </el-radio-group>
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
      <el-form-item label="内容" prop="content">
        <script :id="ueId" class="ueditor-box" type="text/plain" style="width: 100%; height: 260px;" v-model="dataForm.type"></script>
      </el-form-item>
    </el-form>
    <div class="evaluation-form-btns">
      <el-button @click="cancel()">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </div>
  </div>
</template>

<script>
  import ueditor from 'ueditor'
  import Vue from 'vue'

  export default {
    data () {
      return {
        ue: null,
        ueId: `J_ueditorBox_${new Date().getTime()}`,
        ueContent: '',
        id: null,
        type: 1,
        evaluation: {},
        dialogVisible: false,
        dataForm: {
          id: null,
          title: '',
          author: '',
          original: '',
          thePublicName: '',
          description: '',
          region: '',
          price: '',
          area: '',
          label: [],
          content: '',
          type: 1,
          status: 1
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
          description: '',
          region: '',
          price: '',
          area: '',
          label: [],
          content: '',
          type: 1,
          mainImage: ''
        }
        this.initOptions()
      }
    },
    mounted () {
      this.ue = ueditor.getEditor(this.ueId, {
        // serverUrl: '', // 服务器统一请求接口路径
        zIndex: 3000
      })
    },
    methods: {
      getContent () {
        this.ue.ready(() => {
          this.dataForm.content = this.ue.getContent()
        })
      },

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
        return list.filter(item => item.n1 === key).map(item => {
          return {
            id: item.id,
            key: item.id,
            label: item.name,
            value: item.name,
            type: item.type
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
            // const content = this.getHtmlContent()
            // if (content) {
            //   this.dataForm.content = content
            // }
            this.getContent()
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
            that.ue.ready(function () {
              // 赋值给UEditor
              that.ue.setContent(that.evaluation.content)
            });
          } else {
            this.evaluation = {}
          }
        })
      },
      saveEvaluation () {
        debugger
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

      // 标签
      handleClose (tag) {
        this.dataForm.label.splice(this.dataForm.label.indexOf(tag), 1)
      },

      showInput () {
        this.tagInputVisible = true
        this.$nextTick(() => {
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
          if (this.dataForm.label.includes(inputValue)) {
            this.dataForm.label = this.dataForm.label.filter(item => {
              return item !== inputValue
            })
          }
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
      },
      uploadImgHook (v) {
        console.log('upload image hook', v)
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

    .content-editor {
      // width: 400px;
      height: 400px;
      border: 1px solid #dcdfe6;
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


    .evaluation-form-btns {
      margin-top: 50px;
      display: flex;
      flex-direction: row;
      flex-wrap: nowrap;
      justify-content: flex-end;
      align-items: flex-end;
    }
  }

  .mod-demo-ueditor {
    position: relative;
    z-index: 510;
    > .el-alert {
      margin-bottom: 10px;
    }
  }

  .edui-button.edui-for-135editor .edui-button-wrap .edui-button-body .edui-icon{

    background-image: url("http://static.135editor.com/img/icons/editor-135-icon.png") !important;

    background-size: 85%;

    background-position: center;

    background-repeat: no-repeat;

  }
</style>
