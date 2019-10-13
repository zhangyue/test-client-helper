# Test Client Helper
Test Client Helper是一个针对测试客户端的测试框架辅助模块。它可以帮助测试客户端解决如下两个主要问题：
  1. 结构化地打印操作类型、输入参数、输出结果以及任何异常信息到日志中。
  2. 对负向测试（negative test）设置期望的错误、异常类型以及错误信息，避免不必要的try-catch和if-else污染测试方法中的测试步骤代码。

See README_en.md for English version.

## Maven配置：
```
<!-- http://artifactory.jd.com/libs-snapshots-local/com/jdcloud/test-tool/test-client-helper/ -->
<dependency>
    <groupId>com.jdcloud.test-tool</groupId>
    <artifactId>test-client-helper</artifactId>
    <version>$version</version>
</dependency>
```

## 使用方法: 
* 在测试代码中创建测试客户端包裹类，并继承TestClientHelper类。
* 在测试客户端包裹类中实现请求调用的包裹方法，在其中利用executeAndAssert()或
executeAndAssertWithResult()方法通过函数引用来调用客户端SDK的请求调用方法。与此
同时，构造适当的Banner和SdkMethodOutputHandler对象来处理测试日志。
* 在负向测试（negative test）中，通过在调用测试客户端的请求调用包裹方法前调用
withExpectedError()来指定期望的错误或异常。

## 例子：
* TestClientHelper的子类请参考：http://git.jd.com/cloud-storage/oss-test/blob/master/jss_test/src/main/java/com/jcloud/clienthelper/S3ClientHelper.java
* 相应的测试方法请参考：http://git.jd.com/cloud-storage/oss-test/tree/master/jss_test/src/main/java/com/jcloud/test/jss/mirrorstorage 

## TestCase类
* TestCase类是本module提供的另外一个测试辅助类。这个类是一个对org.testng.Assert
的一个很好的替代品。后者抛出AssertionError时会让测试代码没有机会对此错误进行日
志处理，导致在某些时候测试因出错而退出，但测试日志中看不到任何错误信息。