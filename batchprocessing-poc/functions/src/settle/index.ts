import { firestore } from 'firebase-functions';
import * as admin from "firebase-admin";
import { config } from '../config.json';
const db = admin.firestore();

const performSettlement = () => {
    return new Promise(resolve => {
        setTimeout(resolve, config.batch.settleTime);
    });
};

export const settlement = firestore.document(config.firestore.transactions.docPath).
    onWrite(async (change, context) => {
        const transId = context.params.transId;
        const newDoc = change.after.data();
        if (newDoc) {
            try {
                //ensure ony batched tranactions are picked up.
                if (['BATCHED'].includes(newDoc.transactionState)) {
                    console.log("Document to be settled", JSON.stringify(newDoc));
                    const batchRef = db.collection('store/cart/transactions').doc(transId);

                    //double check to ensure no other event is processing the same transactions id. 
                    //To prevent double sent issue.  
                    await db.runTransaction(async tx => {
                        const doc = await tx.get(batchRef);
                        if (!doc.exists) {
                            throw Error(`Transaction for id: ${transId} does not exists`);
                        }
                        if (!doc.data) {
                            throw Error(`Transaction for id: ${transId} data missing`);
                        }
                        if (doc.data['transactionState'] === 'IN_PROGRESS') {
                            throw Error(`TransactionId ${transId} is already in progress`);
                        }
                        await tx.update(batchRef, {
                            'transactionState': 'IN_PROGRESS',
                            "settled": true,
                            "updateDateTime": new Date().toISOString(),
                        });
                    });

                    console.log(transId, 'Transaction in getting processed');

                    await performSettlement();

                    await db.collection('store/cart/transactions').doc(transId).update({
                        'transactionState': 'PROCESSED',
                        "settled": true,
                        "updateDateTime": new Date().toISOString(),
                    });
                    console.log(transId, 'transaction settled');
                    return true;
                }
            } catch (error) {
                console.log(`Failed to process transId: ${transId}`);
            }
        }
        return false;
    });
