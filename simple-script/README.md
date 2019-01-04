## 使用方法：
#### 1、放置脚本
参考根目录的README.md

#### 2、放置模板
将xx.vm放到项目根路径下

#### 3、执行脚本
参考根目录的README.md


### 如何切换模板: 
找到脚本中的代码:  
``template = ve.getTemplate("POJOTemplate.vm", templateEncoding)``  
可将POJOTemplate.vm改为其他模板, 如ControllerTemplate.vm, ReqVOTemplate.vm等

### 如何修改目标文件名或后缀: 
找到脚本中的代码:  
``def fileName = className + ".java"``  
可以在此加前后缀, 改后缀名