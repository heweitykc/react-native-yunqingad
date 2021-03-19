
package com.palmmob.yunqing_rn;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;

public class RNYunqingadModule extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;

  public RNYunqingadModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  public String getName() {
    return "RNYunqingad";
  }
}