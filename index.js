const admin = require('firebase-admin');
const functions = require('firebase-functions');
admin.initializeApp(functions.config().firebase);

var db = admin.firestore();


// 문자가 온다면
exports.receiveSMS = functions.firestore
    .document('chat/2/message/{data}')
    .onCreate((change, context) => {
    const newValue = change.data();
    const title

    const payload = {
        notification: {
          title: 'SMS',
          body: `새로운 무통장 입금이 있습니다.`
        }
      };

    var tokens = [];

    // FireStore 에서 데이터 읽어오기
    db.collection('users').get().then((snapshot) => {
        snapshot.forEach((doc) => {

            // console.log(doc.id);
            // console.log(doc.data().token);
            // console.log(doc.data().created);
            tokens.push(doc.data().token);
        });

        console.log(tokens);

        if (tokens.length > 0 ){
            admin.messaging().sendToDevice(tokens, payload)
            .then(function(response) {
                // See the MessagingDevicesResponse reference documentation for
                // the contents of response.
                console.log('Successfully sent message:', response);
                return true;
            })
            .catch(function(error) {
                console.log('Error sending message:', error);
                return false;
            });

        }
        return true;
    })
    .catch((err) => {
        console.log('Error getting documents', err);
        return false;
    });


});
// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions

exports.helloWorld = functions.https.onRequest((request, response) => {
 response.send("Hello from Firebase!");
 });
