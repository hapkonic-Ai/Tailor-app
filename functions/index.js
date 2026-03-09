/**
 * Firebase Cloud Functions — Tailor App
 *
 * setUserRole: Sets a custom JWT claim ("role": "admin" | "tailor") on a given Firebase user.
 * Only callable by existing admins (verified via their own JWT role claim).
 *
 * Deploy: firebase deploy --only functions
 * Requires: firebase-admin, firebase-functions packages (see package.json)
 */

const functions = require("firebase-functions");
const admin = require("firebase-admin");

admin.initializeApp();

/**
 * Callable Cloud Function — sets the role custom claim for a target user.
 *
 * Request data:
 *   { uid: string, role: "admin" | "tailor" }
 *
 * Caller must be signed in as an admin (role claim === "admin").
 */
exports.setUserRole = functions.https.onCall(async (data, context) => {
  // 1. Require the caller to be authenticated
  if (!context.auth) {
    throw new functions.https.HttpsError(
      "unauthenticated",
      "You must be signed in to assign roles."
    );
  }

  // 2. Require the caller to have the "admin" role
  const callerRole = context.auth.token.role;
  if (callerRole !== "admin") {
    throw new functions.https.HttpsError(
      "permission-denied",
      "Only admins can assign roles."
    );
  }

  // 3. Validate input
  const { uid, role } = data;
  if (!uid || typeof uid !== "string") {
    throw new functions.https.HttpsError("invalid-argument", "uid is required.");
  }
  if (role !== "admin" && role !== "tailor") {
    throw new functions.https.HttpsError(
      "invalid-argument",
      'role must be "admin" or "tailor".'
    );
  }

  // 4. Set the custom claim
  await admin.auth().setCustomUserClaims(uid, { role });

  return { success: true, uid, role };
});
