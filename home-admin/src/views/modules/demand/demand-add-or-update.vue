<template>
  <el-dialog
    title="修改"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
             label-width="80px">
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
      <el-form-item label="说明" prop="explanation">
        <el-input v-model="dataForm.explanation" placeholder="说明"></el-input>
      </el-form-item>
      <el-form-item label="电话" prop="phone">
        <el-input v-model="dataForm.phone" placeholder="电话"></el-input>
      </el-form-item>
      <el-form-item label="意向楼盘" prop="intentions">
        <el-input v-model="dataForm.intentions" placeholder="意向楼盘"></el-input>
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
        singlePage: {},
        dataForm: {
          id: null,
          region: '',
          price: '',
          area: '',
          label: [],
          explanation: '',
          phone: '',
          intentions: ''
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
    components: {
    },
    created () {
      this.initOptions()
    },
    methods: {
      init (id) {
        this.dataForm.id = id || null
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (id) {
            this.getDemandInfo(id)
          }
        })
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
      querySearch (queryString, cb) {
        var tags = this.tagOptions
        var results = queryString ? tags.filter(this.createFilter(queryString)) : tags
        // 调用 callback 返回建议列表的数据
        cb(results)
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
          this.dataForm.label.push(inputValue)
        }
        this.tagInputVisible = false
        this.tagInputValue = ''
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
      dataFormSubmit () {
        this.$confirm('确认保存？')
          .then(() => {
            this.saveDemand()
          })
          .catch(() => {
          })
      },
      getDemandInfo (id) {
        const that = this
        this.$http({
          url: this.$http.adornUrl('/sys/api/getDemandInfo'),
          method: 'get',
          params: this.$http.adornParams({
            id: id
          })
        }).then(({data}) => {
          if (data && data.code === 200) {
            that.demand = data.data
            this.dataForm = {
              ...this.demand
            }
          }
        })
      },
      saveDemand () {
        this.$http({
          url: this.$http.adornUrl(`/sys/api/saveDemand`),
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
