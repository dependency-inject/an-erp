import { http } from '../libs/http';

var search = (queryParameters) => http.post('material/search', queryParameters);

var update = (material) => http.post('material/update', material);

export default { search, update };
