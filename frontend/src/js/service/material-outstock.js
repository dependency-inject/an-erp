import { http } from '../libs/http';

var add = (materialOutstockBill) => http.post('material-outstock/add', materialOutstockBill);

var getById = (billId) => http.post('material-outstock/getById', { billId: billId });

var remove = (idList) => http.post('material-outstock/remove', { idList: idList });

var search = (queryParameters) => http.post('material-outstock/search', queryParameters);

var update = (materialOutstockBill) => http.post('material-outstock/update', materialOutstockBill);

var updateAuditState = (idList, materialOutstockState) => http.post('material-outstock/updateAuditState', { idList: idList, materialOutstockState: materialOutstockState });

export default {add, getById, remove, search, update, updateAuditState};
