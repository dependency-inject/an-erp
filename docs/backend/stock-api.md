## 库存管理 API

所有请求以 HTTP 状态码 200 表示请求成功，请求结果以 application/json 格式在 Response Body 中返回；状态码 400 表示请求失败，具体的错误消息以文本形式在 Response Body 中返回。

### StockController

####统计货品仓储

* URL

| 协议 |          URL          | 方法 |
| :--: | :-------------------: | ---- |
| HTTP | /stock/product-search | POST |

* 返回结果示例

```json
[
    {materialId: 1, materialNo: "E38ABC123", materialName: "物料1号", unit: "件", categoryId: 1, …},
	{materialId: 3, materialNo: "E38ABC187", materialName: "物料18号", unit: "件", categoryId: 4, …}
]
```

####统计物料仓储

* URL

| 协议 |          URL           | 方法 |
| :--: | :--------------------: | :--: |
| HTTP | /stock/material-search | POST |


* 返回结果示例

```json
[
  {materialId: 1, materialNo: "E38ABC123", materialName: "物料1号", unit: "件", categoryId: 1, …},
	{materialId: 3, materialNo: "E38ABC187", materialName: "物料18号", unit: "件", categoryId: 4, …}
]
```


### WarehouseController

#### 获取仓库信息（分页）

* URL


| 协议 | URL               | 方法 |
| :--- | :---------------- | :--- |
| HTTP | /warehouse/search | POST |

* 请求参数

| 名称       | 类型    | 描述                  |
| :--------- | :------ | :-------------------- |
| current    | Integer | 当前页号              |
| limit      | Integer | 每页记录数            |
| sortColumn | String  | 排序字段              |
| sort       | String  | 排序方式（ASC、DESC） |
| searchKey  | String  | 搜索关键字            |

* 返回结果示例

```json
{
    total: 2
    list: [
    { "warehouseId": 1, "warehouseNo": "5JA4800", ...},
	{ "warehouseId": 32, "warehouseNo": "5JA$832", ...}
    ]
}
```


#### 增加仓库

* URL

| 协议 | URL            | 方法 |
| :--- | :------------- | :--- |
| HTTP | /warehouse/add | POST |

* 请求参数

| 名称          | 类型   | 描述     |
| :------------ | :----- | :------- |
| warehouseNo   | String | 仓库编号 |
| warehouseName | String | 仓库名称 |

* 返回结果示例

```json
{
  "warehouseId": 12,
  "warehouseNo": "aaa",
  "warehouseName": "西北重工仓",
  "createAt": 1528720469000,
  "createBy": 1,
  "updateAt": 1528720469000,
  "updateBy": 1
}
```
#### 修改仓库信息

* URL

| 协议 | URL               | 方法 |
| :--- | :---------------- | :--- |
| HTTP | /warehouse/update | POST |

* 请求参数

| 名称          | 类型    | 描述     |
| :------------ | :------ | :------- |
| warehouseNo   | String  | 仓库编号 |
| warehouseName | String  | 仓库名称 |
| warehouseId   | Integer | 仓库id   |

* 返回结果示例

```json
{
  "warehouseId": 13,
  "warehouseNo": "aaa",
  "warehouseName": "西北不重工仓",
  "createAt": 1528720469000,
  "createBy": 1,
  "updateAt": 1528720469111,
  "updateBy": 2
}
```
#### 删除客户

* URL

| 协议 | URL               | 方法 |
| :--- | :---------------- | :--- |
| HTTP | /warehouse/remove | POST |

* 请求参数

| 名称   | 类型   | 描述                  |
| :----- | :----- | :-------------------- |
| idList | String | 仓库id列表（"11,12"） |

* 返回结果示例

```json
"success"
```
