## service-vue
- service-vue基于vue、element-ui构建开发
- 前后端分离，通过token进行数据交互，可独立部署
- 主题定制，通过scss变量统一一站式定制
- 动态菜单，通过菜单管理统一管理访问路由
- 数据切换，通过mock配置对接口数据／mock模拟数据进行切换
- 发布时，可动态配置CDN静态资源／切换新旧版本
- 演示地址：[http://localhost:8080/service](http://localhost:8080/service) (账号密码：admin/admin)
  

## 说明文档

### 打包发布

```
# 构建生产环境(默认)
npm run build

# 构建测试环境
npm run build --qa

# 构建验收环境
npm run build --uat

# 构建生产环境
npm run build --prod
```

### 常见问题

##### 开发时，如何连接后台项目api接口？

修改`/static/config/index.js`目录文件中` window.SITE_CONFIG['baseUrl'] = '本地api接口请求地址';`

##### 开发时，如何解决跨域？

1. 修改`/config/dev.env.js`目录文件中`OPEN_PROXY: true`开启代理
2. 修改`/config/index.js`目录文件中`proxyTable`对象`target: '代理api接口请求地址'`
3. 重启本地服务

##### 开发时，如何提前配置CDN静态资源？

修改`/static/config/index-[qa/uat/prod].js`目录文件中`window.SITE_CONFIG['domain'] = '静态资源cdn地址';`

##### 构建生成后，发布需要上传哪些文件？

```
/dist`目录下：`1805021549（静态资源，18年05月03日15时49分）、config（配置文件）、index.html
```

##### 构建生成后，如何动态配置CDN静态资源？

修改`/dist/config/index.js`目录文件中`window.SITE_CONFIG['domain'] = '静态资源cdn地址';`

##### 构建生成后，如何动态切换新旧版本？

修改`/dist/config/index.js`目录文件中` window.SITE_CONFIG['version'] = '旧版本号';`

### 主题定制

提供12套颜色主题，进行element-ui和整站主题切换。具体切换方法如下：

1. 修改[/src/element-ui-theme/index.js]()文件中`import './element-[#17b3a3]/index.css'`[]中括号中的值，值可以在同文件中`list`属性中取即可。**（注意：这里只是修改element-ui组件主题）**
2. 修改[/src/assets/scss/_variables.scss]()文件中`$--color-primary: [#17b3a3];`[]中括号中的值，值与第一步值同步即可。**（注意：这里只是修改站点主题，不包括element-ui组件主题）**

主题定制具体实现方法是：

1. 先通过element-ui官方提供的[在线主题生成工具](https://elementui.github.io/theme-chalk-preview/#/zh-CN)，进行切换主题色，再下载解压文件（保留`fonts目录中文件和index.css即可`）放置`/src/element-ui-theme/`目录中，使用同目录中的`index.js`进行统一配置管理。

2. 再设置修改站点主题，使整站主题色统一一致。

### 开启mock本地模拟数据

1. 通过设置[/src/mock/index.js)]()文件中`fnCreate(common, [false])`[]中括号中为true／false开启关闭当前模块mock本地模拟数据功能。（默认开启）
2. 通过设置[/src/mock/modules/]()文件下模块`isOpen: [false]`[]中括号中为`true／false`开启关闭当前api接口mock本地模拟数据功能。（默认开启）