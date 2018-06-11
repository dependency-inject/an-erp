## 货品出入库 API
所有请求以 HTTP 状态码 200 表示请求成功，请求结果以 application/json 格式在 Response Body 中返回；状态码 400 表示请求失败，具体的错误消息以文本形式在 Response Body 中返回。

## ProductInstockController

#### 新增产品入库单

- URL

| 协议 | URL                   | 方法 |
| ---- | --------------------- | ---- |
| HTTP | /product-instock /add | POST |

- 请求参数

| 名称          | 类型    | 描述                  |
| ------------- | ------- | --------------------- |
| fromPrincipal | Integer | 交货人                |
| productSource | Integer | 货品来源（1生产入库） |
| remark        | String  | 备注                  |
| productList   | String  | 入库货品列表          |

- 返回结果示例

```java
{
  "billId": 5,
  "billNo": "PI806102216322374",
  "fromPrincipal": 2,
  "warehousePrincipal": 1,
  "billTime": "Sun Jun 10 22;16;32 CST 2018",
  "productSource": 1,
  "createAt": "Sun Jun 10 22;16;32 CST 2018",
  "creatBy": 1
}
```



#### 修改货品入库单

- URL

| 协议 | URL                       | 方法 |
| ---- | ------------------------- | ---- |
| HTTP | /product-instock /getById | POST |

- 请求参数

| 名称          | 类型    | 描述                  |
| ------------- | ------- | --------------------- |
| billId        | Integer | 货品入库单ID          |
| fromPrincipal | Integer | 交货人                |
| productSource | Integer | 货品来源（1生产入库） |
| remark        | String  | 备注                  |
| productList   | String  | 入库货品列表          |

- 返回结果示例

```java
{
  "billId": 5,
  "billNo": "PI806102216322374",
  "fromPrincipal": 2,
  "warehousePrincipal": 1,
  "billTime": "Sun Jun 10 22;16;32 CST 2018",
  "productSource": 1,
  "createAt": "Sun Jun 10 22;16;32 CST 2018",
  "creatBy": 1,
  ...
}
```

- 异常情况

| 触发条件                                 | 返回信息       |
| :--------------------------------------- | -------------- |
| 用户为系统默认用户信息（sys_default为1） | 入库单不可修改 |



#### 查询货品入库单信息（分页）

- URL

| 协议 | URL                      | 方法 |
| ---- | ------------------------ | ---- |
| HTTP | /product-instock /search | POST |

- 请求参数

| 名称       | 类型    | 描述                |
| ---------- | ------- | ------------------- |
| current    | Integer | 当前页号            |
| limit      | Integer | 每页记录数          |
| sortColumn | String  | 排序字段            |
| sort       | String  | 排序方式(ASC, DESC) |
| searchKey  | String  | 搜索关键字          |
| state      | Integer | 入库单状态          |
| beginTime  | Long    | 开始时间            |
| endTime    | Long    | 结束时间            |

- 返回结果示例

```java
[
    {"billId": 1,"billNo": "PI806102216322374",...}
    {"billId": 2,"billNo": "PI806102216322398",...}
]
```



#### 查询货品入库单信息（单个）

- URL

| 协议 | URL                        | 方法 |
| ---- | -------------------------- | ---- |
| HTTP | /product-instock  /getById | POST |

- 请求参数

| 名称   | 类型    | 描述         |
| ------ | ------- | ------------ |
| billId | Integer | 货品入库单ID |

- 返回结果示例

```java
{
  "billId": 5,
  "billNo": "PI806102216322374",
  "fromPrincipal": 2,
  "warehousePrincipal": 1,
  "billTime": "Sun Jun 10 22;16;32 CST 2018",
  "productSource": 1,
  "createAt": "Sun Jun 10 22;16;32 CST 2018",
  "creatBy": 1,
  ...
}
```



#### 删除货品入库单

- URL

| 协议 | URL                     | 方法 |
| ---- | ----------------------- | ---- |
| HTTP | /product-instock/remove | POST |

- 请求参数

