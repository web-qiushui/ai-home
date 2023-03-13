function friendlyDate(timestamp) {
    var formats = {
        'year': '%n% 年前',
        'month': '%n% 月前',
        'day': '%n% 天前',
        'hour': '%n% 小时前',
        'minute': '%n% 分钟前',
        'second': '%n% 秒前',
    };

    var now = Date.now();
    var seconds = Math.floor((now - timestamp) / 1000);
    var minutes = Math.floor(seconds / 60);
    var hours = Math.floor(minutes / 60);
    var days = Math.floor(hours / 24);
    var months = Math.floor(days / 30);
    var years = Math.floor(months / 12);

    var diffType = '';
    var diffValue = 0;
    if (years > 0) {
        diffType = 'year';
        diffValue = years;
    } else {
        if (months > 0) {
            diffType = 'month';
            diffValue = months;
        } else {
            if (days > 0) {
                diffType = 'day';
                diffValue = days;
            } else {
                if (hours > 0) {
                    diffType = 'hour';
                    diffValue = hours;
                } else {
                    if (minutes > 0) {
                        diffType = 'minute';
                        diffValue = minutes;
                    } else {
                        diffType = 'second';
                        diffValue = seconds === 0 ? (seconds = 1) : seconds;
                    }
                }
            }
        }
    }
    return formats[diffType].replace('%n%', diffValue);
}
let defaultFormat = 'yyyy-MM-dd HH:mm:ss';

function _date(date) {
    return {
        'y+': date.getFullYear(),
        'M+': date.getMonth() + 1,
        'd+': date.getDate(),
        'h+': date.getHours() % 12 === 0 ? 12 : date.getHours() % 12,
        'H+': date.getHours(),
        'm+': date.getMinutes(),
        's+': date.getSeconds(),
        'q+': Math.floor((date.getMonth() + 3) / 3),
        'S+': date.getMilliseconds()
    };
}
let format = (date, fmt) => {
    fmt = fmt || defaultFormat;
    if (typeof date === 'string') {
        date = date.replace(/-/g, '/');
    }
    date = new Date(date);
    let o = _date(new Date(date));
    let week = {
        '0': '/u65e5',
        '1': '/u4e00',
        '2': '/u4e8c',
        '3': '/u4e09',
        '4': '/u56db',
        '5': '/u4e94',
        '6': '/u516d'
    };
    if (/(y+)/.test(fmt)) {
        fmt = fmt.replace(RegExp.$1, (date.getFullYear() + '').substr(4 - RegExp.$1.length));
    }
    if (/(E+)/.test(fmt)) {
        fmt = fmt.replace(RegExp.$1, ((RegExp.$1.length > 1) ? (RegExp.$1.length > 2 ? '/u661f/u671f' : '/u5468') : '') + week[date.getDay() + '']);
    }
    for (let k in o) {
        if (new RegExp('(' + k + ')').test(fmt)) {
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? (o[k]) : (('00' + o[k]).substr(('' + o[k]).length)));
        }
    }
    return fmt;
};
let toast = (message) => {
    uni.showToast({icon: 'none', title: message + '', duration: 1500});
};
let showLoading = (title) => {
    uni.showLoading({mask: true, title: title});
};

let isMobile = (str) => {
    return /^1(3[0-9]|4[57]|5[0-35-9]|6[0-9]|7[0135678]|8[0-9])\d{8}$/g.test(str);
};
let isIdcard = (cid) => {
    let arrExp = [7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2];
    let arrValid = ['1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'];
    if (/^\d{17}\d|x$/i.test(cid)) {
        let sum = 0, idx;
        for (let i = 0; i < cid.length - 1; i++) {
            sum += parseInt(cid.substr(i, 1), 10) * arrExp[i];
        }
        idx = sum % 11;
        return arrValid[idx] === cid.substr(17, 1).toUpperCase();
    } else {
        return false;
    }
};
let regName = (name) => {
    return /^[\u4e00-\u9fa5]{2,4}$/.test(name);
}
let hideLoading = () => {
    uni.hideLoading();
};
let template = (str, options) => {
    return str.replace(/\{\w+\}/g, function ($0) {
        return options[$0.replace(/\{|\}/g, '')];
    });
};
export default {
    friendlyDate,
    toast: toast,
    isMobile: isMobile,
    showLoading: showLoading,
    hideLoading: hideLoading,
    template: template,
    format: format,
    isIdcard: isIdcard,
    regName: regName
}
