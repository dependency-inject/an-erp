## 采购辅助管理 API

所有请求以 HTTP 状态码 200 表示请求成功，请求结果以 application/json 格式在 Response Body 中返回；状态码 400 表示请求失败，具体的错误消息以文本形式在 Response Body 中返回。


### SupplierController

#### 新增供货商信息

* URL

| 协议   | URL        | 方法   |
| :--- | :--------- | :--- |
| HTTP | /supplier/add | POST |

* 请求参数

| 名称         | 类型      | 描述              |
| :--------- | :------ | :-------------- |
| supplierName  | String  | 供应商姓名             |
| contact   | String  | 联系人            |
| contactPhone     | String | 联系方式   |
| region     | String  | 地区              |
| address | String  | 地址 |

* 返回结果示例

```json
{
  "supplierId": 2,
  "supplierName": "supplier15",
  "contact": "王xx",
  "contactPhone": 123xxxx1234,
  "region": "中国",
  "address": xx省xx市xx区xx路xx号
}
```

#### 获取供货商信息（单个）

* URL

| 协议   | URL            | 方法   |
| :--- | :------------- | :--- |
| HTTP | /supplier/getById | POST |

* 请求参数

| 名称      | 类型      | 描述   |
| :------ | :------ | :--- |
| supplierId | Integer | 供应商ID |

* 返回结果示例


```json
{
  "supplierId": 2,
  "supplierName": "supplier15",
  "contact": "王xx",
  "contactPhone": 123xxxx1234,
  "region": "中国",
  "address": xx省xx市xx区xx路xx号
}
```
#### 删除供应商信息（批量）

* URL

| 协议   | URL           | 方法   |
| :--- | :------------ | :--- |
| HTTP | /supplier/remove | POST |

* 请求参数

| 名称     | 类型     | 描述            |
| :----- | :----- | :------------ |
| idList | String | 供应商ID列表（用逗号分隔） |

* 返回结果示例

```json
success
```

#### 获取供应商信息（分页）

* URL

| 协议   | URL           | 方法   |
| :--- | :------------ | :--- |
| HTTP | /supplier/search | POST |

* 请求参数

| 名称         | 类型      | 描述                  |
| :--------- | :------ | :------------------ |
| current    | Integer | 当前页号                |
| limit      | Integer | 每页记录数               |
| sortColumn | String  | 排序字段                |
| sort       | String  | 排序方式（ASC、DESC）      |
| searchKey  | String  | 搜索关键字               |


* 返回结果示例

```json
{
  total: 2,
  list: [
    { "supplierId": 1,
  "supplierName": "supplier15", ... },
    { "supplierId": 2,
  "supplierName": "supplier8", ... }
  ]
}
```

#### 更新供货商信息

* URL

| 协议   | URL           | 方法   |
| :--- | :------------ | :--- |
| HTTP | /supplier/update | POST |

* 请求参数

| 名称         | 类型      | 描述              |
| :--------- | :------ | :-------------- |
| supplierID    | Integer | 供应商ID            |
| supplierName  | String  | 供应商姓名             |
| contact   | String  | 联系人            |
| contactPhone     | String | 联系方式   |
| region     | String  | 地区              |
| address | String  | 地址 |

* 返回结果示例


```json
{
  "supplierId": 2,
  "supplierName": "supplier15",
  "contact": "王xx",
  "contactPhone": 123xxxx1234,
  "region": "中国",
  "address": xx省xx市xx区xx路xx号
}
```
#### 新增供货商物料信息

* URL

| 协议   | URL        | 方法   |
| :--- | :--------- | :--- |
| HTTP | /supplier/addMaterial | POST |

* 请求参数

| 名称         | 类型      | 描述              |
| :--------- | :------ | :-------------- |
| supplierId  | Integer  | 供应商ID             |
| materialId   | Integer  | 物料ID            |
| price     | BigDecimal | 价格   |
| remark     | String  |  备注    |


* 返回结果示例

```json
{
  "supplierMaterialId": 2,
  "supplierId": "3",
  "materialId": "14",
  "materialName":"大头钉",
  "materialNo":"EXMA1234",
  "price": 124,
  "remark": "库存不足"
}
```
* 异常情况

