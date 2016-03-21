# PPDefaults <img src="https://travis-ci.org/RandomCodeOrg/PPDefaults.svg"></img> [![License](http://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

A project that provides some predefined implementations of PProcessor which can be executed by the <a href="https://github.com/RandomCodeOrg/PPPlugin">
PPPlugin</a>.

## Getting started

1. Include the PPPlugin that will execute the post processor classes. You may take a look at the readme of the [PPPlugin project](https://github.com/RandomCodeOrg/PPPlugin#ppplugin-) or [this code project article](http://www.codeproject.com/Articles/1063734/Automated-Logging-with-Maven).
2. Include the following dependency in your `pom.xml`:

 ```xml
<dependency>
      <groupId>com.github.randomcodeorg.ppplugin</groupId>
      <artifactId>ppdefaults</artifactId>
      <version>0.0.2</version>
  </dependency>
 ```
3. Create a class that inherits from a post processor you want to use 
