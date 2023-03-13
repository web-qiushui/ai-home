import Mock from '../mock'
import config from '../../common/config'

import dictRes from './dict'
import kingkong from './kingkong'
import guide from './guide'
import pageInfo from './info'
import carousel from './carousel'
import websiteconfig from './websiteConfig'
import news from './24hNews'
import evaluationInfo from './evaluationInfo'
import evaluationComments from './comments'

const result = {
    "code": 200,
    "data": {},
    "msg": "success",
    "desc": null
}

Mock.mock(config.mockHost + '/home-service/sys/datadict/anon/getDatadict', dictRes)
Mock.mock(config.mockHost + '/home-service/sys/config/anon/getKingKongConfig', kingkong)
Mock.mock(config.mockHost + '/home-service/sys/config/anon/getNewcomerGuideConfig', guide)
Mock.mock(config.mockHost + '/home-service/sys/config/anon/getWebsiteConfig', websiteconfig)
Mock.mock(config.mockHost + '/home-service/app/api/getCarouselList', carousel)
Mock.mock(config.mockHost + '/home-service/app/api/getMainPageInfo', pageInfo)
Mock.mock(config.mockHost + '/home-service/app/api/getPreSale', news)
Mock.mock(config.mockHost + '/home-service/app/api/get24hNews', news)
Mock.mock(config.mockHost + '/home-service/app/api/getRegistering', news)
Mock.mock(config.mockHost + '/home-service/app/api/getEvaluationInfo', evaluationInfo)
Mock.mock(config.mockHost + '/home-service/app/api/getEvaluationComment', evaluationComments)
Mock.mock(config.mockHost + '/home-service/app/api/saveEvaluationLike', result)
Mock.mock(config.mockHost + '/home-service/app/api/saveEvaluationForward', result)
Mock.mock(config.mockHost + '/home-service/app/api/saveEvaluationCollect', result)
Mock.mock(config.mockHost + '/home-service/app/api/saveEvaluationComment', result)
Mock.mock(config.mockHost + '/home-service/app/api/saveEvaluationCommentLike', result)
export {}