| 触发条件                | 返回信息   |
| :------------------ | :----- |
| 已存在物料ID为materialId的物料信息 | 货品已在供料信息中，不可重复 |

#### 获取供货商物料信息（单个）

* URL

| 协议   | URL            | 方法   |
| :--- | :------------- | :--- |
| HTTP | /supplier/getMaterialById | POST |

* 请求参数

| 名称      | 类型      | 描述   |
| :------ | :------ | :--- |
| supplierMaterialId | Integer | 供应商物料ID |

* 返回结果示例


```json
{
  "supplierMaterialId": 2,
  "supplierId": "3",
  "materialId": "14",
  "materialName":"大头钉",
  "materialNo":"EXMA1234",
  "price": 124,
  "remark": "库存不足"
}
```
#### 删除供应商物料信息（批量）

* URL

| 协议   | URL           | 方法   |
| :--- | :------------ | :--- |
| HTTP | /supplier/removeMaterial | POST |

* 请求参数

| 名称     | 类型     | 描述            |
| :----- | :----- | :------------ |
| idList | String | 供应商物料ID列表（用逗号分隔） |

* 返回结果示例

```json
success
```

#### 获取供应商物料信息（分页）

* URL

| 协议   | URL           | 方法   |
| :--- | :------------ | :--- |
| HTTP | /supplier/searchMaterial | POST |

* 请求参数

| 名称         | 类型      | 描述                  |
| :--------- | :------ | :------------------ |
| supplierId    | Integer | 供应商ID                |
| current    | Integer | 当前页号                |
| limit      | Integer | 每页记录数               |
| sortColumn | String  | 排序字段                |
| sort       | String  | 排序方式（ASC、DESC）      |
| searchKey  | String  | 搜索关键字               |


* 返回结果示例

```json
{
  total: 2,
  list: [
    { "supplierMaterialId": 1,
  "supplierId": "supplier15", ... },
    { "supplierMaterialId": 2,
  "supplierId": "supplier8", ... }
  ]
}
```

#### 更新供货商物料信息

* URL

| 协议   | URL           | 方法   |
| :--- | :------------ | :--- |
| HTTP | /supplier/update | POST |

* 请求参数

| 名称         | 类型      | 描述              |
| :--------- | :------ | :-------------- |
| supplierMaterialId    | Integer | 供应商物料ID            |
| supplierId  | Integer  | 供应商ID             |
| materialId   | Integer  | 物料ID            |
| price     | BigDecimal | 价格   |
| remark     | String  |  备注    |


* 返回结果示例


```json
{
  "supplierMaterialId": 2,
  "supplierId": "3",
  "materialId": "14",
  "materialName":"大头钉",
  "materialNo":"EXMA1234",
  "price": 124,
  "remark": "库存不足"
}
```


### StockController

#### 获取缺料情况信息（分页）

* URL

| 协议   | URL           | 方法   |
| :--- | :------------ | :--- |
| HTTP | /stock/getMaterialLack | POST |

* 请求参数

| 名称         | 类型      | 描述                  |
| :--------- | :------ | :------------------ |
| current    | Integer | 当前页号                |
| limit      | Integer | 每页记录数               |
| sortColumn | String  | 排序字段                |
| sort       | String  | 排序方式（ASC、DESC）      |
| searchKey  | String  | 搜索关键字               |


* 返回结果示例

```json
{
  total: 2,
  list: [
    { "materialId": 1,
  "materialNo": "EXAM456", ... },
    { "materialId": 2,
  "materialNo": "EXAM457", ... }
  ]
}
```


#### 根据物料信息反查供应商报价（分页）

* URL

| 协议   | URL            | 方法   |
| :--- | :------------- | :--- |
| HTTP | /stock/findSupplierPrice | POST |

* 请求参数

| 名称         | 类型      | 描述                  |
| :--------- | :------ | :------------------ |
| materialId    | Integer | 物料ID                |
| current    | Integer | 当前页号                |
| limit      | Integer | 每页记录数               |
| sortColumn | String  | 排序字段                |
| sort       | String  | 排序方式（ASC、DESC）      |



* 返回结果示例

```json
{
  total: 2,
  list: [
    { "supplierMaterialId": 1,
  "supplierId": "supplier15", ... },
    { "supplierMaterialId": 2,
  "supplierId": "supplier8", ... }
  ]
}
```