import { http } from '../libs/http';

var add = (supplier) => http.post('supplier/add', supplier);

var getById = (supplierId) => http.post('supplier/getById', { supplierId: supplierId });

var remove = (idList) => http.post('supplier/remove', { idList: idList });

var search = (queryParameters) => http.post('supplier/search', queryParameters);

var update = (supplier) => http.post('supplier/update', supplier);

export default { add, getById, remove, search, update};
