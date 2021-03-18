
# react-native-yunqingad

## Getting started

`$ npm install react-native-yunqingad --save`

### Mostly automatic installation

`$ react-native link react-native-yunqingad`

### Manual installation


#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-yunqingad` and add `RNYunqingad.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNYunqingad.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.reactlibrary.RNYunqingadPackage;` to the imports at the top of the file
  - Add `new RNYunqingadPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-yunqingad'
  	project(':react-native-yunqingad').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-yunqingad/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-yunqingad')
  	```

#### Windows
[Read it! :D](https://github.com/ReactWindows/react-native)

1. In Visual Studio add the `RNYunqingad.sln` in `node_modules/react-native-yunqingad/windows/RNYunqingad.sln` folder to their solution, reference from their app.
2. Open up your `MainPage.cs` app
  - Add `using Yunqingad.RNYunqingad;` to the usings at the top of the file
  - Add `new RNYunqingadPackage()` to the `List<IReactPackage>` returned by the `Packages` method


## Usage
```javascript
import RNYunqingad from 'react-native-yunqingad';

// TODO: What to do with the module?
RNYunqingad;
```
  