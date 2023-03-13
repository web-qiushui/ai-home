<template>
  <el-dialog
    :title="dataForm.id ? '修改' : '新增'"
    :close-on-click-modal="false"
    :visible.sync="visible"
  >
    <el-form
      :model="dataForm"
      :rules="dataRule"
      ref="dataForm"
      @keyup.enter.native="dataFormSubmit()"
      label-width="80px"
      id="dataForm"
    >
      <el-form-item label="报告ID" prop="homeDemandId">
        <el-input v-model="dataForm.homeDemandId" disabled readonly></el-input>
      </el-form-item>
      <el-form-item label="用户" prop="homeUserId">
        <el-input v-model="dataForm.homeUserId" placeholder="用户"></el-input>
      </el-form-item>
      <el-form-item label="顾问" prop="homeConsultantId">
        <el-input
          v-model="dataForm.homeConsultantId"
          placeholder="顾问"
        ></el-input>
      </el-form-item>
      <el-form-item label="地址" prop="address">
        <el-input v-model="dataForm.address" placeholder="地址"></el-input>
      </el-form-item>
      <el-form-item label="品牌" prop="brand">
        <el-input v-model="dataForm.brand" placeholder="品牌"></el-input>
      </el-form-item>
      <el-form-item label="楼盘" prop="realEstate">
        <el-input v-model="dataForm.realEstate" placeholder="楼盘"></el-input>
      </el-form-item>
      <el-form-item label="价格" prop="price">
        <el-input v-model="dataForm.price" placeholder="价格"></el-input>
      </el-form-item>
      <el-form-item label="折扣" prop="discount">
        <el-input v-model="dataForm.discount" placeholder="折扣"></el-input>
      </el-form-item>
      <el-form-item label="匹配度" prop="suitability">
        <el-input
          v-model="dataForm.suitability"
          placeholder="匹配度"
        ></el-input>
      </el-form-item>
      <el-form-item label="项目优势" prop="projectAdvantage">
        <el-input
          v-model="dataForm.projectAdvantage"
          placeholder="项目优势"
        ></el-input>
      </el-form-item>
      <el-form-item label="项目劣势" prop="projectDisadvantage">
        <el-input
          v-model="dataForm.projectDisadvantage"
          placeholder="项目劣势"
        ></el-input>
      </el-form-item>
      <el-form-item label="相关推荐" prop="relatedSuggestion">
        <el-input
          v-model="dataForm.relatedSuggestion"
          placeholder="相关推荐"
        ></el-input>
      </el-form-item>
      <el-form-item label="版权" prop="copyright">
        <el-input v-model="dataForm.copyright" placeholder="版权"></el-input>
      </el-form-item>
      <el-form-item label="免责声明" prop="disclaimer">
        <el-input
          v-model="dataForm.disclaimer"
          placeholder="免责声明"
        ></el-input>
      </el-form-item>
      <el-form-item label="背景图" prop="imageUrl">
        <div class="image-container">
          <el-upload
            class="image-uploader"
            :action="imageUploadUrl"
            :headers="uploadHeaders"
            :show-file-list="false"
            :on-success="fileUploadSuccess"
            :before-upload="fileBeforeUpload"
            :auto-upload="true"
          >
            <img
              v-if="dataForm.imageUrl"
              :src="getImageUrl(dataForm.imageUrl)"
              class="image"
            />
            <i
              v-else
              slot="default"
              class="el-icon-plus image-uploader-icon"
            ></i>
          </el-upload>
        </div>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
      <el-button type="primary" @click="generateImage()">生成图片</el-button>
    </span>
  </el-dialog>
</template>

<script>
import Vue from 'vue'
import html2canvas from 'html2canvas'

export default {
  data() {
    return {
      visible: false,
      imageUploadUrl: this.$http.adornUrl("/sys/oss/upload/images"),
      uploadHeaders: {
        token: Vue.cookie.get("token")
      },
      singlePage: {},
      dataForm: {
        id: null,
        address: "",
        brand: "",
        copyright: "",
        disclaimer: "",
        discount: "",
        homeConsultantId: null,
        homeDemandId: null,
        homeUserId: null,
        imageUrl: "",
        price: "",
        projectAdvantage: "",
        projectDisadvantage: "",
        realEstate: "",
        relatedSuggestion: "",
        suitability: ""
      },
      dataRule: {}
    };
  },
  components: {},
  created() {
  },
  methods: {
    init(id) {
      // this.dataForm.id = id || null;
      this.visible = true;
      this.$nextTick(() => {
        this.$refs["dataForm"].resetFields();
        if (id) {
          this.getReportInfo(id);
        }
      });
    },
    dataFormSubmit() {
      this.$confirm("确认保存？")
        .then(() => {
          this.saveReport();
        })
        .catch(() => {});
    },
    getReportInfo(id) {
      const that = this;
      this.$http({
        url: this.$http.adornUrl("/sys/api/getHomeReportInfo"),
        method: "get",
        params: this.$http.adornParams({
          id: id
        })
      }).then(({ data }) => {
        if (data && data.code === 200) {
          that.report = data.data;
          this.dataForm = {
            ...this.report
          };
        }
      });
    },
    saveReport() {
      this.$http({
        url: this.$http.adornUrl(`/sys/api/saveHomeReport`),
        method: "post",
        data: this.$http.adornData({
          ...this.dataForm
        })
      }).then(({ data }) => {
        if (data && data.code === 200) {
          this.$message({
            message: "保存成功",
            type: "success",
            duration: 1500,
            onClose: () => {
              this.visible = false;
              this.$emit("refreshDataList");
            }
          });
        } else {
          this.$message.error(data.msg);
        }
      });
    },
    fileBeforeUpload(file) {
      console.log("upload file", file);
    },
    fileUploadSuccess(response, file, fileList) {
      if (response.code === 200) {
        this.$nextTick(() => {
          this.dataForm.imageUrl = response.data;
        });
      }
    },
    generateImage() {
      const dom = document.querySelector('#dataForm')
      console.log('dom', dom)
      html2canvas(dom).then(canvas => {
          // document.body.appendChild(canvas)
          this.saveImage(canvas)
          console.log("canvas", canvas)
      });
    },
    saveImage(canvas) {
      if (!canvas) {
        return
      }
      const imgData = canvas.toDataURL({format: 'png', quality:1});
      const strDataURI = imgData.substr(22, imgData.length);
      const blob = dataURLtoBlob(imgData);

      const link = document.createElement("a");
      const objurl = URL.createObjectURL(blob);
      link.download = `购房报告-${this.dataForm.id}${this.guid()}`;
      link.href = objurl;
      link.click();

      // blob.arrayBuffer().then(buffer => {
      //   console.log('buffer', buffer)
      //   this.uploadImage(buffer)
      // })

      this.uploadImage(imgData)


      console.log('blob', blob, objurl, imgData)

      function  dataURLtoBlob(dataurl) {
          let arr = dataurl.split(','),
          mime = arr[0].match(/:(.*?);/)[1],
          bstr = atob(arr[1]),
          n = bstr.length,
          u8arr = new Uint8Array(n);

          while(n--){
            u8arr[n] = bstr.charCodeAt(n);
          }
          return new Blob([u8arr], {type:mime})
        }
    },
    uploadImage(imgData) {
      this.$http({
        url: this.$http.adornUrl(`/sys/oss/upload/images`),
        method: "post",
        data: {
          file: imgData
        }
      }).then(res => {
        console.log(res)
      })
    }
  }
};
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
}
.image-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.image-uploader .el-upload:hover {
  border-color: #409eff;
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
</style>
