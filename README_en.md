# Test Client Helper
Test Client Helper is a helper module for your test client with wo major responsibilities:
  1. Helps you log your test operations along with the input parameters, output 
results and any exceptions in a structural way.
  2. Setting expected error or exception for negative tests and avoid polluting
  the test steps code by unnecessary try-catch and if-else blocks in test methods.

See README.md for Chinese version.

## Maven:
```
<!-- http://artifactory.jd.com/libs-snapshots-local/com/jdcloud/test-tool/test-client-helper/ -->
<dependency>
    <groupId>com.jdcloud.test-tool</groupId>
    <artifactId>test-client-helper</artifactId>
    <version>$version</version>
</dependency>
```

## Usage: 
* Simply let your test client wrapper classes extend the TestClientHelper class.
* Implement your test client wrapper methods in your test client wrapper 
classes. In these test client wrapper methods, call the test client SDK methods' 
method references with the executeAndAssert() or executeAndAssertWithResult() 
methods. In the meanwhile, construct the Banner and SdkMethodOutputHandler
objects, which will take care of your logging.
* In your negative test methods, call the withExpectedError() method before 
calling your wrapper methods.

## Samples:
* See http://git.jd.com/cloud-storage/oss-test/blob/master/jss_test/src/main/java/com/jcloud/clienthelper/S3ClientHelper.java
for TestClientHelper's sub-class
* See http://git.jd.com/cloud-storage/oss-test/tree/master/jss_test/src/main/java/com/jcloud/test/jss/mirrorstorage 
for test methods.

## The TestCase class
* There is also a test assistant class - TestCase. This class is a good 
alternative to org.testng.Assert, which throws AssertionError without leaving
a moment for you to write necessary error or information into log.