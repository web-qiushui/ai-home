<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
             label-width="80px">
      <el-form-item label="用户" prop="userId" v-if="!dataForm.id">
        <el-select v-model="dataForm.userId" placeholder="请选择用户" style="width: 100%" value-key="id">
          <el-option
            v-for="item in userList"
            :key="item.id"
            :label="item.name"
            :value="item.id">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="姓名">
        <el-input v-model="dataForm.name" placeholder="姓名"></el-input>
      </el-form-item>
      <el-form-item label="头像">
        <el-upload
          class="avatar-uploader"
          action="https://www.029aijia.cn/home-service/sys/oss/upload/images"
          :show-file-list="false"
          :headers="headers"
          :on-success="(res,file)=>{return handleAvatarSuccess(res,file,'avatar')}">
          <img v-if="dataForm.avatar" :src="imgUrl+dataForm.avatar" class="avatar">
          <i v-else class="el-icon-plus avatar-uploader-icon"></i>
        </el-upload>
      </el-form-item>
      <el-form-item label="手机号" prop="phone">
        <el-input v-model="dataForm.phone" placeholder="手机号"></el-input>
      </el-form-item>
      <el-form-item label="身份证号" prop="idCard">
        <el-input v-model="dataForm.idCard" placeholder="身份证号"></el-input>
      </el-form-item>
      <el-form-item label="项目名称">
        <el-input v-model="dataForm.project.name" placeholder="项目名称"></el-input>
      </el-form-item>
      <el-form-item label="项目地址">
        <el-cascader :options="options" :props="{value:'label'}" v-model="dataForm.project.address"></el-cascader>
      </el-form-item>
      <el-form-item label="项目标签">
        <div>
          <el-tag
            v-for="(tag,index) in dataForm.project.label"
            :key="index"
            closable
            :type="tag" @close="handleClose(tag)">
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
        </div>
      </el-form-item>
      <el-form-item label="项目图片">
        <el-upload
          class="avatar-uploader"
          action="https://www.029aijia.cn/home-service/sys/oss/upload/images"
          :show-file-list="false"
          :headers="headers"
          :on-success="(res,file)=>{return handleAvatarSuccess(res,file,'imagesUrl')}">
          <img v-if="dataForm.project.imagesUrl" :src="imgUrl+dataForm.project.imagesUrl" class="avatar">
          <i v-else class="el-icon-plus avatar-uploader-icon"></i>
        </el-upload>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
  import {isMobile, isIdcard} from '@/utils/validate'
  import options from '../../../utils/citys'

  export default {
    data () {
      var validateMobile = (rule, value, callback) => {
        if (!isMobile(value)) {
          callback(new Error('手机号格式错误'))
        } else {
          callback()
        }
      }
      var validateIdentityCard = (rule, value, callback) => {
        if (!isIdcard(value)) {
          callback(new Error('身份号格式错误'))
        } else {
          callback()
        }
      }
      return {
        visible: false,
        dataForm: {
          userId: '',
          avatar: '',
          id: '',
          idCard: '',
          name: '',
          phone: '',
          project: {
            address: '',
            imagesUrl: '',
            label: [],
            name: ''
          }
        },
        dataRule: {
          userId: [
            {required: true, message: '请选择用户', trigger: 'blur'}
          ],
          phone: [
            {required: true, message: '手机号不能为空', trigger: 'blur'},
            {validator: validateMobile, trigger: 'blur'}
          ],
          idCard: [
            {required: true, message: '身份号不能为空', trigger: 'blur'},
            {validator: validateIdentityCard, trigger: 'blur'}
          ]
        },
        options: options,
        imgUrl: null,
        tagInputVisible: false,
        tagInputValue: '',
        tagOptions: [],
        userList: []
      }
    },
    computed: {
      headers () {
        return {
          'token': this.$cookie.get('token')
        }
      }
    },
    methods: {
      init (id) {
        this.imgUrl = window.SITE_CONFIG.imageUrl
        this.dataForm.id = id || null
        this.initOptions()
        this.visible = true
        if (this.dataForm.id) {
          this.$http({
            url: this.$http.adornUrl(`/sys/api/getConsultantInfo`),
            method: 'get',
            params: this.$http.adornParams({
              id: this.dataForm.id
            })
          }).then(({data}) => {
            if (data && data.code === 200) {
              data.data.project.address = data.data.project.address.split(' ')
              this.dataForm = data.data
            }
          })
        } else {
          this.dataForm = {
            userId: '',
            avatar: '',
            id: '',
            idCard: '',
            name: '',
            phone: '',
            project: {
              address: '',
              imagesUrl: '',
              label: [],
              name: ''
            }
          }
          this.getUserBeanList()
        }
      },
      getUserBeanList () {
        this.$http({
          url: this.$http.adornUrl('/sys/api/getUserBeanList'),
          method: 'get'
        }).then(({data}) => {
          if (data && data.code === 200) {
            this.userList = data.data
          }
        })
      },
      handleAvatarSuccess (res, file, name) {
        if (res && res.code === 200) {
          if (name === 'avatar') {
            this.dataForm.avatar = res.data
          } else {
            this.dataForm.project.imagesUrl = res.data
          }
        }
      },
      initOptions () {
        const datadict = Object.assign([], this.$store.state.common.datadict)
        const unitTypes = this.get(datadict, 'UNIT_TYPE_SELECT')
        const types = this.get(datadict, 'TYPE_SELECT')
        const others = this.get(datadict, 'OTHER_SELECT')

        this.tagOptions = [...unitTypes, ...types, ...others]
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
      handleTagSelect (e) {
        this.tagInputValue = e.value
        this.handleInputConfirm()
      },
      handleInputConfirm () {
        let inputValue = this.tagInputValue
        if (inputValue) {
          this.dataForm.project.label.push(inputValue)
        }
        this.tagInputVisible = false
        this.tagInputValue = ''
      },
      showInput () {
        this.tagInputVisible = true
        this.$nextTick(() => {
          this.$refs.saveTagInput.focus()
        })
      },
      handleClose (tag) {
        this.dataForm.project.label.splice(this.dataForm.project.label.indexOf(tag), 1)
      },
      // 表单提交
      dataFormSubmit () {
        const data = this.dataForm
        if (Array.isArray(this.dataForm.project.address)) {
          data.project.address = this.dataForm.project.address.join(' ')
        }

        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.$http({
              url: this.$http.adornUrl(`/sys/api/saveConsultant`),
              method: 'post',
              data: this.$http.adornData({
                ...data
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
        })
      }
    }
  }
</script>

<style>
  .el-form-item__content {
    height: 36px;
    display: flex;
    flex-direction: column;
    justify-content: center;
  }

  .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }

  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }

  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 60px;
    height: 60px;
    line-height: 60px;
    text-align: center;
  }

  .avatar {
    width: 60px;
    height: 60px;
    display: block;
  }
</style>
