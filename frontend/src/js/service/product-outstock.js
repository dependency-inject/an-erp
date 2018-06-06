import { http } from '../libs/http';

var search = (queryParameters) => http.post('product-outstock/search', queryParameters);

var add = (productoutstock) => http.post('product-outstock/add', productoutstock);

var getById = (billId) => http.post('product-outstock/getById', { billId: billId });

var remove = (idList) => http.post('product-outstock/remove', { idList: idList });

var update = (productoutstock) => http.post('product-outstock/update', productoutstock);

var audit = (idList) => http.post('product-outstock/audit', { idList: idList });

var unaudit = (idList) => http.post('product-outstock/unaudit', { idList: idList });

var finish = (billId) => http.post('product-outstock/finish', { billId: billId });

export default { add, getById, remove, search, update, audit, unaudit, finish };
