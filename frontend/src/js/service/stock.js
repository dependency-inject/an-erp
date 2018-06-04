import { http } from '../libs/http';

var productSearch = (queryParameters) => http.post('stock/product-search', queryParameters);

var materialSearch = (queryParameters) => http.post('stock/material-search', queryParameters);

var searchMaterialCost = (queryParameters) => http.post('stock/searchMaterialCost', queryParameters);

export default { productSearch, materialSearch, searchMaterialCost };
