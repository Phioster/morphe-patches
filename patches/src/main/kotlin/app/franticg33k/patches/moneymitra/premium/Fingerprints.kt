package app.franticg33k.patches.moneymitra.premium

import app.morphe.patcher.Fingerprint

/**
 * RevenueCat EntitlementInfo.isActive() — the primary gate for all premium features.
 * Targeting the method that returns the isActive boolean field.
 */
object EntitlementInfoIsActiveFingerprint : Fingerprint(
    definingClass = "Lcom/revenuecat/purchases/EntitlementInfo;",
    name = "isActive",
    returnType = "Z",
    parameters = listOf()
)

/**
 * RevenueCat SubscriptionInfo.isActive() — used to check individual subscription
 * active status. Provides defense-in-depth for subscription-level checks.
 */
object SubscriptionInfoIsActiveFingerprint : Fingerprint(
    definingClass = "Lcom/revenuecat/purchases/SubscriptionInfo;",
    name = "isActive",
    returnType = "Z",
    parameters = listOf()
)
