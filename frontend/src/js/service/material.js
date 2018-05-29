import { http } from '../libs/http';

var search = (queryParameters) => http.post('material/search', queryParameters);

var updateCost = (material) => http.post('material/updateCost', material);

export default { search, updateCost };
