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
 * RevenueCat DateActive.isActive() — used internally to determine if a date-based
 * entitlement is still in its active period. Patching this provides defense-in-depth.
 */
object DateActiveIsActiveFingerprint : Fingerprint(
    definingClass = "Lcom/revenuecat/purchases/utils/DateActive;",
    name = "isActive",
    returnType = "Z",
    parameters = listOf()
)
