## 成本管理 API

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

  

####反查物料报价

* URL

| 协议 |           URL            | 方法 |
| :--: | :----------------------: | :--: |
| HTTP | /stock/findSupplierPrice | POST |


  * 返回结果示例

  ```json
[
	{supplierMaterialId: 1, supplierId: 1, materialId: 1, price: 600, remark: "233", materialName: "物料1号",…}, 
	{supplierMaterialId: 4, supplierId: 1, materialId: 3, price: 333, remark: "", materialName: "物料3号",…}
]
  ```

  ####获取缺料情况

* URL

| 协议 |          URL           | 方法 |
| :--: | :--------------------: | :--: |
| HTTP | /stock/getMaterialLack | POST |


  * 返回结果示例

  ```json
[
	{materialId: 1, materialNo: "E38ABC123", materialName: "物料1号", unit: "件", totalStock: 1,…},
	{materialId: 3, materialNo: "E38ABC125", materialName: "物料3号", unit: "件", totalStock: 1,…}
]
  ```

#### 搜索物料成本
* URL

| 协议 |            URL            | 方法 |
| :--: | :-----------------------: | :--: |
| HTTP | /stock/searchMaterialCost | POST |


  * 返回结果示例

  ```json
[
	{materialId: 5, materialNo: "QWE20170524119", materialName: "yitiji", unit: "cm", categoryId: 23,…}, 
	{materialId: 3, materialNo: "E38ABC125", materialName: "物料3号", unit: "件", categoryId: 2,…}
]
  ```