import { http } from '../libs/http';

var addmaterial = (material_id,quantity,remark)=> http.post('development-draw/addmaterial', {material_id:material_id,quantity:quantity,remark:remark});
var allBills = () => http.post('development-draw/allBills',);
var getBill = (id) => http.post('development-draw/billdetail', {bill_id:id});
var addBill=(billno,remark,reason) => http.post('development-draw/addbill',{billno:billno,remark:remark,reason:reason})
var deletebills= (bill_id) => http.post('development-draw/deletebills', {bill_id:bill_id});
var getAllBillMaterials=(id) => http.post('development-draw/allMaterial', {bill_id:id});
var deleteitem=(id) => http.post('development-draw/deletematerial', {material_id:id});
var searchBill = (search) => http.post('development-draw/searchbill', {search:search});
var changematerials= (bill_id,material,quantity,remark)=> http.post('development-draw/changematerials', {bill_id:bill_id,material:material,quantity:quantity,remark:remark});
var deletematerials= (bill_id,material)=>http.post('development-draw/deletematerials', {bill_id:bill_id,material:material});
var changestatus=(bill_id,status)=>http.post('development-draw/cahngestate', {bill_id:bill_id,status:status});

export default { addmaterial, allBills,getBill,addBill,deletebills,getAllBillMaterials,deleteitem,searchBill,changematerials,deletematerials,changestatus};
