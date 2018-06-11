## 生产管理 API

所有请求以 HTTP 状态码 200 表示请求成功，请求结果以 application/json 格式在 Response Body 中返回；状态码 400 表示请求失败，具体的错误消息以文本形式在 Response Body 中返回。
### ProductionDrawController
#### 获得订单详细信息
* URL

| 协议   | URL        | 方法   |
| :--- | :--- | :--- |
| HTTP | /production-draw/billDetail | POST |

* 请求参数

| 名称         | 类型      | 描述              |
| :--------- | :------ | :-------------- |
| int billId  | Integer  | 领料单ID             |


* 返回结果示例

```json
{
  "auditAt":1528533621000,
  "auditBy":1,
  "auditName":"",
  "billId":8,
  "billNo":"PDM180609164015754",
  "billState":3,
  "billStateCn":"已完成",
  "billTime":1528533616000,
  "billTimeLocal":"2018-06-09 16:40:16",
  ...
  "materialList":[...],
  "relatedBill":14
}
```
#### 搜索订单
* URL

| 协议   | URL        | 方法   |
| :--- | :--- | :--- |
| HTTP | /production-draw/searchBill | POST |

* 请求参数

| 名称         | 类型      | 描述              |
| :--------- | :------ | :-------------- |
|current |Integer|当前位置|
|limit |Integer|一次读取长度|
|sortColumn|String|按照哪一列排序|
|sort  |String|排序方式 升序 降序|
|searchKey|String| 关键字查找|
|state|Integer| 订单状态|
|beginTime|Long| 订单开始时间|
|endTime|Long| 订单结束时间|


* 返回结果示例

```json
[
    {
        "auditAt":1528533621000,
        "auditBy":1,
        "auditName":"",
        "billId":8,
        "billNo":"PDM180609164015754",
        "billState":3,
        "billStateCn":"已完成",
        "billTime":1528533616000,
        "billTimeLocal":"2018-06-09 16:40:16",
        ...
        "materialList":[...],
        "relatedBill":14
    }
    {...}
]
```
#### 审核订单
* URL

| 协议   | URL        | 方法   |
| :--- | :--- | :--- |
| HTTP | /production-draw/auditBill | POST |

* 请求参数

| 名称         | 类型      | 描述              |
| :--------- | :------ | :-------------- |
|idList |String |审核订单列表|
```json
{
  "success"
}
```

* 异常情况

| 触发条件                | 返回信息   |
| :------------------ | :----- |
| 订单号已审核 | 单据不是待审核状态 |
#### 反审核订单
* URL

| 协议   | URL        | 方法   |
| :--- | :--- | :--- |
| HTTP | /production-draw/unauditBill | POST |

* 请求参数

| 名称         | 类型      | 描述              |
| :--------- | :------ | :-------------- |
|idList |String |反审核订单列表|
```json
{
  "success"
}
```

* 异常情况

| 触发条件                | 返回信息   |
| :------------------ | :----- |
| 订单号已审核 | 单据不是已审核状态 |
#### 添加订单
* URL

| 协议   | URL        | 方法   |
| :--- | :--- | :--- |
| HTTP | /production-draw/addBill | POST |

* 请求参数

| 名称         | 类型      | 描述              |
| :--------- | :------ | :-------------- |
|relatedBill|Integer|相关生产订单|
|remark|String|注释|
|materialList|Stirng|相关物料|
```json
{
    "auditAt":1528533621000,
    "auditBy":1,
    "auditName":"",
    "billId":8,
    "billNo":"PDM180609164015754",
    "billState":3,
    "billStateCn":"已完成",
    "billTime":1528533616000,
    "billTimeLocal":"2018-06-09 16:40:16",
    ...
    "materialList":[...],
    "relatedBill":14
}
```

* 异常情况

| 触发条件                | 返回信息   |
| :------------------ | :----- |
| 相关订单不存在 | 相关订单不存在 |
| 相关订单不是生产中状态 | 相关订单不是生产中状态 |
#### 更新订单
* URL

| 协议   | URL        | 方法   |
| :--- | :--- | :--- |
| HTTP | /production-draw/updateBill | POST |

* 请求参数

| 名称         | 类型      | 描述              |
| :--------- | :------ | :-------------- |
|billId|Integer|订单编号|
|relatedBill|Integer|相关订单|
|remark|String|注释|
|materialList|Stirng|相关物料|
```json
{
    "auditAt":1528533621000,
    "auditBy":1,
    "auditName":"",
    "billId":8,
    "billNo":"PDM180609164015754",
    "billState":3,
    "billStateCn":"已完成",
    "billTime":1528533616000,
    "billTimeLocal":"2018-06-09 16:40:16",
    ...
    "materialList":[...],
    "relatedBill":14
}
```

* 异常情况

| 触发条件                | 返回信息   |
| :------------------ | :----- |
| 订单已经审核不能更改 | 单据不是待审核状态 |
#### 删除订单
* URL

| 协议   | URL        | 方法   |
| :--- | :--- | :--- |
| HTTP | /production-draw/deleteBill | POST |

* 请求参数

| 名称         | 类型      | 描述              |
| :--------- | :------ | :-------------- |
|idList|String|相关删除订单List|
```json
{
  "success"
}
```

* 异常情况

| 触发条件                | 返回信息   |
| :------------------ | :----- |
| 相关订单已经审核不能更改 | 相关订单不是待审核状态 |
#### 获得相关生产订单物料
* URL

| 协议   | URL        | 方法   |
| :--- | :--- | :--- |
| HTTP | /production-draw/allOrderBillMaterial | POST |

