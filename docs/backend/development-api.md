## 研发辅助管理 API

所有请求以 HTTP 状态码 200 表示请求成功，请求结果以 application/json 格式在 Response Body 中返回；状态码 400 表示请求失败，具体的错误消息以文本形式在 Response Body 中返回。

### DevelopmentDrawController

#### 获取研发领料信息（单个）

* URL

| 协议   | URL        | 方法   |
| :--- | :--------- | :--- |
| HTTP | /development-draw/billDetail | POST |

* 请求参数

| 名称         | 类型      | 描述              |
| :--------- | :------ | :-------------- |
| billId  | int  | 研发领料单id号             |

* 返回结果示例

```json
{
"auditAt": null,
"auditBy": null,
"auditName": "",
"billId": 7,
"billNo": "DDM180609162811055",
"billState": 1,
"billStateCn": "待审核",
"billTime": 1528532891000,
"billTimeLocal": "2018-06-09 16:28:11",
"createAt": 1528532891000,
"createBy": 1,
"drawReason": 2,
"drawReasonCn": "研发领料",
"finishAt": null,
"finishBy": null,
"finishName": "",
"materialList": [{
"billId": 7,
"billMaterialId": 28,
"materialId": 10,
"materialName": "4274电源模块（5V）",
"materialNo": "4274DY001",
"quantity": 1,
"remark": ""
}],
"relatedBill": null,
"remark": "",
"toPrincipal": 1,
"toPrincipalName": "admin",
"updateAt": 1528532891000,
"updateBy": 1,
"warehousePrincipal": null,
"warehousePrincipalName": ""
}
```

* 异常情况

| 触发条件                | 返回信息   |
| :------------------ | :----- |
| 领料单是生产领料单不是研发领料单 | null |

#### 查询研发领料单

* URL

| 协议   | URL                   | 方法   |
| :--- | :-------------------- | :--- |
| HTTP | /development-draw/searchBill | POST |

* 请求参数

| 名称              | 类型     | 描述      |
| :-------------- | :----- | :------ |
| current     | Integer | 当前位置     |
| limit     | Integer | 一次读取长度     |
| sortColumn | String | 按哪一列排序 |
| sort | String | 排序方式 升序 降序 |
| searchKey | String | 关键字查找 |
| state | Integer | 订单状态 |
| beginTime | Long | 开始时间 |
| endTime | Long | 结束时间 |

* 返回结果示例

```json
[{
"auditAt": null,
"auditBy": null,
"auditName": "",
"billId": 7,
"billNo": "DDM180609162811055",
"billState": 1,
"billStateCn": "待审核",
"billTime": 1528532891000,
"billTimeLocal": "2018-06-09 16:28:11",
"createAt": 1528532891000,
"createBy": 1,
"detailPermission": true,
"drawReason": 2,
"finishAt": null,
"finishBy": null,
"finishName": "",
materialList:[{
"length": 0
}],
"relatedBill": null,
"remark": "",
"removePermission": true,
"toPrincipal": 1,
"toPrincipalName": "admin",
"updateAt": 1528532891000,
"updateBy": 1,
"warehousePrincipal": null,
"warehousePrincipalName": ""
}]
```

#### 审核研发领料单

* URL

| 协议   | URL            | 方法   |
| :--- | :------------- | :--- |
| HTTP | /development-draw/auditBill | POST |

* 请求参数

| 名称      | 类型      | 描述   |
| :------ | :------ | :--- |
| idList | String | 审核的研发领料单id列表|

* 返回结果示例

```json
success
```

#### 反审核研发领料单

* URL

| 协议   | URL            | 方法   |
| :--- | :------------- | :--- |
| HTTP | /development-draw/unauditBill | POST |

* 请求参数

| 名称     | 类型      | 描述                  |
| :----- | :------ | :------------------ |
| idList | String | 反审核的研发领料单id列表 |

* 返回结果示例

```json
success
```

#### 添加研发领料单

* URL

| 协议   | URL           | 方法   |
| :--- | :------------ | :--- |
| HTTP | /development-draw/addBill | POST |

* 请求参数

| 名称     | 类型     | 描述            |
| :----- | :----- | :------------ |
| remark | String | 该研发领料单相关备注 |
| materialList | String | 该研发领料单相关物料列表 |

* 返回结果示例

