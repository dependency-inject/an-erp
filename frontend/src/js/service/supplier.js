import { http } from '../libs/http';

var add = (supplier) => http.post('supplier/add', supplier);

var getById = (supplierId) => http.post('supplier/getById', { supplierId: supplierId });

var remove = (idList) => http.post('supplier/remove', { idList: idList });

var search = (queryParameters) => http.post('supplier/search', queryParameters);

var update = (supplier) => http.post('supplier/update', supplier);

var addPrice = (materialPrice) => http.post('supplier/addmaterial', materialPrice);

var removePrice = (idList) => http.post('supplier/removematerial', { idList: idList });

var searchPrice = (queryParameters) => http.post('supplier/searchmaterial', queryParameters);

var updatePrice = (supplier) => http.post('supplier/updatematerial', supplier);

var searchLackMaterial = (queryParameters) => http.post('supplier/searchlackmaterial', queryParameters);

export default {add, getById, remove, search, update, addPrice, removePrice, searchPrice, updatePrice, searchLackMaterial};
