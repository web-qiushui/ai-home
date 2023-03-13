<template>
  <div>
    <div ref="editor" style="text-align:left"></div>
  </div>
</template>

<script>
  import E from 'wangeditor'
  import Vue from 'vue'

  export default {
    name: 'WangEditor',
    data () {
      return {
        // 真正的编辑器里的内容
        editorContent: '',
        // 图片上传地址
        imageUploadUrl: this.$http.adornUrl('/sys/oss/upload/images'),
        // 编辑器对象
        editor: null
      }
    },
    watch: {
      //  watch 表示监听  当父组件的内容变化时需要更新编辑器的内容
      content () {
        this.editor.txt.html(this.content)
      }
    },
    /**
     * 这里使用了 Vue 的 mounted 函数钩子，这属于 Vue 生命周期的一个阶段
     */
    mounted () {
      const editor = new E(this.$refs.editor)
      editor.customConfig.zIndex = 10000
      // 当编辑器内容变化的时候通知父组件
      editor.customConfig.onchange = (html) => {
        this.editorContent = html
        // 通知父级控件方法，富组件可以通过 editorContent 事件去获取最新的编辑器内容
        this.$emit('editorContent', html)
      }
      // 下面两个配置，使用其中一个即可显示“上传图片”的tab。但是两者不要同时使用！！！
      // editor.customConfig.uploadImgShowBase64 = true   // 使用 base64 保存图片
      editor.customConfig.uploadImgServer = this.imageUploadUrl  // 上传图片到服务器
      // 图片的大小和数量都是用默认值  5M和1000张
      // 将图片大小限制为 3M
      editor.customConfig.uploadImgMaxSize = 3 * 1024 * 1024
      // 限制一次最多上传 5 张图片
      // editor.customConfig.uploadImgMaxLength = 5
      // 上传图片时，可自定义filename，即在使用formdata.append(name, file)添加图片文件时，自定义第一个参数。
      editor.customConfig.uploadFileName = 'file'
      // 上传图片时，添加请求头
      editor.customConfig.uploadImgHeaders = {
        'token': Vue.cookie.get('token')
      }
      // 可使用监听函数在上传图片的不同阶段做相应处理
      editor.customConfig.uploadImgHooks = {
        // 图片上传并返回结果，图片插入成功之后触发
        // xhr 是 XMLHttpRequst 对象，editor 是编辑器对象，result 是服务器端返回的结果
        customInsert: function (insertImg, result, editor) {
          if (result.code === 200) {
            const imageUrl = window.SITE_CONFIG.imageUrl + result.data
            insertImg(imageUrl)
          }
        }
      }
      editor.create()
      editor.txt.html(this.content)
      this.editor = editor
    },
    props: {
      // 传递过来的编辑器内容参数，用于设置编辑器内容
      content: {
        type: String,
        default: ''
      }
    }
  }
</script>

<style scoped>
  .editor {
    width: 100%;
    margin: 0 auto;
    position: relative;
    z-index: 0;
  }

  .toolbar {
    border: 1px solid #ccc;
  }

  .text {
    border: 1px solid #ccc;
    min-height: 500px;
  }
</style>
