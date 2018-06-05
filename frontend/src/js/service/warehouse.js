import { http } from '../libs/http';

var add = (warehouse) => http.post('warehouse/add', warehouse);

var getById = (warehouseId) => http.post('warehouse/getById', { warehouseId: warehouseId });

var getList = () => http.post('warehouse/getList');

var remove = (idList) => http.post('warehouse/remove', { idList: idList });

var search = (queryParameters) => http.post('warehouse/search', queryParameters);

var update = (warehouse) => http.post('warehouse/update', warehouse);

export default { add, getById, getList, remove, search, update };
