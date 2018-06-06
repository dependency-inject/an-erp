import { http } from '../libs/http';

var getById = (billId) => http.post('draw-material/getById', { billId: billId });

var getList = (queryParameters) => http.post('draw-material/getList', queryParameters);

export default { getById, getList };
