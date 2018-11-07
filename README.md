# DatabaseToolsScripts
IDEA Database Tools 生成代码脚本

###脚本说明
>IntelliJ IDEA的Database Tools脚本， 可根据表信息自动生成代码， 基于自带的Generate POJOs.groovy脚本进行了修改， 支持自定义Velocity模板， 无需额外引入jar包。

####目录说明
>generate: 配置文件路径  
>hotel: 示例项目

##使用方法：
####1、放置脚本
>展开右侧DatabaseTools， 任意表上右键 -> Scripted Extensions -> Go to Scripts Directory.  
![Image text](images/1.png)  
>左侧Project视图会自动展开Database Tools and SQL， 右键Show Explorer, 将Generate Templates.groovy放到和Generate POJOs.groovy同一目录  
![Image text](images/2.png)

####2、编写配置文件
>工程根目录下创建目录generate, 创建config.json到改目录下，配置项可参考示例generate/config说明.json

####3、执行脚本
>展开右侧DatabaseTools， 任意表上右键 -> Scripted Extensions -> Generate Templates, 即可执行脚本， 将根据选中表生成对应DTO、VO等自定义代码。
![Image text](images/3.png)
  
  
  
#### 生成结果示例：
![Image text](images/DTO.png)
![Image text](images/VO.png)

###如果以上操作后，代码或者脚本没在Project视图中显示， 请刷新一下
![Image text](images/10.png)
