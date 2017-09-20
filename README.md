# RecyclerAdapter
RecyclerView.Adapter的头、尾、空
> 基于这个项目的修改[baseAdapter](https://github.com/hongyangAndroid/baseAdapter/blob/master/baseadapter-recyclerview/src/main/java/com/zhy/adapter/recyclerview/wrapper/HeaderAndFooterWrapper.java)，我只要简单的头尾空的功能。
> 原文的头尾和空，好像不能一起使用，且空布局的响应较慢。所以写了这个库。

### 使用方法
> 用AdapterWrapper包住真正的Adapater就可以了，并且尽量不要操作AdapterWrapper。
> RecyclerView.setAdapter(new AdapterWrapper(adapter));
```
AdapterWrapper#addHeader(View);
AdapterWrapper#addFooter(View);
AdapterWrapper#setEmptyView(View);//默认有emptyView,空布局会把头和尾一起遮住，先这样吧。
```
###  使用方法[MainActivity.class](https://github.com/xuanu/RecyclerAdapter/blob/master/app/src/main/java/apk/cn/zeffect/recycleradapter/MainActivity.kt)
Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}Copy
Step 2. Add the dependency

	dependencies {
	        compile 'com.github.xuanu:RecyclerAdapter:1.0'
	}
Copy
Share this release:

#### 截图
![image](https://github.com/xuanu/RecyclerAdapter/raw/master/screenshots/2.gif)