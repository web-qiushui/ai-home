import api from './api'
import util from '../common/util'

let request = function (method, url, data, showError = true) {
    let token = uni.getStorageSync('token');
    return new Promise((resolve, reject) => {
        uni.request({
            method,
            url,
            data,
            header: token ? {token: token} : null,
            success(res) {
                let data = res.data;
                if (data.code === 200) {
                    resolve(data.data);
                } else if (data.code === 401 || data.code === 403) {
                    uni.showModal({
                        title: '提示',
                        showCancel: false,
                        content: data.msg,
                        success: function () {
                            uni.navigateTo({url: '/pages/login/login'});
                        },
                    });
                    reject()

                } else {
                    if (showError) {
                        setTimeout(() => {
                            util.toast(data.msg);
                        }, 100);
                    }
                    reject()
                    return null;
                }
            },
            fail() {
                if (showError) {
                    util.toast('服务器繁忙,请稍后再试!');
                }
                reject()
            },
            complete(data) {
                return data;
            }
            ,
        });
    })

};
let get = (url, params, showError = true) => {
    return request('GET', url, params || {}, showError);
};

let post = (url, data, showError = true) => {
    return request('POST', url, data, showError);
};
let del = (url, data) => {
    return request('DELETE', url, data);
};

let put = (url, data) => {
    return request('PUT', url, data);
};
export default {
    loginPhone(msg) {
        return post(api.LOGINPHONE, msg)
    },
    loginWx(msg) {
        return post(api.LOGIONWX, msg)
    },
    getUser() {
        return get(util.template(api.USERINFO));
    },
    getDataDict(params) {
        return get(api.GETDATADICT, params)
    },
    getKingKongConfig() {
        return get(api.GETKINGKONGCONFIG)
    },
    getMainPageInfo(params) {
        return get(api.GETMAINPAGEINFO, params)
    },
    getPreSale(params) {
        return get(api.GETPRESALE, params)
    },
    getOpeningNotice(params) {
        return get(api.GETOPENINGNOTICE, params)
    },
    getRegistering(params) {
        return get(api.GETREGISTERING, params)
    },
    get24hNews(params) {
        return get(api.GET24HNEWS, params)
    },
    getUserConsultantList(data) {
        return get(api.USERCONSULTANTLIST, data);
    },
    getEvaluationForwardList(data) {
        return get(api.EVALUATIONFORWARDLIST, data)
    },
    postSaveDemand(data) {
        return post(api.SAVEDEMAND, data);
    },
    getConsultantCommentList(data) {
        return get(api.CONSULTANTCOMMENTLIST, data)
    },
    getEvaluationCommentList(data) {
        return get(api.EVALUATIONCOMMENTLIST, data)
    },
    getNewComerGuideConfig() {
        return get(api.GETNEWCOMERGUIDECONFIG)
    },
    getCarouselList() {
        return get(api.GETCAEOUSELLIST)
    },
    getWebsiteConfig() {
        return get(api.GETWEBSITECONFIG)
    },
    getconsultantcomment(data) {
        return get(api.GETCONSULTANTCOMMENT, data)
    },
    getevaluationcollectsize() {
        return get(util.template(api.GETEVALUATIONCOLLECTSIZE));
    },
    getevaluationcollectlist(data) {
        return get(api.GETEVALUATIONCOLLECTLIST, data)
    },
    deletedemand(data) {
        return get(api.GETDELETEDEMAND, data)
    },
    getdemandlist(data) {
        return get(api.GETDEMANDLIST, data)
    },
    getdemandsize() {
        return get(util.template(api.GETDEMANDSIZE));
    },
    postsaveconsultant(data) {
        return post(api.SAVECONSULTANT, data);
    },
    getmineinfo() {
        return get(util.template(api.GETMINEINFO))
    },
    postsaveconsultantcomment(data) {
        return post(api.SAVECONSULTANTCOMMENT, data);
    },
    getEvaluationComment(params) {
        return get(api.GETEVALUATIONCOMMENT, params)
    },
    getEvaluationInfo(params) {
        return get(api.GETEVALUATIONINFO, params)
    },
    saveEvaluationCollect(data) {
        return post(api.SAVEEVALUATIONCOLLECT, data)
    },
    saveEvaluationLike(data) {
        return post(api.SAVEEVALUATIONLIKE, data)
    },
    saveEvaluationForward(data) {
        return post(api.SAVEEVALUATIONFORWARD, data)
    },
    saveEvaluationComment(data) {
        return post(api.SAVEEVALUATIONCOMMENT, data)
    },
    saveEvaluationCommentLike(data) {
        return post(api.SAVEEVALUATIONCOMMETLIKE, data)
    },
    getconsultantinfo(data) {
        return get(api.GETCONSULTANTINFO, data)
    },
    getreleaseconfig() {
        return get(api.GETRELEASECONFIG)
    },
    getBuddyList() {
        return get(api.GETBUDDYLIST)
    },
    getRecommendList(params) {
        return get(api.GETRECOMMENDLIST, params)
    },
    getQuickReplyConfig(params) {
        return get(api.GETQUICKREPLYCONFIG, params)
    },
    getconsultantuser() {
        return get(api.GETCONSULTANTUSER)
    },
    saveConsultantComment(data) {
        return post(api.SAVECONSULTANTCOMMENT, data)
    },
    getHomeConsultantList(params) {
        return get(api.GETHOMECONSULTANTLIST, params)
    },
    getChatCard(params) {
        return get(api.GETCHATCARD, params)
    },
    getAiConsultant(params) {
        return get(api.GETAICONSULTANT, params)
    },
    saveAIBuddy(data) {
        return get(api.SAVEAIBUDDY, data)
    },
    saveHomeBuddy(data) {
        return get(api.SAVEHOMEBUDDY, data)
    },
    getbuddybyconsultant(data) {
        return get(api.GETBUDDYBYCONSULTANT, data);
    },
    sendcaptcha(data) {
        return get(api.SENDCAPTCHA, data)
    },
    checkcaptcha(data) {
        return get(api.CHECKCAPTCHA, data)
    },
    updateuserinfo(data) {
        return post(api.UPDATEUSERINFO, data);
    },
    getsinglepagebycode(data) {
        return get(api.GETSINGLEPAGEBYCODE, data)
    },
    refreshToken(param) {
        return get(api.REFESHTOKEN, param, false)
    }
}
