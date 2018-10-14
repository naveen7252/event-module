---

---

#  事件总线模块设计

[TOC]

## 一、模块架构

### 1.1 模块概述

> 事件总线模块是专门用来接收及通知处理模块事件的功能性模块，管理着所有模块事件，并且提供事件的创建、订阅、接收、发送等功能，是模块间的事件中转站。

> [^注：]: 1>事件主题的创建者才拥有事件的删除权利。2>事件的订阅者与发送者是否需要做权限控制？这部分待定，不能删除，这个跟业务系统不太一样，所有topic都需要预先注册?

### 1.2 架构图

![event-bus-module](image/event-bus-module.png)

- 事件总线模块主要含2部分交互逻辑：

  - 与系统核心模块的微服务注册与服务信息获取。

  - 与其他基础模块间的事件消息创建、订阅、转发管理。

### 1.3 模块上下文

![event-bus-content](image/event-bus-content.png)


### 1.4 实现原理图

![event-bus-model](image/event-bus-model.png)

## 二、功能设计

### 2.1 功能架构图

![event-bus-function](image/event-bus-function.png)

- 微服务接口信息同步管理

  用于与kernel服务治理模块之间的服务接口同步

- 事件储存管理

  -    用于进行事件信息的创建，订阅等储存，并且在模块重启是进行信息的初始化。

- 事件订阅管理

  - 维护订阅事件的“配置表”：包括所有各个模块订阅的重要参数

- 事件转发管理

    开放接口用于事件的接收，对接收事件按订阅进行转发，转发调用接口通过服务信息管理接口获得 。

- 功能接口管理

    -   开放查询接口供外部查询


### 2.2 核心流程

* 事件处理时序

![event-bus-seq-flow](image/event-bus-seq-flow.png)  

- 事件处理基本流程

![event-bus-main-flow](image/event-bus-main-flow.png)

### 2.3 关键处理逻辑

	在事件转发失败（比如网络原因）情况下进行的异常逻辑处理 ，按以下2种逻辑处理:
	
	    1、保留事件调用 按队列重复调用，直到转发成功。
	
	    2、尝试多次后直接丢弃。

## 三、接口设计

### 3.1 模块接口

* 事件主题创建

  * cmd: addtopic

  * 参数说明：


  ```
  moduleId:""//发布模块Id
  moduleName:""//发布模块名称
  topicId:""	//主题id
  topicName:""	//主题名称
  ```

  * 返回值说明：

  ```
  result：100  //操作码
  message:""   //失败时的信息
  data:{
  	result：// true false
  }
  ```

* 事件主题删除

  - cmd: deltopic
  - 参数说明

  ```
  topicId :    //事件主题id
  ```
  - 返回值说明：


```
  result：100  //操作码
  message:""   //失败时的信息
  data:{
  	result：// true false
  }
```

* 事件主题订阅【自动创建】
  - cmd: subscribe
  - 参数说明
```
  topicId :    //事件主题id
  retryType :    //转发方式， 1：失败放弃转发  2:失败持续转发 
  serviceApi: //转发回调服务api接口
  moduleId: //订阅者模块id
```
        返回值说明:

```
  result：100  //操作码
  message:""   //失败时的信息
  data:{
  	result：// true false
  }
```

- 事件取消订阅

  - post: unsubscribe
  - 参数说明

  ```
  topicId :    //事件主题id
  moduledId:   //发送者模块id
  ```

  - 返回值说明：

```
  result：100  //操作码
  message:""   //失败时的信息
  data:{
  	result：// true false
  }
```




- 事件发送【自动创建】 在没人订阅情况下是否保留一定时间？

  - cmd: publish
  - 参数说明

  ```
  topicId :    //事件主题id
  moduledCode:   //发送者模块id
  type:1 无订阅者 丢弃， 2 无订阅者 保留，直到消费
  data： //待转发的数据信息
  ```

  - 返回值说明：

```
  result：100  //操作码
  message:""   //失败时的信息
  data:{
  	result：// true false
  }
```





### 3.2 功能接口

        功能接口是提供给界面和命令行工具使用的接口

- 获取事件主题信息

  - cmd: gettopic

  - 参数说明

    ```
    topicId:""    //事件主题id
    ```

  - 返回值说明

    ```
    result：100  //操作码
    message:""   //失败时的信息
    data:{
        topicId: ""   //主题id
       	topicName:""//主题名称
    	createTime:""	//创建时间
    	moduleName：""  //主题创建者（模块）名称
    	moduleId:""    //主题创建者（模块）Id
    	subscriberList:[//订阅者信息
            {
            moduleId:"" //订阅者
            subscriberTime:"" //订阅时间
            serviceApi: "" //回调API
            }
    	]
    	
    }
    ```

 

- 获取所有事件主题信息

  - cmd : topiclist

  - 参数说明

    ```
    page:""  起始页
    recordNum：""  单页记录数量 
    ```

  - 返回值说明

    ```
    result：100  //操作码
    message:""   //失败时的信息
    data:{
        list:[
        {
        topicId: ""   //主题id
       	topicName:""//主题名称
    	createTime:""	//创建时间
    	moduleName：""  //主题创建者（模块）名称
    	moduleId:""    //主题创建者（模块）Id
    	}，
    	{
        topicId: ""   //主题id
       	topicName:""//主题名称
    	createTime:""	//创建时间
    	moduleName：""  //主题创建者（模块）名称
    	moduleId:""    //主题创建者（模块）Id
    	}
       ]
    	
    }
    ```

 



## 四、事件说明

该模块是最底层模块，不发送和订阅任何事件

## 五、协议

### 5.1 网络通讯协议

无

### 5.2 交易协议

无

## 六 模块依赖关系

- 内核模块
  - 模块注册
  - 模块注销
  - 模块状态上报（心跳）

  - 服务接口数据获取及定时更新

## 七、模块配置项

```
server.ip:0.0.0.0   //本机ip，用于提供服务给其他模块,可以不填，默认自动获取
server.port:8080    //提供服务的端口,可以不填，默认自动获取
```

## 八、Java特有的设计

* 核心对象类定义

  待完善

* 存储数据结构

  待完善

## 九、SDK提供给其他模块使用
- 1 引入模块的依赖
- 2 注册订阅事件
- 3 发送事件
- 4 处理事件
- 5 取消注册订阅
- 6 网络连接检测