* 请求参数

| 名称         | 类型      | 描述              |
| :--------- | :------ | :-------------- |
|relatedBillId|Integer|相关生产订单|
```json
[
    {
       "billId":5,
       "billMaterialId":34,
       "materialId":1,
       "materialName":"0603电阻（1千欧)",
       "materialNo":"0603DZ001",
       "quantity":120,
       "remark":""
    }
    {...}
]
  
```
### ProductionReturnController
#### 获得订单详细信息
* URL

| 协议   | URL        | 方法   |
| :--- | :--- | :--- |
| HTTP | /production-draw/billDetail | POST |

* 请求参数

| 名称         | 类型      | 描述              |
| :--------- | :------ | :-------------- |
| int billId  | Integer  | 领料单ID             |


* 返回结果示例

```json
{
  "auditAt":1528533621000,
  "auditBy":1,
  "auditName":"",
  "billId":8,
  "billNo":"PDM180609164015754",
  "billState":3,
  "billStateCn":"已完成",
  "billTime":1528533616000,
  "billTimeLocal":"2018-06-09 16:40:16",
  ...
  "materialList":[...],
  "relatedBill":14
}
```
#### 搜索订单
* URL

| 协议   | URL        | 方法   |
| :--- | :--- | :--- |
| HTTP | /production-draw/searchBill | POST |

* 请求参数

| 名称         | 类型      | 描述              |
| :--------- | :------ | :-------------- |
|current |Integer|当前位置|
|limit |Integer|一次读取长度|
|sortColumn|String|按照哪一列排序|
|sort  |String|排序方式 升序 降序|
|searchKey|String| 关键字查找|
|state|Integer| 订单状态|
|beginTime|Long| 订单开始时间|
|endTime|Long| 订单结束时间|


* 返回结果示例

```json
[
    {
        "auditAt":1528533621000,
        "auditBy":1,
        "auditName":"",
        "billId":8,
        "billNo":"PDM180609164015754",
        "billState":3,
        "billStateCn":"已完成",
        "billTime":1528533616000,
        "billTimeLocal":"2018-06-09 16:40:16",
        ...
        "materialList":[...],
        "relatedBill":14
    }
    {...}
]
```
#### 审核订单
* URL

| 协议   | URL        | 方法   |
| :--- | :--- | :--- |
| HTTP | /production-draw/auditBill | POST |

* 请求参数

| 名称         | 类型      | 描述              |
| :--------- | :------ | :-------------- |
|idList |String |审核订单列表|
```json
{
  "success"
}
```

* 异常情况

| 触发条件                | 返回信息   |
| :------------------ | :----- |
| 订单号已审核 | 单据不是待审核状态 |
#### 反审核订单
* URL

| 协议   | URL        | 方法   |
| :--- | :--- | :--- |
| HTTP | /production-draw/unauditBill | POST |

* 请求参数

| 名称         | 类型      | 描述              |
| :--------- | :------ | :-------------- |
|idList |String |反审核订单列表|
```json
{
  "success"
}
```

* 异常情况

| 触发条件                | 返回信息   |
| :------------------ | :----- |
| 订单号已审核 | 单据不是已审核状态 |
#### 添加订单
* URL

| 协议   | URL        | 方法   |
| :--- | :--- | :--- |
| HTTP | /production-draw/addBill | POST |

* 请求参数

| 名称         | 类型      | 描述              |
| :--------- | :------ | :-------------- |
|relatedBill|Integer|相关生产订单|
|remark|String|注释|
|materialList|Stirng|相关物料|
```json
{
  "auditAt":1528533621000,
  "auditBy":1,
  "auditName":"",
  "billId":8,
  "billNo":"PDM180609164015754",
  "billState":3,
  "billStateCn":"已完成",
  "billTime":1528533616000,
  "billTimeLocal":"2018-06-09 16:40:16",
  ...
  "materialList":[...],
  "relatedBill":14
}
```

* 异常情况

| 触发条件                | 返回信息   |
| :------------------ | :----- |
| 相关订单不存在 | 相关订单不存在 |
| 相关订单不是生产中状态 | 相关订单不是生产中状态 |
#### 更新订单
* URL

| 协议   | URL        | 方法   |
| :--- | :--- | :--- |
| HTTP | /production-draw/updateBill | POST |

* 请求参数

| 名称         | 类型      | 描述              |
| :--------- | :------ | :-------------- |
|billId|Integer|订单编号|
|relatedBill|Integer|相关订单|
|remark|String|注释|
|materialList|Stirng|相关物料|
```json
{
  "auditAt":1528533621000,
  "auditBy":1,
  "auditName":"",
  "billId":8,
  "billNo":"PDM180609164015754",
  "billState":3,
  "billStateCn":"已完成",
  "billTime":1528533616000,
  "billTimeLocal":"2018-06-09 16:40:16",
  ...
  "materialList":[...],
  "relatedBill":14
}
```

* 异常情况

| 触发条件                | 返回信息   |
| :------------------ | :----- |
| 订单已经审核不能更改 | 单据不是待审核状态 |
#### 删除订单
* URL

| 协议   | URL        | 方法   |
| :--- | :--- | :--- |
| HTTP | /production-draw/deleteBill | POST |

* 请求参数

| 名称         | 类型      | 描述              |
| :--------- | :------ | :-------------- |
|idList|String|相关删除订单List|
```json
{
  "success"
}
```

* 异常情况

| 触发条件                | 返回信息   |
| :------------------ | :----- |
| 相关订单已经审核不能更改 | 相关订单不是待审核状态 |
