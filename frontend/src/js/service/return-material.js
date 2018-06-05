import { http } from '../libs/http';

var getById = (billId) => http.post('return-material/getById', { billId: billId });

var getList = (queryParameters) => http.post('return-material/getList', queryParameters);

export default { getById, getList };
