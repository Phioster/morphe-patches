package app.franticg33k.patches.moneymitra.premium

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.franticg33k.patches.moneymitra.shared.Constants.COMPATIBILITY_MONEYMITRA

private const val TRUE_RETURN = """
    const/4 v0, 0x1
    return v0
"""

@Suppress("unused")
val unlockMoneyMitraPremiumPatch = bytecodePatch(
    name = "Unlock Premium",
    description = "Unlocks all premium features in MoneyMitra by forcing RevenueCat's " +
        "EntitlementInfo.isActive() and DateActive.isActive() to always return true. " +
        "This removes paywalls and enables premium course access without a subscription.",
    default = true
) {
    compatibleWith(COMPATIBILITY_MONEYMITRA)

    execute {
        EntitlementInfoIsActiveFingerprint.method.addInstructions(0, TRUE_RETURN)
        DateActiveIsActiveFingerprint.method.addInstructions(0, TRUE_RETURN)
    }
}
