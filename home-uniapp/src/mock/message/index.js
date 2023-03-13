import Mock from '../mock'
import config from '../../common/config'

import buddy from './buddy'
import card from './card'

const result = {
    "code": 200,
    "data": {},
    "msg": "success",
    "desc": null
}

Mock.mock(config.mockHost + '/home-service/app/api/getBuddyList', buddy)
Mock.mock(config.mockHost + '/home-service/app/api/getChatCard', card)
export { }