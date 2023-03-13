const IS_MOCK = false
const mockHost = 'http://192.168.1.57:8080/home-service'
const realHost = 'https://www.029aijia.cn/home-service'
const imgHost = 'https://www.029aijia.cn'
const socketHost = 'wss://www.029aijia.cn'

const host = IS_MOCK ? mockHost : realHost;
const imageHost = IS_MOCK ? '' : imgHost;

const config = {
    host, imageHost, mockHost, IS_MOCK, socketHost
};

export default config;
