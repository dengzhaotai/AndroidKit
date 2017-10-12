# AndroidKit
封装Android基础类和常用工具类
##1.在Project的build.gradle上添加
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
##2.在模块的build.gradle上添加依赖
	dependencies {
	        compile 'com.github.dengzhaotai:AndroidKit:v1.2'  //注意v1.2版本使用最新的
	}
