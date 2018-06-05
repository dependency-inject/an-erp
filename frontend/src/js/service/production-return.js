import { http } from '../libs/http';

var getBill = (billId) => http.post('production-return/billDetail', { billId: billId });

var searchBill = (queryParameters) => http.post('production-return/searchBill', queryParameters);

var auditBill = (idList) => http.post('production-return/auditBill', { idList: idList });

var unauditBill = (idList) => http.post('production-return/unauditBill', { idList: idList });

var getMaterialList = () => http.post('production-return/getMaterialList');

var addBill = (bill) => http.post('production-return/addBill', bill)

var updateBill = (bill) => http.post('production-return/updateBill', bill)

var deleteBill = (idList) => http.post('production-return/deleteBill', { idList: idList });

export default { getBill, searchBill, auditBill, unauditBill, getMaterialList, addBill, updateBill, deleteBill };
