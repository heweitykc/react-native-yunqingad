/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow strict-local
 */

import React from 'react';
import type {Node} from 'react';
import {
  SafeAreaView,
  ScrollView,
  StatusBar,
  StyleSheet,
  Text,
  useColorScheme,
  View,
  TouchableHighlight
} from 'react-native';

import {
  Colors,
  DebugInstructions,
  Header,
  LearnMoreLinks,
  ReloadInstructions,
} from 'react-native/Libraries/NewAppScreen';

import {adInit, loadRewardVideo, BannerAd, loadSplash_half} from './ADMgr';

adInit();

showad = () => {
  const rewardVideo = loadRewardVideo({
    "adid" : "dev_android_gdt_stimulatevideo"
  })
  console.log('loadRewardVideo');
  rewardVideo.subscribe('onAdLoaded', (e) => {
      console.log('onAdLoaded')
    }
  );
  rewardVideo.subscribe('onAdClose', (e) => {
      console.log('onAdClose')
    }
  );  
  rewardVideo.subscribe('onVideoReward', (e) => {
      console.log('onVideoReward')
    }
  ); 
  rewardVideo.subscribe('onVideoComplete', (e) => {
      console.log('onVideoComplete')
    }
  ); 
  rewardVideo.subscribe('onAdClick', (e) => {
      console.log('onAdClick')
    }
  ); 
  rewardVideo.subscribe('onSkipVideo', (e) => {
      console.log('onSkipVideo')
    }
  );
  rewardVideo.subscribe('onAdError', (e) => {
      console.log('onAdError')
    }
  );  
}

showsplash = () => {  
  const splash = loadSplash_half({
    "adid" : "beta_android_splash",
    "countdown" : 5
  })
  splash.subscribe('onAdLoaded', (e) => {
    console.log(e)
  })
  splash.subscribe('onAdClose', (e) => {
    console.log(e)
  })  
  splash.subscribe('onAdClick', (e) => {
    console.log(e)
  })  
  splash.subscribe('onSkip', (e) => {
    console.log(e)
  })   
  splash.subscribe('onAdError', (e) => {
    console.log(e)
  })       
}

const App: () => Node = () => {
  const backgroundStyle = {
    backgroundColor:  Colors.lighter,
  };

  return (
    <SafeAreaView style={backgroundStyle}>
      <StatusBar />
      <ScrollView
        contentInsetAdjustmentBehavior="automatic"
        style={backgroundStyle}>
        <Header />
        <View
          style={{
            backgroundColor:  Colors.white,
          }}>
        </View>
        <TouchableHighlight style={styles.login_phone} underlayColor='transparent' activeOpacity={0.95} onPress={showad}>
            <Text style={styles.login_phone_t}>{'打开悬赏>'}</Text>
        </TouchableHighlight>                         
        <BannerAd
              adWidth={314}
              adHeight={50}
              mediaId={'beta_android_banner'} 
          />     
          <View style={{height:100}} ></View>
      <TouchableHighlight style={styles.login_phone} underlayColor='transparent' activeOpacity={0.95} onPress={showsplash}>
            <Text style={styles.login_phone_t}>{'打开启动屏>'}</Text>
        </TouchableHighlight>                

        
      </ScrollView>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  login_phone_t : {
    fontSize:30
  },
  sectionContainer: {
    marginTop: 32,
    paddingHorizontal: 24,
  },
  sectionTitle: {
    fontSize: 24,
    fontWeight: '600',
  },
  sectionDescription: {
    marginTop: 8,
    fontSize: 18,
    fontWeight: '400',
  },
  highlight: {
    fontWeight: '700',
  },
});

export default App;
