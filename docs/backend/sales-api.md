## 销售管理 API

所有请求以 HTTP 状态码 200 表示请求成功，请求结果以 application/json 格式在 Response Body 中返回；状态码 400 表示请求失败，具体的错误消息以文本形式在 Response Body 中返回。

### ClientController

#### 获取客户信息（分页）

* URL

| 协议   | URL            | 方法   |
| :--- | :------------- | :--- |
| HTTP | /client/search | POST |

* 请求参数

| 名称         | 类型      | 描述             |
| :--------- | :------ | :------------- |
| current    | Integer | 当前页号           |
| limit      | Integer | 每页记录数          |
| sortColumn | String  | 排序字段           |
| sort       | String  | 排序方式（ASC、DESC） |
| searchKey  | String  | 搜索关键字          |

* 返回结果示例

```json
{
  "list": [
  {
    "clientId": 1,
    "clientName": "广东电子商",
    "contact": "张三",
    "contactPhone": "",
    "createAt": 1528519665000,
    "createBy": 1,
    "updateAt": 1528717822000,
    "updateBy": 1
  },...],
  "statistics": null,
  "total": 8
}
```
#### 获取全部客户列表

* URL

| 协议   | URL             | 方法   |
| :--- | :-------------- | :--- |
| HTTP | /client/getList | POST |

* 返回结果示例

```json
[{
  "clientId": 1,
  "clientName": "广东电子商",
  "contact": "张三",
  "contactPhone": "",
  "createAt": 1528519665000,
  "createBy": 1,
  "updateAt": 1528717822000,
  "updateBy": 1
},...]
```
#### 增加客户

* URL

| 协议   | URL         | 方法   |
| :--- | :---------- | :--- |
| HTTP | /client/add | POST |

* 请求参数

| 名称           | 类型     | 描述   |
| :----------- | :----- | :--- |
| clientName   | String | 客户名称 |
| contact      | String | 联系人  |
| contactPhone | String | 联系电话 |

* 返回结果示例

```json
{
  "clientId": 12,
  "clientName": "aaa",
  "contact": "",
  "contactPhone": "",
  "createAt": 1528720469000,
  "createBy": 1,
  "updateAt": 1528720469000,
  "updateBy": 1
}
```
#### 修改客户信息

* URL

| 协议   | URL            | 方法   |
| :--- | :------------- | :--- |
| HTTP | /client/update | POST |

* 请求参数

| 名称           | 类型      | 描述   |
| :----------- | :------ | :--- |
| clientName   | String  | 客户名称 |
| contact      | String  | 联系人  |
| contactPhone | String  | 联系电话 |
| clientId     | Integer | 客户id |

* 返回结果示例

```json
{
  "clientId": 12,
  "clientName": "aaa",
  "contact": "",
  "contactPhone": "",
  "createAt": 1528720469000,
  "createBy": 1,
  "updateAt": 1528720494000,
  "updateBy": 1
}
```
#### 删除客户

* URL

| 协议   | URL            | 方法   |
| :--- | :------------- | :--- |
| HTTP | /client/remove | POST |

* 请求参数

| 名称     | 类型     | 描述              |
| :----- | :----- | :-------------- |
| idList | String | 客户id列表（"11,12"） |

* 返回结果示例

```json
"success"
```
### OrderController

#### 获取订单列表（分页）

* URL

| 协议   | URL           | 方法   |
| :--- | :------------ | :--- |
| HTTP | /order/search | POST |

* 请求参数

| 名称         | 类型      | 描述         |
| :--------- | :------ | :--------- |
| current    | Integer | 当前页数       |
| limit      | Integer | 读取长度       |
| sortColumn | String  | 排序字段       |
| sort       | String  | 排序方式 升序 降序 |
| searchKey  | String  | 搜索关键字      |
| state      | Integer | 订单状态       |
| beginTime  | Long    | 开始时间       |
| endTime    | Long    | 结束时间       |

* 返回结果示例