| 名称   | 类型   | 描述             |
| ------ | ------ | ---------------- |
| idList | string | 货品入库单ID列表 |

- 返回结果示例

```java
"success"
```

- 异常情况

| 触发条件                       | 返回信息       |
| ------------------------------ | -------------- |
| 入库单不是待审核状态，不能删除 | 入库单不可删除 |



#### 审核货品入库单

- URL

| 协议 | URL                    | 方法 |
| ---- | ---------------------- | ---- |
| HTTP | /product-instock/audit | POST |

- 请求参数

| 名称   | 类型   | 描述             |
| ------ | ------ | ---------------- |
| idList | string | 货品入库单ID列表 |

- 返回结果示例

```java
"success"
```

- 异常情况

| 触发条件                           | 返回信息       |
| ---------------------------------- | -------------- |
| 入库单不是待审核状态，不能完成审核 | 入库单不可审核 |



#### 反审核货品入库单

- URL

| 协议 | URL                      | 方法 |
| ---- | ------------------------ | ---- |
| HTTP | /product-instock/unaudit | POST |

- 请求参数

| 名称   | 类型   | 描述             |
| ------ | ------ | ---------------- |
| idList | string | 货品入库单ID列表 |

- 返回结果示例

```java
"success"
```

- 异常情况

| 触发条件                             | 返回信息         |
| ------------------------------------ | ---------------- |
| 入库单不是已审核状态，不能进行反审核 | 入库单不可反审核 |



#### 完成货品入库单

- URL

| 协议 | URL                      | 方法 |
| ---- | ------------------------ | ---- |
| HTTP | /product-instock/unaudit | POST |

- 请求参数

| 名称   | 类型    | 描述         |
| ------ | ------- | ------------ |
| billId | Integer | 货品入库单ID |

- 返回结果示例

```java
"success"
```

- 异常情况

| 触发条件                       | 返回信息       |
| ------------------------------ | -------------- |
| 入库单不是已审核状态，不能完成 | 入库单不可完成 |



## **ProductOutstockController**

#### 新增产品出库单

- URL

| 协议 | URL                    | 方法 |
| ---- | ---------------------- | ---- |
| HTTP | /product-outstock /add | POST |

- 请求参数

| 名称          | 类型    | 描述                  |
| ------------- | ------- | --------------------- |
| toPrincipal   | Integer | 领货人                |
| productSource | Integer | 货品去向（1发货出库） |
| remark        | String  | 备注                  |
| productList   | String  | 出库货品列表          |

- 返回结果示例

```java
{
  "billId": 5,
  "billNo": "PI806102216322374",
  "toPrincipal": 2,
  "warehousePrincipal": 1,
  "billTime": "Sun Jun 10 22;16;32 CST 2018",
  "productSource": 1,
  "createAt": "Sun Jun 10 22;16;32 CST 2018",
  "creatBy": 1
}
```

- 异常情况

| 触发条件                     | 返回信息                 |
| :--------------------------- | ------------------------ |
| 货品库存不足无法新建出库单   | 出库单因货品不足无法新增 |
| 出库单相关订单不存在         | 出库单无法新增           |
| 出库单相关订单不在生产中状态 | 出库单无法新增           |



#### 修改货品出库单

- URL

| 协议 | URL                        | 方法 |
| ---- | -------------------------- | ---- |
| HTTP | /product-outstock /getById | POST |

- 请求参数

| 名称          | 类型    | 描述                  |
| ------------- | ------- | --------------------- |
| billId        | Integer | 货品出库单ID          |
| toPrincipal   | Integer | 领货人                |
| productSource | Integer | 货品去向（1发货出库） |
| remark        | String  | 备注                  |
| productList   | String  | 出库货品列表          |

- 返回结果示例

```java
{
  "billId": 5,
  "billNo": "PI806102216322374",
  "toPrincipal": 2,
  "warehousePrincipal": 1,
  "billTime": "Sun Jun 10 22;16;32 CST 2018",
  "productSource": 1,
  "createAt": "Sun Jun 10 22;16;32 CST 2018",
  "creatBy": 1,
  ...
}
```

