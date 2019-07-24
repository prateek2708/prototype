import { pubsub } from 'firebase-functions';
import * as admin from "firebase-admin";
const uuidv1 = require('uuid/v1');
import { config } from '../config.json';

const db = admin.firestore();

const BATCH_SIZE = config.batch.batchSize;

const getBatch = async () => {
    let queryRef = db.collection(config.firestore.transactions.collectionPath)
        .where('transactionState', "==", 'NEW');
    if (config.batch.batchEnabled) {
        queryRef = queryRef.limit(BATCH_SIZE);
    }
    const querySnapshot = await queryRef.get();
    return querySnapshot.docs.map(documentSnapshot => {
        return documentSnapshot.data();
    });
};

const markBatchAsProcessed = async (batchId) => {
    let noRecords = false;
    while (!noRecords) {
        const batchData = await getBatch();
        if (batchData.length === 0) {
            console.log(`No transaction present in batchId: ${batchId}`);
            noRecords = true;
            return true;

        }
        console.log(`Batch to be marked for processing batchId: ${batchId}, batchData: ${JSON.stringify(batchData)}`);
        // Get a new write batch
        const batch = db.batch();
        Object.keys(batchData).forEach(key => {
            const transactionId = batchData[key].transactionId;
            const transactionRef = db.collection(config.firestore.transactions.collectionPath).doc(transactionId);
            batch.update(transactionRef, {
                'transactionState': 'BATCHED',
                "updateDateTime": new Date().toISOString(),
            });
        });
        // Commit the batch
        await batch.commit();
    }
    return true;
};

export const batchJOB = pubsub.schedule(config.batch.cronExpression)
    .onRun(async (context) => {
        const batchId = uuidv1();
        await markBatchAsProcessed(batchId);
        console.log(`batchId ${batchId} marked In processing...`);
        return true;
    });