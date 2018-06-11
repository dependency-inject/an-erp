## 物料出入库 API

所有请求以 HTTP 状态码 200 表示请求成功，请求结果以 application/json 格式在 Response Body 中返回；状态码 400 表示请求失败，具体的错误消息以文本形式在 Response Body 中返回。

### MaterialInstockController

#### 新增物料入库单

* URL

| 协议   | URL        | 方法   |
| :--- | :--------- | :--- |
| HTTP | /material-instock/add | POST |

* 请求参数

| 名称         | 类型      | 描述              |
| :--------- | :------ | :-------------- |
| fromPrincipal   | Integer | 交料人                        |
| materialSource  | Integer | 物料来源（1退料入库，2采购入库）|
| relatedBill     | Integer | 相关退料单ID（采购入库时为空）  |
| remark          | String  | 备注                          |
| materialList    | String  | 对应物料列表（用逗号分隔）      |

* 返回结果示例

```json
{
  "billId": 4,
  "billNo": "MI180610180404906",
  "fromPrincipal": 1,
  "warehousePrincipal": 1,
  "billTime": "Sun Jun 10 18:04:05 CST 2018",
  "materialSource": 1,
  "relatedBill": 2,
  "billState": 1,
  "remark": "退料",
  "auditAt": null,
  "auditBy": null,
  "finishAt": null,
  "finishBy": null,
  "createAt": "Sun Jun 10 18:04:05 CST 2018",
  "createBy": 1,
  "updateAt": "Sun Jun 10 18:04:05 CST 2018",
  "updateBy": 1
}
```

* 异常情况

| 触发条件                | 返回信息   |
| :------------------ | :----- |
| 与物料入库单关联退料单不存在   | 相关退料单不存在 |
| 与物料入库单关联退料单未经审核 | 相关退料单不是已审核状态 |

#### 更新物料入库单

* URL

| 协议   | URL                   | 方法   |
| :--- | :-------------------- | :--- |
| HTTP | /material-instock/update | POST |

* 请求参数

| 名称              | 类型     | 描述      |
| :-------------- | :----- | :------ |
| billId          | Integer| 物料入库单ID                    |
| fromPrincipal   | Integer| 交料人                         |
| materialSource  | Integer| 物料来源（1退料入库，2采购入库） |
| relatedBill     | Integer| 相关退料单ID（采购入库时为空）   |
| remark          | String | 备注                           |
| materialList    | String | 对应物料列表（用逗号分隔）       |

* 返回结果示例

```json
success
```

* 异常情况

| 触发条件                | 返回信息   |
| :------------------ | :----- |
| 物料入库单状态不为待审核状态 | 单据不是待审核状态 |

#### 查询物料入库单信息（分页）

* URL

| 协议   | URL            | 方法   |
| :--- | :------------- | :--- |
| HTTP | /material-instock/search | POST |

* 请求参数

| 名称      | 类型      | 描述   |
| :------ | :------ | :--- |
| current    | Integer | 当前页号                                  |
| limit      | Integer | 每页记录数                                |
| sortColumn | String  | 排序字段                                  |
| sort       | String  | 排序方式（ASC、DESC）                      |
| searchKey  | String  | 搜索关键字                                |
| state      | Integer | 物料入库单状态（1待审核，2已审核，3已完成）  |
| beginTime  | Long    | 订单创建最早时间                          |
| endTime    | Long    | 订单创建最晚时间                          |

* 返回结果示例

```json
{
  total: 2,
  list: [
    { "billId": 1, "billNo": "MI180609160042754", ... },
    { "billId": 3, "billNo": "MI180609164116799", ... }
  ]
}
```

#### 删除物料入库单（批量）

* URL

| 协议   | URL           | 方法   |
| :--- | :------------ | :--- |
| HTTP | /material-instock/remove | POST |

* 请求参数

| 名称     | 类型     | 描述            |
| :----- | :----- | :------------ |
| idList | String | 物料入库单ID列表（用逗号分隔） |

* 返回结果示例

```json
success
```

* 异常情况

| 触发条件                                     | 返回信息       |
| :--------------------------------------- | :--------- |
| 选中单据中存在不为待审核状态单据            | 单据不是待审核状态 |

#### 获取物料入库单信息（单个）

* URL

| 协议   | URL            | 方法   |
| :--- | :------------- | :--- |
| HTTP | /material-instock/getById | POST |

* 请求参数

| 名称      | 类型      | 描述   |
| :------ | :------ | :--- |
| billId  | Integer | 单据编号 |

* 返回结果示例