```json
{
"auditAt": null,
"auditBy": null,
"auditName": "",
"billId": 9,
"billNo": "DDM180610011113644",
"billState": 1,
"billTime": 1528564274000,
"createAt": 1528564274000,
"createBy": 1,
"drawReason": 2,
"finishAt": null,
"finishBy": null,
"finishName": "",
"materialList":[{
"billId": 9,
"billMaterialId": 32,
"materialId": 1,
"materialName": "0603电阻（1千欧）",
"materialNo": "0603DZ001",
"quantity": 1,
"remark": "hahaha",
}],
"relatedBill": null,
"remark": "xixixi",
"toPrincipal": 1,
"toPrincipalName": "admin",
"updateAt": 1528564274000,
"updateBy": 1,
"warehousePrincipal": null,
"warehousePrincipalName": ""
}
```

#### 更新研发领料单（单个）

* URL

| 协议   | URL           | 方法   |
| :--- | :------------ | :--- |
| HTTP | /development-draw/updateBill | POST |

* 请求参数

| 名称         | 类型      | 描述                  |
| :--------- | :------ | :------------------ |
| billId    | Integer | 研发领料单id号                |
| remark      | String | 备注               |
| materialList | String  | 物料列表                |

* 返回结果示例

```json
{
"auditAt": null,
"auditBy": null,
"auditName": "",
"billId": 9,
"billNo": "DDM180610011113644",
"billState": 1,
"billTime": 1528564274000,
"createAt": 1528564274000,
"createBy": 1,
"drawReason": 2,
"finishAt": null,
"finishBy": null,
"finishName": "",
"materialList":[{
"billId": 9,
"billMaterialId": 32,
"materialId": 1,
"materialName": "0603电阻（1千欧）",
"materialNo": "0603DZ001",
"quantity": 1,
"remark": "hahaha",
}],
"relatedBill": null,
"remark": "改了备注",
"toPrincipal": 1,
"toPrincipalName": "admin",
"updateAt": 1528564760000,
"updateBy": 1,
"warehousePrincipal": null,
"warehousePrincipalName": ""
}
```

#### 删除研发领料单（单个或多个）

* URL

| 协议   | URL           | 方法   |
| :--- | :------------ | :--- |
| HTTP | /development-draw/deleteBill  | POST |

* 请求参数

| 名称         | 类型      | 描述              |
| :--------- | :------ | :-------------- |
| idList    | String | 删除的研发领料单ID列表            |

* 返回结果示例

```json
success
```

### DevelopmentReturnController

#### 获取研发退料信息（单个）

* URL

| 协议   | URL        | 方法   |
| :--- | :--------- | :--- |
| HTTP | /development-return/billDetail | POST |

* 请求参数

| 名称         | 类型      | 描述              |
| :--------- | :------ | :-------------- |
| billId  | int  | 研发退料单id号             |

* 返回结果示例

```json
{
"auditAt": null,
"auditBy": null,
"auditName": "",
"billId": 3,
"billNo": "DDM180610102411977",
"billState": 1,
"billStateCn": "待审核",
"billTime": 1528597452000,
"billTimeLocal": "2018-06-10 10:24:12",
"createAt": 1528597452000,
"createBy": 1,
"finishAt": null,
"finishBy": null,
"finishName": "",
"fromPrincipal": 1,
"fromPrincipalName": "admin",
"materialList": [{
"billId": 3,
"billMaterialId": 28,
"materialId": 10,
"materialName": "4274电源模块（5V）",
"materialNo": "4274DY001",
"quantity": 1,
"remark": ""
}],
"relatedBill": null,
"remark": "grege",
"returnReason": 2,
"returnReasonCn": "研发退料",
"updateAt": 1528597452000,
"updateBy": 1,
"warehousePrincipal": null,
"warehousePrincipalName": ""
}
```

* 异常情况

| 触发条件                | 返回信息   |
| :------------------ | :----- |
| 退料单是生产退料单不是研发退料单 | null |

#### 查询研发退料单

* URL

| 协议   | URL                   | 方法   |
| :--- | :-------------------- | :--- |
| HTTP | /development-return/searchBill | POST |

* 请求参数

| 名称              | 类型     | 描述      |
| :-------------- | :----- | :------ |
| current     | Integer | 当前位置     |
| limit     | Integer | 一次读取长度     |
| sortColumn | String | 按哪一列排序 |
| sort | String | 排序方式 升序 降序 |
| searchKey | String | 关键字查找 |
| state | Integer | 订单状态 |
| beginTime | Long | 开始时间 |
| endTime | Long | 结束时间 |