```json
{
  "list": [
  {
    "billId": 15,
    "billNo": "OR180611202449048",
    "salesman": 1,
    "clientId": 1,
    "contact": "",
    "contactPhone": "",
    "billTime": 1528719889000,
    "billAmount": 580.00,
    "billState": 1,
    "remark": "",
    "auditAt": null,
    "auditBy": null,
    "produceAt": null,
    "produceBy": null,
    "deliveryAt": null,
    "deliveryBy": null,
    "createAt": 1528719889000,
    "createBy": 1,
    "updateAt": 1528719889000,
    "updateBy": 1,
    "salesName": "admin",
    "clientName": "广东电子商",
    "auditName": "",
    "produceName": "",
    "deliveryName": "",
    "productList": []
  },...],
  "statistics": null,
  "total": 15
}
```

#### 查询订单信息（单个）

* URL

| 协议   | URL            | 方法   |
| :--- | :------------- | :--- |
| HTTP | /order/getById | POST |

* 请求参数

| 名称     | 类型      | 描述   |
| :----- | :------ | :--- |
| billId | Integer | 订单id |

* 返回结果示例

```json
{
  "billId": 14,
  "billNo": "OR180609163957149",
  "salesman": 1,
  "clientId": 4,
  "contact": "",
  "contactPhone": "",
  "billTime": 1528533597000,
  "billAmount": 18000.00,
  "billState": 3,
  "remark": "",
  "auditAt": 1528533600000,
  "auditBy": 1,
  "produceAt": 1528533603000,
  "produceBy": 1,
  "deliveryAt": null,
  "deliveryBy": null,
  "createAt": 1528533597000,
  "createBy": 1,
  "updateAt": 1528533597000,
  "updateBy": 1,
  "salesName": "admin",
  "clientName": "湖北电子商",
  "auditName": "admin",
  "produceName": "admin",
  "deliveryName": "",
  "productList": [
  {
    "billProductId": 27,
    "billId": 14,
    "productId": 9,
    "quantity": 150,
    "price": 120.00,
    "total": 18000.00,
    "remark": "",
    "productNo": "A50101",
    "productName": "A50散热器（5-30V）"
  }]
}
```
#### 获取订单表的所有信息

* URL

| 协议   | URL            | 方法   |
| :--- | :------------- | :--- |
| HTTP | /order/getList | POST |

* 请求参数

| 名称              | 类型      | 描述              |
| :-------------- | :------ | :-------------- |
| state           | Integer | 订单状态            |
| onlyNotOutstock | Boolean | 是否只获取没有货品出库单的订单 |
| onlyNotDraw     | Boolean | 是否只获取没有领料单的订单   |

* 返回结果示例

```json
{
  auditAt: 1528526073000
  auditBy: 1
  auditName: ""
  billAmount: 24600
  billId: 1
  billNo: "OR180609143426966"
  billState: 4
  billTime: 1528526067000
  clientId: 1
  clientName: ""
  contact: ""
  contactPhone: ""
  createAt: 1528526067000
  createBy: 1
  deliveryAt: 1528532132000
  deliveryBy: 1
  deliveryName: ""
  produceAt: 1528526077000
  produceBy: 1
  produceName: ""
  productList: []
  remark: "月底前尽快发货"
  salesName: ""
  salesman: 1
  updateAt: 1528526067000
  updateBy: 1
},..]
```

#### 审核订单

* URL

| 协议   | URL          | 方法   |
| :--- | :----------- | :--- |
| HTTP | /order/audit | POST |

* 请求参数

| 名称     | 类型     | 描述     |
| :----- | :----- | :----- |
| idList | String | 订单id列表 |

* 返回结果示例

```json
"success"
```

#### 启动生产

* URL

| 协议   | URL            | 方法   |
| :--- | :------------- | :--- |
| HTTP | /order/produce | POST |

* 请求参数

| 名称     | 类型     | 描述   |
| :----- | :----- | :--- |
| billId | String | 订单id |

* 返回结果示例

```json
"success"
```
#### 反审核

* URL

| 协议   | URL            | 方法   |
| :--- | :------------- | :--- |
| HTTP | /order/unaudit | POST |

* 请求参数

| 名称     | 类型     | 描述     |
| :----- | :----- | :----- |
| idList | String | 订单id列表 |

* 返回结果示例

```json
"单据不是已审核状态"
```
#### 撤销订单

* URL

| 协议   | URL           | 方法   |
| :--- | :------------ | :--- |
| HTTP | /order/cancel | POST |