```json
{
  "billId": 4,
  "billNo": "MI180610180404906",
  "fromPrincipal": 1,
  "warehousePrincipal": 1,
  "billTime": "Sun Jun 10 18:04:05 CST 2018",
  "materialSource": 1,
  "relatedBill": 2,
  "billState": 1,
  "remark": "退料",
  "auditAt": null,
  "auditBy": null,
  "finishAt": null,
  "finishBy": null,
  "createAt": "Sun Jun 10 18:04:05 CST 2018",
  "createBy": 1,
  "updateAt": "Sun Jun 10 18:04:05 CST 2018",
  "updateBy": 1
}
```

#### 审核物料入库单（批量）

* URL

| 协议   | URL           | 方法   |
| :--- | :------------ | :--- |
| HTTP | /material-instock/audit | POST |

* 请求参数

| 名称     | 类型     | 描述            |
| :----- | :----- | :------------ |
| idList | String | 物料入库单ID列表（用逗号分隔） |

* 返回结果示例

```json
success
```

* 异常情况

| 触发条件                                     | 返回信息       |
| :--------------------------------------- | :--------- |
| 选中单据中存在不为待审核状态单据            | 单据不是待审核状态 |

#### 反审核物料入库单（批量）

* URL

| 协议   | URL           | 方法   |
| :--- | :------------ | :--- |
| HTTP | /material-instock/unaudit | POST |

* 请求参数

| 名称     | 类型     | 描述            |
| :----- | :----- | :------------ |
| idList | String | 物料入库单ID列表（用逗号分隔） |

* 返回结果示例

```json
success
```

* 异常情况

| 触发条件                                     | 返回信息       |
| :--------------------------------------- | :--------- |
| 选中单据中存在不为已审核状态单据            | 单据不是已审核状态 |

#### 完成物料入库单（单个）

* URL

| 协议   | URL           | 方法   |
| :--- | :------------ | :--- |
| HTTP | /material-instock/finish | POST |

* 请求参数

| 名称     | 类型     | 描述            |
| :----- | :----- | :------------ |
| billId | Integer | 物料入库单ID |

* 返回结果示例

```json
success
```

* 异常情况

| 触发条件                                     | 返回信息       |
| :--------------------------------------- | :--------- |
| 物料入库单状态不为已审核状态               | 单据不是已审核状态 |

### MaterialOutstockController

#### 新增物料出库单

* URL

| 协议   | URL        | 方法   |
| :--- | :--------- | :--- |
| HTTP | /material-outstock/add | POST |

* 请求参数

| 名称         | 类型      | 描述              |
| :--------- | :------ | :-------------- |
| toPrincipal          | Integer | 领料人                        |
| materialWhereabouts  | Integer | 物料去向（1领料出库）          |
| relatedBill          | Integer | 相关领料单ID                  |
| remark               | String  | 备注                          |
| materialList         | String  | 对应物料列表（用逗号分隔）      |

* 返回结果示例

```json
{
  "billId": 3,
  "billNo": "MO180609162553860",
  "toPrincipal": 1,
  "warehousePrincipal": 1,
  "billTime": "Sat Jun 09 16:25:54 CST 2018",
  "materialWhereabouts": 1,
  "relatedBill": 3,
  "billState": 1,
  "remark": "",
  "auditAt": Sat Jun 09 16:25:58 CST 2018,
  "auditBy": 1,
  "finishAt": null,
  "finishBy": null,
  "createAt": "Sat Jun 09 16:25:54 CST 2018",
  "createBy": 1,
  "updateAt": "Sat Jun 09 16:25:54 CST 2018",
  "updateBy": 1
}
```

* 异常情况

| 触发条件                | 返回信息   |
| :------------------ | :----- |
| 与物料出库单关联领料单不存在          | 相关领料单不存在        |
| 与物料出库单关联领料单未经审核        | 相关领料单不是已审核状态 |
| 单据中所选物料存在有库存数量不足的物料 | 物料库存数量不足        |

#### 更新物料出库单

* URL

| 协议   | URL                   | 方法   |
| :--- | :-------------------- | :--- |
| HTTP | /material-outstock/update | POST |

* 请求参数

| 名称              | 类型     | 描述      |
| :-------------- | :----- | :------ |
| billId               | Integer| 物料出库单ID                    |
| toPrincipal          | Integer| 领料人                         |
| materialWhereabouts  | Integer| 物料去向（1领料出库）           |
| relatedBill          | Integer| 相关领料单ID                   |
| remark               | String | 备注                           |
| materialList         | String | 对应物料列表（用逗号分隔）       |

* 返回结果示例

```json
success
```

