import { http } from '../libs/http';

var getBill = (billId) => http.post('development-return/billDetail', { billId: billId });

var searchBill = (queryParameters) => http.post('development-return/searchBill', queryParameters);

var auditBill = (idList) => http.post('development-return/auditBill', { idList: idList });

var unauditBill = (idList) => http.post('development-return/unauditBill', { idList: idList });

var addBill = (bill) => http.post('development-return/addBill', bill)

var updateBill = (bill) => http.post('development-return/updateBill', bill)

var deleteBill = (idList) => http.post('development-return/deleteBill', { idList: idList });

export default { getBill, searchBill, auditBill, unauditBill, addBill, updateBill, deleteBill };