* 请求参数

| 名称     | 类型     | 描述   |
| :----- | :----- | :--- |
| billId | String | 订单id |

* 返回结果示例

```json
"success"
```
#### 增加订单

* URL

| 协议   | URL        | 方法   |
| :--- | :--------- | :--- |
| HTTP | /order/add | POST |

* 请求参数

| 名称           | 类型         | 描述   |
| :----------- | :--------- | :--- |
| clientId     | Integer    | 客户id |
| contact      | String     | 联系人  |
| contactPhone | String     | 联系方式 |
| billAmount   | BigDecimal | 金额   |
| remark       | String     | 备注   |
| productList  | String     | 商品信息 |

* 返回结果示例

```json
{
  "billId": 15,
  "billNo": "OR180611202449048",
  "salesman": 1,
  "clientId": 1,
  "contact": "",
  "contactPhone": "",
  "billTime": 1528719889000,
  "billAmount": 580.00,
  "billState": 1,
  "remark": "",
  "auditAt": null,
  "auditBy": null,
  "produceAt": null,
  "produceBy": null,
  "deliveryAt": null,
  "deliveryBy": null,
  "createAt": 1528719889000,
  "createBy": 1,
  "updateAt": 1528719889000,
  "updateBy": 1,
  "salesName": "admin",
  "clientName": "广东电子商",
  "auditName": "",
  "produceName": "",
  "deliveryName": "",
  "productList": [
  {
    "billProductId": 28,
    "billId": 15,
    "productId": 3,
    "quantity": 1,
    "price": 580.00,
    "total": 580.00,
    "remark": "",
    "productNo": "A50001",
    "productName": "A50电路板（5-30V）"
  }]
}
```
#### 修改订单

* URL

| 协议   | URL           | 方法   |
| :--- | :------------ | :--- |
| HTTP | /order/update | POST |

* 请求参数

| 名称           | 类型         | 描述   |
| :----------- | :--------- | :--- |
| billId       | Integer    | 订单id |
| contact      | String     | 联系人  |
| contactPhone | String     | 联系方式 |
| billAmount   | BigDecimal | 金额   |
| remark       | String     | 备注   |
| productList  | String     | 商品信息 |

* 返回结果示例

```json
{
  "billId": 8,
  "billNo": "OR180609162152383",
  "salesman": 1,
  "clientId": 4,
  "contact": "",
  "contactPhone": "",
  "billTime": 1528532512000,
  "billAmount": 11600.00,
  "billState": 1,
  "remark": "",
  "auditAt": null,
  "auditBy": null,
  "produceAt": null,
  "produceBy": null,
  "deliveryAt": null,
  "deliveryBy": null,
  "createAt": 1528532512000,
  "createBy": 1,
  "updateAt": 1528720206000,
  "updateBy": 1,
  "salesName": "admin",
  "clientName": "湖北电子商",
  "auditName": "",
  "produceName": "",
  "deliveryName": "",
  "productList": [
  {
    "billProductId": 29,
    "billId": 8,
    "productId": 4,
    "quantity": 20,
    "price": 580.00,
    "total": 11600.00,
    "remark": "",
    "productNo": "A50002",
    "productName": "A50电路板（10-30V）"
  }]
}
```
#### 删除订单

* URL

| 协议   | URL           | 方法   |
| :--- | :------------ | :--- |
| HTTP | /order/remove | POST |

* 请求参数

| 名称     | 类型     | 描述              |
| :----- | :----- | :-------------- |
| idList | String | 订单id列表（"11,12"） |

* 返回结果示例

```json
"success"
```
#### 订单物料分解

* URL

| 协议   | URL                        | 方法   |
| :--- | :------------------------- | :--- |
| HTTP | /order/getMaterialRequired | POST |

* 请求参数

| 名称     | 类型      | 描述   |
| :----- | :------ | :--- |
| billId | Integer | 订单id |

* 返回结果示例

```json
[{
  "materialNo": "0603DZ001",
  "materialName": "0603电阻（1千欧）",
  "quantity": 60,
  "productNo": "A50001",
  "productName": "A50电路板（5-30V）",
  "materialProperty": "",
  "productMaterialRemark": ""
},...]
```