* 异常情况

| 触发条件                | 返回信息   |
| :------------------ | :----- |
| 物料出库单状态不为待审核状态          | 单据不是待审核状态 |
| 单据中所选物料存在有库存数量不足的物料 | 物料库存数量不足  |

#### 查询物料出库单信息（分页）

* URL

| 协议   | URL            | 方法   |
| :--- | :------------- | :--- |
| HTTP | /material-outstock/search | POST |

* 请求参数

| 名称      | 类型      | 描述   |
| :------ | :------ | :--- |
| current    | Integer | 当前页号                                 |
| limit      | Integer | 每页记录数                               |
| sortColumn | String  | 排序字段                                 |
| sort       | String  | 排序方式（ASC、DESC）                     |
| searchKey  | String  | 搜索关键字                               |
| state      | Integer | 物料出库单状态（1待审核，2已审核，3已完成） |
| beginTime  | Long    | 单据创建最早时间                          |
| endTime    | Long    | 单据创建最晚时间                          |

* 返回结果示例

```json
{
  total: 2,
  list: [
    { "billId": 2, "billNo": "MO180609160728560", ... },
    { "billId": 4, "billNo": "MO180609162607687", ... }，
    { "billId": 7, "billNo": "MO180609164207072", ... }
  ]
}
```

#### 删除物料出库单（批量）

* URL

| 协议   | URL           | 方法   |
| :--- | :------------ | :--- |
| HTTP | /material-outstock/remove | POST |

* 请求参数

| 名称     | 类型     | 描述            |
| :----- | :----- | :------------ |
| idList | String | 物料出库单ID列表（用逗号分隔） |

* 返回结果示例

```json
success
```

* 异常情况

| 触发条件                                     | 返回信息       |
| :--------------------------------------- | :--------- |
| 选中单据中存在不为待审核状态单据            | 单据不是待审核状态 |

#### 获取物料出库单信息（单个）

* URL

| 协议   | URL            | 方法   |
| :--- | :------------- | :--- |
| HTTP | /material-outstock/getById | POST |

* 请求参数

| 名称      | 类型      | 描述   |
| :------ | :------ | :--- |
| billId  | Integer | 单据编号 |

* 返回结果示例

```json
{
  "billId": 3,
  "billNo": "MO180609162553860",
  "toPrincipal": 1,
  "warehousePrincipal": 1,
  "billTime": "Sat Jun 09 16:25:54 CST 2018",
  "materialWhereabouts": 1,
  "relatedBill": 3,
  "billState": 2,
  "remark": "",
  "auditAt": Sat Jun 09 16:25:58 CST 2018,
  "auditBy": 1,
  "finishAt": null,
  "finishBy": null,
  "createAt": "Sat Jun 09 16:25:54 CST 2018",
  "createBy": 1,
  "updateAt": "Sat Jun 09 16:25:54 CST 2018",
  "updateBy": 1
}
```

#### 审核物料出库单（批量）

* URL

| 协议   | URL           | 方法   |
| :--- | :------------ | :--- |
| HTTP | /material-outstock/audit | POST |

* 请求参数

| 名称     | 类型     | 描述            |
| :----- | :----- | :------------ |
| idList | String | 物料出库单ID列表（用逗号分隔） |

* 返回结果示例

```json
success
```

* 异常情况

| 触发条件                                     | 返回信息       |
| :--------------------------------------- | :--------- |
| 选中单据中存在不为待审核状态单据            | 单据不是待审核状态 |

#### 反审核物料出库单（批量）

* URL

| 协议   | URL           | 方法   |
| :--- | :------------ | :--- |
| HTTP | /material-outstock/unaudit | POST |

* 请求参数

| 名称     | 类型     | 描述            |
| :----- | :----- | :------------ |
| idList | String | 物料出库单ID列表（用逗号分隔） |

* 返回结果示例

```json
success
```

* 异常情况

| 触发条件                                     | 返回信息       |
| :--------------------------------------- | :--------- |
| 选中单据中存在不为已审核状态单据            | 单据不是已审核状态 |

#### 完成物料出库单（单个）

* URL

| 协议   | URL           | 方法   |
| :--- | :------------ | :--- |
| HTTP | /material-outstock/finish | POST |

* 请求参数

| 名称     | 类型     | 描述            |
| :----- | :----- | :------------ |
| billId | Integer | 物料出库单ID |

* 返回结果示例

```json
success
```

* 异常情况

| 触发条件                                     | 返回信息       |
| :--------------------------------------- | :--------- |
| 物料出库单状态不为已审核状态               | 单据不是已审核状态 |
