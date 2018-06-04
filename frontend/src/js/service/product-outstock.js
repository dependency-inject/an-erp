import { http } from '../libs/http';

var search = (queryParameters) => http.post('product-outstock/search', queryParameters);

var add = (productoutstock) => http.post('product-outstock/add', productoutstock);

var getById = (billId) => http.post('product-outstock/getById', { billId: billId });

var remove = (idList) => http.post('product-outstock/remove', { idList: idList });

var update = (productoutstock) => http.post('product-outstock/update', productoutstock);

var getProductIdList = () => http.post('product-outstock/getProductIdList');

var audit = (idList) => http.post('product-outstock/audit', { idList: idList });

var unaudit = (idList) => http.post('product-outstock/unaudit', { idList: idList });

var getAdmins = () => http.post('product-outstock/getAdmins');

var getWarehouses = () => http.post('product-outstock/getWarehouses');

export default { add, getById, remove, search, update, getProductIdList, audit, unaudit,getAdmins,getWarehouses };
