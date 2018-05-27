import { http } from '../libs/http';

var addmaterial = (material_id,quantity,remark)=> http.post('development-return/addmaterial', {material_id:material_id,quantity:quantity,remark:remark});
var allBills = () => http.post('development-return/allBills',);
var getBill = (id) => http.post('development-return/billdetail', {bill_id:id});
var addBill=(billno,remark,reason) => http.post('development-return/addbill',{billno:billno,remark:remark,reason:reason})
var deletebills= (bill_id) => http.post('development-return/deletebills', {bill_id:bill_id});
var getAllBillMaterials=(id) => http.post('development-return/allMaterial', {bill_id:id});
var deleteitem=(id) => http.post('development-return/deletematerial', {material_id:id});
var searchBill = (search) => http.post('development-return/searchbill', {search:search});
var changematerials= (bill_id,material,quantity,remark)=> http.post('development-return/changematerials', {bill_id:bill_id,material:material,quantity:quantity,remark:remark});
var deletematerials= (bill_id,material)=>http.post('development-return/deletematerials', {bill_id:bill_id,material:material});
var changestatus=(bill_id,status)=>http.post('development-return/cahngestate', {bill_id:bill_id,status:status});

export default { addmaterial, allBills,getBill,addBill,deletebills,getAllBillMaterials,deleteitem,searchBill,changematerials,deletematerials,changestatus};
