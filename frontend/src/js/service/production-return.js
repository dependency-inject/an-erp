import { http } from '../libs/http';

var getStatistics = () => http.post('production-return/getStatistics');

var getBill = (billId) => http.post('production-return/billDetail', { billId: billId });

var searchBill = (queryParameters) => http.post('production-return/searchBill', queryParameters);

var auditBill = (idList) => http.post('production-return/auditBill', { idList: idList });

var unauditBill = (idList) => http.post('production-return/unauditBill', { idList: idList });

var addBill = (bill) => http.post('production-return/addBill', bill)

var updateBill = (bill) => http.post('production-return/updateBill', bill)

var deleteBill = (idList) => http.post('production-return/deleteBill', { idList: idList });

export default { getStatistics, getBill, searchBill, auditBill, unauditBill, addBill, updateBill, deleteBill };
