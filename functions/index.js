/**
 * Import function triggers from their respective submodules:
 *
 * const { onCall } = require("firebase-functions/v2/https");
 * const { onDocumentWritten } = require("firebase-functions/v2/firestore");
 *
 * See a full list of supported triggers at https://firebase.google.com/docs/functions
 */

// Create and deploy your first functions
// https://firebase.google.com/docs/functions/get-started

// exports.helloWorld = onRequest((request, response) => {
//   logger.info("Hello logs!", { structuredData: true });
//   response.send("Hello from Firebase!");
// });

const functions = require("firebase-functions");
const nodemailer = require("nodemailer");
const admin = require("firebase-admin");
admin.initializeApp();

require("dotenv").config();

const transporter = nodemailer.createTransport({
  service: "Gmail",
  auth: {
    user: process.env.EMAIL_USER,
    pass: process.env.EMAIL_PASS,
  },
});

exports.sendOtp = functions.https.onCall((data, context) => {
  const email = data.email;
  const otp = Math.floor(100000 + Math.random() * 900000);

  // Save the OTP in Firestore or Realtime Database (example for Firestore)
  return admin
      .firestore()
      .collection("otps")
      .doc(email)
      .set({
        otp: otp.toString(),
        timestamp: admin.firestore.FieldValue.serverTimestamp(),
      })
      .then(() => {
        const mailOptions = {
          from: process.env.EMAIL_USER,
          to: email,
          subject: "Your OTP Code",
          text: `Your OTP code is: ${otp}`,
        };

        return transporter.sendMail(mailOptions);
      })
      .then(() => {
        return {message: "OTP sent successfully!"};
      })
      .catch((error) => {
        throw new functions.https.HttpsError("internal", error.message);
      });
});