* 返回结果示例

```json
[{
"auditAt": null,
"auditBy": null,
"auditName": "",
"billId": 3,
"billNo": "DDM180610102411977",
"billState": 1,
"billStateCn": "待审核",
"billTime": 1528597452000,
"billTimeLocal": "2018-06-10 10:24:12",
"createAt": 1528597452000,
"createBy": 1,
"detailPermission": true,
"returnReason": 2,
"finishAt": null,
"finishBy": null,
"finishName": "",
materialList:[{
"length": 0
}],
"relatedBill": null,
"remark": "",
"removePermission": true,
"fromPrincipal": 1,
"fromPrincipalName": "admin",
"updateAt": 1528597452000,
"updateBy": 1,
"warehousePrincipal": null,
"warehousePrincipalName": ""
}]
```

#### 审核研发退料单

* URL

| 协议   | URL            | 方法   |
| :--- | :------------- | :--- |
| HTTP | /development-return/auditBill | POST |

* 请求参数

| 名称      | 类型      | 描述   |
| :------ | :------ | :--- |
| idList | String | 审核的研发退料单id列表|

* 返回结果示例

```json
success
```

#### 反审核研发退料单

* URL

| 协议   | URL            | 方法   |
| :--- | :------------- | :--- |
| HTTP |  /development-return/unauditBill | POST |

* 请求参数

| 名称     | 类型      | 描述                  |
| :----- | :------ | :------------------ |
| idList | String | 反审核的研发退料单id列表 |

* 返回结果示例

```json
success
```

#### 添加研发退料单

* URL

| 协议   | URL           | 方法   |
| :--- | :------------ | :--- |
| HTTP |  /development-return/addBill | POST |

* 请求参数

| 名称     | 类型     | 描述            |
| :----- | :----- | :------------ |
| remark | String | 该研发退料单相关备注 |
| materialList | String | 该研发退料单相关物料列表 |

* 返回结果示例

```json
{
"auditAt": null,
"auditBy": null,
"auditName": "",
"billId": 3,
"billNo": "DDM180610102411977",
"billState": 1,
"billTime": 1528597452000,
"createAt": 1528597452000,
"createBy": 1,
"returnReason": 2,
"finishAt": null,
"finishBy": null,
"finishName": "",
"materialList":[{
"billId": 3,
"billMaterialId": 32,
"materialId": 1,
"materialName": "0603电阻（1千欧）",
"materialNo": "0603DZ001",
"quantity": 1,
"remark": "hahaha",
}],
"relatedBill": null,
"remark": "xixixi",
"fromPrincipal": 1,
"fromPrincipalName": "admin",
"updateAt": 1528597452000,
"updateBy": 1,
"warehousePrincipal": null,
"warehousePrincipalName": ""
}
```

#### 更新研发退料单（单个）

* URL

| 协议   | URL           | 方法   |
| :--- | :------------ | :--- |
| HTTP | /development-return/updateBill | POST |

* 请求参数

| 名称         | 类型      | 描述                  |
| :--------- | :------ | :------------------ |
| billId    | Integer | 研发退料单id号                |
| remark      | String | 备注               |
| materialList | String  | 物料列表                |

* 返回结果示例

```json
{
"auditAt": null,
"auditBy": null,
"auditName": "",
"billId": 3,
"billNo": "DDM180610102411977",
"billState": 1,
"billTime": 1528597452000,
"createAt": 1528597452000,
"createBy": 1,
"returnReason": 2,
"finishAt": null,
"finishBy": null,
"finishName": "",
"materialList":[{
"billId": 3,
"billMaterialId": 32,
"materialId": 1,
"materialName": "0603电阻（1千欧）",
"materialNo": "0603DZ001",
"quantity": 1,
"remark": "hahaha",
}],
"relatedBill": null,
"remark": "改了备注",
"fromPrincipal": 1,
"fromPrincipalName": "admin",
"updateAt": 1528597460000,
"updateBy": 1,
"warehousePrincipal": null,
"warehousePrincipalName": ""
}
```

#### 删除研发退料单（单个或多个）

* URL

| 协议   | URL           | 方法   |
| :--- | :------------ | :--- |
| HTTP | /development-return/deleteBill  | POST |

* 请求参数

| 名称         | 类型      | 描述              |
| :--------- | :------ | :-------------- |
| idList    | String | 删除的研发退料单ID列表            |

* 返回结果示例

