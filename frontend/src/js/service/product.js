import { http } from '../libs/http';

var add = (product) => http.post('product/add', product);

var getById = (productId) => http.post('product/getById', { productId: productId });

var getList = (queryParameters) => http.post('product/getList', queryParameters);

var remove = (productIdList) => http.post('product/remove', { productIdList: productIdList });

var search = (queryParameters) => http.post('product/search', queryParameters);

var update = (product) => http.post('product/update', product);

var updateClosedState = (productIdList, closed) => http.post('product/updateClosedState', { productIdList: productIdList, closed: closed });

var getMaterialList = () => http.post('product/getMaterialList');

export default { add, getById, getList, remove, search, update, updateClosedState, getMaterialList };
