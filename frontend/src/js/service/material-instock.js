import { http } from '../libs/http';

var add = (materialInstockBill) => http.post('material-instock/add', materialInstockBill);

var getById = (billId) => http.post('material-instock/getById', { billId: billId });

var remove = (idList) => http.post('material-instock/remove', { idList: idList });

var search = (queryParameters) => http.post('material-instock/search', queryParameters);

var update = (materialInstockBill) => http.post('material-instock/update', materialInstockBill);

var updateAuditState = (idList, materialInstockState) => http.post('material-instock/updateAuditState', { idList: idList, materialInstockState: materialInstockState });

export default {add, getById, remove, search, update, updateAuditState};
