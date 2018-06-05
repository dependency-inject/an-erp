import { http } from '../libs/http';

var search = (queryParameters) => http.post('product-instock/search', queryParameters);

var add = (productinstock) => http.post('product-instock/add', productinstock);

var getById = (billId) => http.post('product-instock/getById', { billId: billId });

var remove = (idList) => http.post('product-instock/remove', { idList: idList });

var update = (productinstock) => http.post('product-instock/update', productinstock);

var getProductIdList = () => http.post('product-instock/getProductIdList');

var audit = (idList) => http.post('product-instock/audit', { idList: idList });

var unaudit = (idList) => http.post('product-instock/unaudit', { idList: idList });

var getAdmins = () => http.post('product-instock/getAdmins');

var getWarehouses = () => http.post('product-instock/getWarehouses');

export default { add, getById, remove, search, update, getProductIdList, audit, unaudit,getAdmins,getWarehouses };
