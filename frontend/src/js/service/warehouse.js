import { http } from '../libs/http';

var add = (warehouse) => http.post('warehouse/add', warehouse);

var getById = (warehouseId) => http.post('warehouse/getById', { warehouseId: warehouseId });

var remove = (idList) => http.post('warehouse/remove', { idList: idList });

var search = (queryParameters) => http.post('warehouse/search', queryParameters);

var update = (warehouse) => http.post('warehouse/update', warehouse);

export default { add, getById, remove, search, update };
