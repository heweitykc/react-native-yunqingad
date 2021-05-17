import {
    NativeModules,
    requireNativeComponent
} from 'react-native';

const { AdManager, SplashAd, RewardVideoAd,GDTManager,GDT_RewardVideo } = NativeModules

const Banner = requireNativeComponent('BannerAd')

export {
    AdManager,
    SplashAd,
    RewardVideoAd,
    Banner,
    GDTManager, 
    GDT_RewardVideo
}