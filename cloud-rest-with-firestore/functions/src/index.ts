import * as functions from 'firebase-functions';
import * as admin from 'firebase-admin';
import * as express from 'express';
import * as bodyParser from "body-parser";
import * as firebaseHelper from 'firebase-functions-helper';

const swaggerUi = require('swagger-ui-express');
const swaggerDocument = require('../swagger.json');

const firebase = admin.initializeApp(functions.config().firebase);
const app = express();
const main = express();
const db = admin.firestore();
const settings = {/* your settings... */ timestampsInSnapshots: true };
db.settings(settings);

main.use('/api-docs/', swaggerUi.serve, swaggerUi.setup(swaggerDocument));
main.use('/store/v1', app);
main.use(bodyParser.json());
main.use(bodyParser.urlencoded({ extended: false }));
const ordersCollection = 'orders';

export const orderAPI = functions.https.onRequest(main);

// View all orders
app.get('/order', (req, res) => {
    return firebaseHelper.firestore
        .backup(db, ordersCollection)
        .then(data => res.status(200).json({ 'orders': data }))
})

// View a order
app.get('/order/:orderId', (req, res) => {
    firebaseHelper.firestore
        .getDocument(db, ordersCollection, req.params.orderId)
        .then(data => res.status(200).json({ order: data }));
    return true;
})

// Add new order
app.post('/order', (req, res) => {
    firebaseHelper.firestore
        .createNewDocument(db, ordersCollection, req.body).then(error => {
            console.log(error);
            res.send('order created succesfully !!!');
        });
    return true;
})

// Update new order
app.patch('/order/:orderId', (req, res) => {
    firebaseHelper.firestore
        .updateDocument(db, ordersCollection, req.params.orderId, req.body);
    res.send('Update a new order');
})

// Delete a order 
app.delete('/order/:orderId', (req, res) => {
    firebaseHelper.firestore
        .deleteDocument(db, ordersCollection, req.params.orderId);
    res.send('order is deleted');
})
