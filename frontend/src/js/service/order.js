import { http } from '../libs/http';

var search = (queryParameters) => http.post('order/search', queryParameters);

var getById = (billId) => http.post('order/getById', { billId: billId });

var audit = (idList) => http.post('order/audit', { idList: idList });

var unaudit = (idList) => http.post('order/unaudit', { idList: idList });

var produce = (billId) => http.post('order/produce', { billId: billId });

var delivery = (billId) => http.post('order/delivery', { billId: billId });

var cancel = (billId) => http.post('order/cancel', { billId: billId });

var getClientList = () => http.post('order/getClientList');

var getProductList = () => http.post('order/getProductList');

var add = (order) => http.post('order/add', order);

var update = (order) => http.post('order/update', order);

var remove = (idList) => http.post('order/remove', { idList: idList });

var getMaterialRequired = (billId) => http.post('order/getMaterialRequired', { billId: billId });

export default { search, getById, audit, unaudit, produce, delivery, cancel, getClientList, getProductList, add, update, remove, getMaterialRequired };
