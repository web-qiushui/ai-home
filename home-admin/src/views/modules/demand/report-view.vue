<template>
  <el-dialog
    title="查看"
    :close-on-click-modal="false"
    :visible.sync="visible"
  >

    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
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
      this.visible = true;
      this.$nextTick(() => {
        if (id) {
          this.getReportInfo(id);
        }
      });
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
      const imgData =canvas.toDataURL({format: 'png', quality:1});
      const strDataURI = imgData.substr(22, imgData.length);
      const blob = dataURLtoBlob(imgData);
      this.uploadImage(blob)
      const objurl = URL.createObjectURL(blob);
      const link = document.createElement("a");
      link.download = `购房报告-${this.dataForm.id}${this.guid()}`;
      link.href = objurl;
      link.click();

      console.log(blob, objurl)

      function dataURLtoBlob(dataurl) {
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
    uploadImage(blob) {
      this.$http({
        url: this.$http.adornUrl(`/sys/oss/upload/images`),
        method: "post",
        data: this.$http.adornData({
          file: blob
        })
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