- 异常情况

| 触发条件                       | 返回信息                 |
| :----------------------------- | ------------------------ |
| 出库单不是待审核状态，不能修改 | 出库单不可修改           |
| 货品库存不足无法修改出库单     | 出库单因货品不足无法修改 |



#### 查询货品出库单信息（分页）

- URL

| 协议 | URL                       | 方法 |
| ---- | ------------------------- | ---- |
| HTTP | /product-outstock /search | POST |

- 请求参数

| 名称       | 类型    | 描述                |
| ---------- | ------- | ------------------- |
| current    | Integer | 当前页号            |
| limit      | Integer | 每页记录数          |
| sortColumn | String  | 排序字段            |
| sort       | String  | 排序方式(ASC, DESC) |
| searchKey  | String  | 搜索关键字          |
| state      | Integer | 出库单状态          |
| beginTime  | Long    | 开始时间            |
| endTime    | Long    | 结束时间            |

- 返回结果示例

```java
[
    {"billId": 1,"billNo": "PI806102216322374",...}
    {"billId": 2,"billNo": "PI806102216322398",...}
]
```



#### 查询货品出库单信息（单个）

- URL

| 协议 | URL                         | 方法 |
| ---- | --------------------------- | ---- |
| HTTP | /product-outstock  /getById | POST |

- 请求参数

| 名称   | 类型    | 描述         |
| ------ | ------- | ------------ |
| billId | Integer | 货品出库单ID |

- 返回结果示例

```java
{
  "billId": 5,
  "billNo": "PI806102216322374",
  "toPrincipal": 2,
  "warehousePrincipal": 1,
  "billTime": "Sun Jun 10 22;16;32 CST 2018",
  "productSource": 1,
  "createAt": "Sun Jun 10 22;16;32 CST 2018",
  "creatBy": 1,
  ...
}
```



#### 删除货品出库单

- URL

| 协议 | URL                      | 方法 |
| ---- | ------------------------ | ---- |
| HTTP | /product-outstock/remove | POST |

- 请求参数

| 名称   | 类型   | 描述             |
| ------ | ------ | ---------------- |
| idList | string | 货品出库单ID列表 |

- 返回结果示例

```java
"success"
```

- 异常情况

| 触发条件                       | 返回信息       |
| ------------------------------ | -------------- |
| 出库单不是待审核状态，不能删除 | 出库单不可删除 |



#### 审核货品出库单

- URL

| 协议 | URL                     | 方法 |
| ---- | ----------------------- | ---- |
| HTTP | /product-outstock/audit | POST |

- 请求参数

| 名称   | 类型   | 描述             |
| ------ | ------ | ---------------- |
| idList | string | 货品出库单ID列表 |

- 返回结果示例

```java
"success"
```

- 异常情况

| 触发条件                           | 返回信息       |
| ---------------------------------- | -------------- |
| 出库单不是待审核状态，不能完成审核 | 出库单不可审核 |



#### 反审核货品出库单

- URL

| 协议 | URL                       | 方法 |
| ---- | ------------------------- | ---- |
| HTTP | /product-outstock/unaudit | POST |

- 请求参数

| 名称   | 类型   | 描述             |
| ------ | ------ | ---------------- |
| idList | string | 货品出库单ID列表 |

- 返回结果示例

```java
"success"
```

- 异常情况

| 触发条件                             | 返回信息         |
| ------------------------------------ | ---------------- |
| 出库单不是已审核状态，不能进行反审核 | 出库单不可反审核 |



#### 完成货品出库单

- URL

| 协议 | URL                       | 方法 |
| ---- | ------------------------- | ---- |
| HTTP | /product-outstock/unaudit | POST |

- 请求参数

| 名称   | 类型    | 描述         |
| ------ | ------- | ------------ |
| billId | Integer | 货品出库单ID |

- 返回结果示例

```java
"success"
```

- 异常情况

| 触发条件                       | 返回信息       |
| ------------------------------ | -------------- |
| 出库单不是已审核状态，不能完成 | 出库单不可完成 |