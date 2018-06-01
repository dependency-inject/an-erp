import { http } from '../libs/http';

var add = (supplier) => http.post('supplier/add', supplier);

var getById = (supplierId) => http.post('supplier/getById', { supplierId: supplierId });

var remove = (idList) => http.post('supplier/remove', { idList: idList });

var search = (queryParameters) => http.post('supplier/search', queryParameters);

var update = (supplier) => http.post('supplier/update', supplier);

var addMaterial = (materialPrice) => http.post('supplier/addMaterial', materialPrice);

var removeMaterial = (idList) => http.post('supplier/removeMaterial', { idList: idList });

var searchMaterial = (queryParameters) => http.post('supplier/searchMaterial', queryParameters);

var updateMaterial = (materialPrice) => http.post('supplier/updateMaterial', materialPrice);

var getMaterialList = () => http.post('supplier/getMaterialList');

export default {add, getById, remove, search, update, addMaterial, removeMaterial, searchMaterial, updateMaterial, getMaterialList };
