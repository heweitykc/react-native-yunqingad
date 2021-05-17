import React from 'react';
import {
    Platform,
    DeviceEventEmitter,
    NativeModules,
    NativeEventEmitter,
    requireNativeComponent
} from 'react-native';

const { GDTManager, GDT_RewardVideo } = NativeModules

const listenerCache = {}

//subscribe 类型
const EVENT_TYPE = {
    onAdError: 'onAdError',
    onAdClick: 'onAdClick',
    onAdClose: 'onAdClose',
    onAdSkip: 'onAdSkip',
    onAdShow: 'onAdShow'
}

//初始化
const initGDT = (options) => {
    GDTManager.init({"appid" : options['appid']})    
}

//加载激励视频
const loadGDTRewardVideo = (options) => {
    const eventEmitter = new NativeEventEmitter(GDT_RewardVideo);
    let result = GDT_RewardVideo.initAd({"adid":options["adid"]});
    GDT_RewardVideo.loadAd({"customdata":options["customdata"], "uid" : options["uid"]});

    return {
        result,
        subscribe: (type, callback) => {
            if (listenerCache[type]) {
                listenerCache[type].remove()
            }
            return (listenerCache[type] = eventEmitter.addListener('GDT_RewardVideo-' + type, (event) => {
                callback && callback(event)
            }))
        }
    }
}

//展示激励视频
const showGDTRewardVideo = (options) => {
    GDT_RewardVideo.showAd(options)
}

export {
    initGDT,
    loadGDTRewardVideo,
    showGDTRewardVideo
}