```json
success
```## 研发辅助管理 API

所有请求以 HTTP 状态码 200 表示请求成功，请求结果以 application/json 格式在 Response Body 中返回；状态码 400 表示请求失败，具体的错误消息以文本形式在 Response Body 中返回。

### DevelopmentDrawController

#### 获取研发领料信息（单个）

* URL

| 协议   | URL        | 方法   |
| :--- | :--------- | :--- |
| HTTP | /development-draw/billDetail | POST |

* 请求参数

| 名称         | 类型      | 描述              |
| :--------- | :------ | :-------------- |
| billId  | int  | 研发领料单id号             |

* 返回结果示例

```json
{
"auditAt": null,
"auditBy": null,
"auditName": "",
"billId": 7,
"billNo": "DDM180609162811055",
"billState": 1,
"billStateCn": "待审核",
"billTime": 1528532891000,
"billTimeLocal": "2018-06-09 16:28:11",
"createAt": 1528532891000,
"createBy": 1,
"drawReason": 2,
"drawReasonCn": "研发领料",
"finishAt": null,
"finishBy": null,
"finishName": "",
"materialList": [{
"billId": 7,
"billMaterialId": 28,
"materialId": 10,
"materialName": "4274电源模块（5V）",
"materialNo": "4274DY001",
"quantity": 1,
"remark": ""
}],
"relatedBill": null,
"remark": "",
"toPrincipal": 1,
"toPrincipalName": "admin",
"updateAt": 1528532891000,
"updateBy": 1,
"warehousePrincipal": null,
"warehousePrincipalName": ""
}
```

* 异常情况

| 触发条件                | 返回信息   |
| :------------------ | :----- |
| 领料单是生产领料单不是研发领料单 | null |

#### 查询研发领料单

* URL

| 协议   | URL                   | 方法   |
| :--- | :-------------------- | :--- |
| HTTP | /development-draw/searchBill | POST |

* 请求参数

| 名称              | 类型     | 描述      |
| :-------------- | :----- | :------ |
| current     | Integer | 当前位置     |
| limit     | Integer | 一次读取长度     |
| sortColumn | String | 按哪一列排序 |
| sort | String | 排序方式 升序 降序 |
| searchKey | String | 关键字查找 |
| state | Integer | 订单状态 |
| beginTime | Long | 开始时间 |
| endTime | Long | 结束时间 |

* 返回结果示例

```json
[{
"auditAt": null,
"auditBy": null,
"auditName": "",
"billId": 7,
"billNo": "DDM180609162811055",
"billState": 1,
"billStateCn": "待审核",
"billTime": 1528532891000,
"billTimeLocal": "2018-06-09 16:28:11",
"createAt": 1528532891000,
"createBy": 1,
"detailPermission": true,
"drawReason": 2,
"finishAt": null,
"finishBy": null,
"finishName": "",
materialList:[{
"length": 0
}],
"relatedBill": null,
"remark": "",
"removePermission": true,
"toPrincipal": 1,
"toPrincipalName": "admin",
"updateAt": 1528532891000,
"updateBy": 1,
"warehousePrincipal": null,
"warehousePrincipalName": ""
}]
```

#### 审核研发领料单

* URL

| 协议   | URL            | 方法   |
| :--- | :------------- | :--- |
| HTTP | /development-draw/auditBill | POST |

* 请求参数

| 名称      | 类型      | 描述   |
| :------ | :------ | :--- |
| idList | String | 审核的研发领料单id列表|

* 返回结果示例

```json
success
```

#### 反审核研发领料单

* URL

| 协议   | URL            | 方法   |
| :--- | :------------- | :--- |
| HTTP | /development-draw/unauditBill | POST |

* 请求参数

| 名称     | 类型      | 描述                  |
| :----- | :------ | :------------------ |
| idList | String | 反审核的研发领料单id列表 |

* 返回结果示例

```json
success
```

#### 添加研发领料单

* URL

| 协议   | URL           | 方法   |
| :--- | :------------ | :--- |
| HTTP | /development-draw/addBill | POST |

* 请求参数

| 名称     | 类型     | 描述            |
| :----- | :----- | :------------ |
| remark | String | 该研发领料单相关备注 |
| materialList | String | 该研发领料单相关物料列表 |

* 返回结果示例

