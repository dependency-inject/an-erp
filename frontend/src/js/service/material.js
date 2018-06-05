import { http } from '../libs/http';

var add = (material) => http.post('material/add', material);

var getById = (materialId) => http.post('material/getById', { materialId: materialId });

var getList = () => http.post('material/getList');

var remove = (idList) => http.post('material/remove', { idList: idList });

var search = (queryParameters) => http.post('material/search', queryParameters);

var update = (material) => http.post('material/update', material);

var updateCost = (material) => http.post('material/updateCost', material);

export default { add, getById, getList, remove, search, update, updateCost };
