/**
 * 开发环境
 */
;(function () {
  window.SITE_CONFIG = {};
  window.DATADICT = {};
  window.DATADICT_TYPE = {};

  // api接口请求地址
  window.SITE_CONFIG['baseUrl'] = 'https://www.029aijia.cn/home-service';
  window.SITE_CONFIG['imageUrl'] = 'https://www.029aijia.cn';

  // cdn地址 = 域名 + 版本号
  window.SITE_CONFIG['domain']  = './'; // 域名
  window.SITE_CONFIG['version'] = '';   // 版本号(年月日时分)
  window.SITE_CONFIG['cdnUrl']  = window.SITE_CONFIG.domain + window.SITE_CONFIG.version;
})();
