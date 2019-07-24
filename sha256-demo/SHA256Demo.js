//npm install --g sha256

const sha256 = require('sha256');


const s1 = 'test code' ;
const s2 = 'hello world';
 
console.log("\n" + s1 + " : " + sha256(s1));
console.log("\n" + s2 + " : " + sha256(s2));

//test code : cf4817d9793e92a0b00b0afc241354f9a649866da1d883c604c0585ef4129b82

//hello world : b94d27b9934d3e08a52e52d7da7dabfac484efe37a5380ee9088f7ace2efcde9