```json
{
"auditAt": null,
"auditBy": null,
"auditName": "",
"billId": 9,
"billNo": "DDM180610011113644",
"billState": 1,
"billTime": 1528564274000,
"createAt": 1528564274000,
"createBy": 1,
"drawReason": 2,
"finishAt": null,
"finishBy": null,
"finishName": "",
"materialList":[{
"billId": 9,
"billMaterialId": 32,
"materialId": 1,
"materialName": "0603电阻（1千欧）",
"materialNo": "0603DZ001",
"quantity": 1,
"remark": "hahaha",
}],
"relatedBill": null,
"remark": "xixixi",
"toPrincipal": 1,
"toPrincipalName": "admin",
"updateAt": 1528564274000,
"updateBy": 1,
"warehousePrincipal": null,
"warehousePrincipalName": ""
}
```

#### 更新研发领料单（单个）

* URL

| 协议   | URL           | 方法   |
| :--- | :------------ | :--- |
| HTTP | /development-draw/updateBill | POST |

* 请求参数

| 名称         | 类型      | 描述                  |
| :--------- | :------ | :------------------ |
| billId    | Integer | 研发领料单id号                |
| remark      | String | 备注               |
| materialList | String  | 物料列表                |

* 返回结果示例

```json
{
"auditAt": null,
"auditBy": null,
"auditName": "",
"billId": 9,
"billNo": "DDM180610011113644",
"billState": 1,
"billTime": 1528564274000,
"createAt": 1528564274000,
"createBy": 1,
"drawReason": 2,
"finishAt": null,
"finishBy": null,
"finishName": "",
"materialList":[{
"billId": 9,
"billMaterialId": 32,
"materialId": 1,
"materialName": "0603电阻（1千欧）",
"materialNo": "0603DZ001",
"quantity": 1,
"remark": "hahaha",
}],
"relatedBill": null,
"remark": "改了备注",
"toPrincipal": 1,
"toPrincipalName": "admin",
"updateAt": 1528564760000,
"updateBy": 1,
"warehousePrincipal": null,
"warehousePrincipalName": ""
}
```

#### 删除研发领料单（单个或多个）

* URL

| 协议   | URL           | 方法   |
| :--- | :------------ | :--- |
| HTTP | /development-draw/deleteBill  | POST |

* 请求参数

| 名称         | 类型      | 描述              |
| :--------- | :------ | :-------------- |
| idList    | String | 删除的研发领料单ID列表            |

* 返回结果示例

```json
success
```

### DevelopmentReturnController

#### 获取研发退料信息（单个）

* URL

| 协议   | URL        | 方法   |
| :--- | :--------- | :--- |
| HTTP | /development-return/billDetail | POST |

* 请求参数

| 名称         | 类型      | 描述              |
| :--------- | :------ | :-------------- |
| billId  | int  | 研发退料单id号             |

* 返回结果示例

```json
{
"auditAt": null,
"auditBy": null,
"auditName": "",
"billId": 3,
"billNo": "DDM180610102411977",
"billState": 1,
"billStateCn": "待审核",
"billTime": 1528597452000,
"billTimeLocal": "2018-06-10 10:24:12",
"createAt": 1528597452000,
"createBy": 1,
"finishAt": null,
"finishBy": null,
"finishName": "",
"fromPrincipal": 1,
"fromPrincipalName": "admin",
"materialList": [{
"billId": 3,
"billMaterialId": 28,
"materialId": 10,
"materialName": "4274电源模块（5V）",
"materialNo": "4274DY001",
"quantity": 1,
"remark": ""
}],
"relatedBill": null,
"remark": "grege",
"returnReason": 2,
"returnReasonCn": "研发退料",
"updateAt": 1528597452000,
"updateBy": 1,
"warehousePrincipal": null,
"warehousePrincipalName": ""
}
```

* 异常情况

| 触发条件                | 返回信息   |
| :------------------ | :----- |
| 退料单是生产退料单不是研发退料单 | null |

#### 查询研发退料单

* URL

| 协议   | URL                   | 方法   |
| :--- | :-------------------- | :--- |
| HTTP | /development-return/searchBill | POST |

* 请求参数

| 名称              | 类型     | 描述      |
| :-------------- | :----- | :------ |
| current     | Integer | 当前位置     |
| limit     | Integer | 一次读取长度     |
| sortColumn | String | 按哪一列排序 |
| sort | String | 排序方式 升序 降序 |
| searchKey | String | 关键字查找 |
| state | Integer | 订单状态 |
| beginTime | Long | 开始时间 |
| endTime | Long | 结束时间 |

