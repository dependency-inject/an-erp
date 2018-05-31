import { http } from '../libs/http';

var search = (queryParameters) => http.post('order/search', queryParameters);

var getById = (billId) => http.post('order/getById', { billId: billId });

var getProduct = (billId) => http.post('order/getProduct', { billId: billId });

var shenhe = (adminId, billId) => http.post('order/shenhe', { adminId: adminId, billId: billId });

var fanshenhe = (adminId, billId) => http.post('order/fanshenhe', { adminId: adminId, billId: billId });

var produce = (adminId, billId) => http.post('order/produce', { adminId: adminId, billId: billId });

var deliver = (adminId, billId) => http.post('order/deliver', { adminId: adminId, billId: billId });

var cancel = (adminId, billId) => http.post('order/cancel', { adminId: adminId, billId: billId });

var getProducts = () => http.post('order/getProducts');

var add = (order) => http.post('order/add', order);

var remove = (row) => http.post('order/remove', row);

export default { search, getById, getProduct, shenhe, fanshenhe, produce, deliver, cancel, getProducts, add, remove };
