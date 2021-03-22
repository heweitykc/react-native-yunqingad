import {
    NativeModules,
    requireNativeComponent
} from 'react-native';

const { AdManager, SplashAd, RewardVideoAd } = NativeModules

const Banner = requireNativeComponent('BannerAd')

export {
    AdManager,
    SplashAd,
    RewardVideoAd,
    Banner
}