* 返回结果示例

```json
[{
"auditAt": null,
"auditBy": null,
"auditName": "",
"billId": 3,
"billNo": "DDM180610102411977",
"billState": 1,
"billStateCn": "待审核",
"billTime": 1528597452000,
"billTimeLocal": "2018-06-10 10:24:12",
"createAt": 1528597452000,
"createBy": 1,
"detailPermission": true,
"returnReason": 2,
"finishAt": null,
"finishBy": null,
"finishName": "",
materialList:[{
"length": 0
}],
"relatedBill": null,
"remark": "",
"removePermission": true,
"fromPrincipal": 1,
"fromPrincipalName": "admin",
"updateAt": 1528597452000,
"updateBy": 1,
"warehousePrincipal": null,
"warehousePrincipalName": ""
}]
```

#### 审核研发退料单

* URL

| 协议   | URL            | 方法   |
| :--- | :------------- | :--- |
| HTTP | /development-return/auditBill | POST |

* 请求参数

| 名称      | 类型      | 描述   |
| :------ | :------ | :--- |
| idList | String | 审核的研发退料单id列表|

* 返回结果示例

```json
success
```

#### 反审核研发退料单

* URL

| 协议   | URL            | 方法   |
| :--- | :------------- | :--- |
| HTTP |  /development-return/unauditBill | POST |

* 请求参数

| 名称     | 类型      | 描述                  |
| :----- | :------ | :------------------ |
| idList | String | 反审核的研发退料单id列表 |

* 返回结果示例

```json
success
```

#### 添加研发退料单

* URL

| 协议   | URL           | 方法   |
| :--- | :------------ | :--- |
| HTTP |  /development-return/addBill | POST |

* 请求参数

| 名称     | 类型     | 描述            |
| :----- | :----- | :------------ |
| remark | String | 该研发退料单相关备注 |
| materialList | String | 该研发退料单相关物料列表 |

* 返回结果示例

```json
{
"auditAt": null,
"auditBy": null,
"auditName": "",
"billId": 3,
"billNo": "DDM180610102411977",
"billState": 1,
"billTime": 1528597452000,
"createAt": 1528597452000,
"createBy": 1,
"returnReason": 2,
"finishAt": null,
"finishBy": null,
"finishName": "",
"materialList":[{
"billId": 3,
"billMaterialId": 32,
"materialId": 1,
"materialName": "0603电阻（1千欧）",
"materialNo": "0603DZ001",
"quantity": 1,
"remark": "hahaha",
}],
"relatedBill": null,
"remark": "xixixi",
"fromPrincipal": 1,
"fromPrincipalName": "admin",
"updateAt": 1528597452000,
"updateBy": 1,
"warehousePrincipal": null,
"warehousePrincipalName": ""
}
```

#### 更新研发退料单（单个）

* URL

| 协议   | URL           | 方法   |
| :--- | :------------ | :--- |
| HTTP | /development-return/updateBill | POST |

* 请求参数

| 名称         | 类型      | 描述                  |
| :--------- | :------ | :------------------ |
| billId    | Integer | 研发退料单id号                |
| remark      | String | 备注               |
| materialList | String  | 物料列表                |

* 返回结果示例

```json
{
"auditAt": null,
"auditBy": null,
"auditName": "",
"billId": 3,
"billNo": "DDM180610102411977",
"billState": 1,
"billTime": 1528597452000,
"createAt": 1528597452000,
"createBy": 1,
"returnReason": 2,
"finishAt": null,
"finishBy": null,
"finishName": "",
"materialList":[{
"billId": 3,
"billMaterialId": 32,
"materialId": 1,
"materialName": "0603电阻（1千欧）",
"materialNo": "0603DZ001",
"quantity": 1,
"remark": "hahaha",
}],
"relatedBill": null,
"remark": "改了备注",
"fromPrincipal": 1,
"fromPrincipalName": "admin",
"updateAt": 1528597460000,
"updateBy": 1,
"warehousePrincipal": null,
"warehousePrincipalName": ""
}
```

#### 删除研发退料单（单个或多个）

* URL

| 协议   | URL           | 方法   |
| :--- | :------------ | :--- |
| HTTP | /development-return/deleteBill  | POST |

* 请求参数

| 名称         | 类型      | 描述              |
| :--------- | :------ | :-------------- |
| idList    | String | 删除的研发退料单ID列表            |

* 返回结果示例

```json
success
```