import React from 'react';
import {
    Platform,
    DeviceEventEmitter,
    NativeModules,
    NativeEventEmitter,
    requireNativeComponent
} from 'react-native';

const { AdManager, SplashAd, RewardVideoAd } = NativeModules

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
const adInit = () => {
    AdManager.init({})
}

//开屏
const loadSplash = (options) => {
    const eventEmitter = new NativeEventEmitter(SplashAd)
    let result = SplashAd.loadSplashAd(options)

    return {
        result,
        subscribe: (type, callback) => {
            if (listenerCache[type]) {
                listenerCache[type].remove()
            }
            return (listenerCache[type] = eventEmitter.addListener('SplashAd-' + type, (event) => {
                callback && callback(event)
            }))
        }
    }
}

//开屏 - 半屏带logo
const loadSplash_half = (options) => {
    const eventEmitter = new NativeEventEmitter(SplashAd)
    let result = SplashAd.loadSplashAdHalf(options)

    return {
        result,
        subscribe: (type, callback) => {
            if (listenerCache[type]) {
                listenerCache[type].remove()
            }
            return (listenerCache[type] = eventEmitter.addListener('SplashAd-' + type, (event) => {
                callback && callback(event)
            }))
        }
    }
}

//激励视频
const loadRewardVideo = (options) => {
    const eventEmitter = new NativeEventEmitter(RewardVideoAd)
    let result = RewardVideoAd.loadRewardVideoAd(options)

    return {
        result,
        subscribe: (type, callback) => {
            if (listenerCache[type]) {
                listenerCache[type].remove()
            }
            return (listenerCache[type] = eventEmitter.addListener('RewardVideoAd-' + type, (event) => {
                callback && callback(event)
            }))
        }
    }
}

const Banner = requireNativeComponent('BannerAd')

const BannerAd = (props) => {
    const { mediaId, adWidth = 150, onAdLayout, onAdError, onAdClose, onAdClick } = props
    const [height, setHeight] = React.useState(0)
    return (
        <Banner
            mediaId={mediaId}
            adWidth={adWidth}
            style={{ width: adWidth, height }}
            onAdError={(e) => {
                onAdError && onAdError(e.nativeEvent)
            }}
            onAdClick={(e) => {
                onAdClick && onAdClick(e.nativeEvent)
            }}
            onAdClose={(e) => {
                onAdClose && onAdClose(e.nativeEvent)
            }}
            onAdLayout={(e) => {
                if (e.nativeEvent.height) {
                    setHeight(e.nativeEvent.height)
                    onAdLayout && onAdLayout(e.nativeEvent)
                }
            }}
        />
    )
}

export {
    adInit,
    loadSplash,
    loadSplash_half,
    loadRewardVideo,
    BannerAd
}