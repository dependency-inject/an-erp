import { http } from '../libs/http';

var searchMaterialCost = (queryParameters) => http.post('stock/searchMaterialCost', queryParameters);

export default { searchMaterialCost };
