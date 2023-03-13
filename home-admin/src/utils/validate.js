/**
 * 邮箱
 * @param {*} s
 */
export function isEmail (s) {
  return /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((.[a-zA-Z0-9_-]{2,3}){1,2})$/.test(s)
}

/**
 * 手机号码
 * @param {*} s
 */
export function isMobile (s) {
  return /^1[0-9]{10}$/.test(s)
}

/**
 * 电话号码
 * @param {*} s
 */
export function isPhone (s) {
  return /^([0-9]{3,4}-)?[0-9]{7,8}$/.test(s)
}
/**
 * 手机座机
 * @param {*} s
 */
export function phoneNum (s) {
  return /^((0\d{2,3}-\d{7,8})|(1[3456789]\d{9}))$/.test(s)
}

/**
 * URL地址
 * @param {*} s
 */
export function isURL (s) {
  return /^http[s]?:\/\/.*/.test(s)
}

/**
 * URL地址
 * @param {*} s
 */
export function isIdcard (cid) {
  let arrExp = [7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2]
  let arrValid = ['1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2']
  if (/^\d{17}\d|x$/i.test(cid)) {
    let sum = 0
    let idx
    for (let i = 0; i < cid.length - 1; i++) {
      sum += parseInt(cid.substr(i, 1), 10) * arrExp[i]
    }
    idx = sum % 11
    return arrValid[idx] === cid.substr(17, 1).toUpperCase()
  } else {
    return false
  }
}
