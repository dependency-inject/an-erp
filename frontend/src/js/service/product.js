import { http } from '../libs/http';

var add = (product) => http.post('product/add', product);

var getById = (productId) => http.post('product/getById', { productId: productId });

var remove = (productIdList) => http.post('product/remove', { productIdList: productIdList });

var search = (queryParameters) => http.post('product/search', queryParameters);

var update = (product) => http.post('product/update', product);

var updateClosedState = (productIdList, closed) => http.post('product/updateClosedState', { productIdList: productIdList, closed: closed });

var getCategoryList = () => http.post('product/getCategoryList');

var getMaterialList = () => http.post('product/getMaterialList');

export default { add, getById, remove, search, update, updateClosedState, getCategoryList, getMaterialList };
