import { config } from 'firebase-functions';
import { initializeApp } from 'firebase-admin';

const admin = initializeApp(config().firebase);
const db = admin.firestore();
const settings = { timestampsInSnapshots: true };
db.settings(settings);

import { restApi } from "./api";
import { batchJOB } from "./batch";
import { settlement } from "./settle";

exports.batchService = {
    restApi,
    batchJOB,
    settlement
};
