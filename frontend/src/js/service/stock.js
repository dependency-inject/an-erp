import { http } from '../libs/http';

var searchLackMaterial = (queryParameters) => http.post('stock/getMaterialLack', queryParameters);

var searchPriceReverse = (queryParameters) => http.post('stock/findSupplierPrice', queryParameters);

var getMaterialList = () => http.post('stock/getMaterialList');

export default { searchLackMaterial, searchPriceReverse, getMaterialList };
