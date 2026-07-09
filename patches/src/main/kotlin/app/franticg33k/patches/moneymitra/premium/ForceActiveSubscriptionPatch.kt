package app.franticg33k.patches.moneymitra.premium

import app.morphe.patcher.patch.rawResourcePatch
import app.franticg33k.patches.moneymitra.shared.Constants.COMPATIBILITY_MONEYMITRA

@Suppress("unused")
val forceActiveSubscriptionPatch = rawResourcePatch(
    name = "Force Active Subscription",
    description = "Patches Dart AOT-compiled libapp.so to force the active_subscription " +
        "field in the /profile response deserializer to always be true. " +
        "Replaces `ldur x1, [x29, -8]` (load parsed JSON active value) with " +
        "`add x1, x22, #0x20` (load Dart `true` object) at the StoreField " +
        "instruction in ActiveSubscription.fromJson.",
    default = true
) {
    compatibleWith(COMPATIBILITY_MONEYMITRA)

    execute {
        val libFile = get("lib/arm64-v8a/libapp.so", false)
        val bytes = libFile.readBytes()

        // ActiveSubscription.fromJson @ 0x9b4e1c
        // At offset 0x9b5158 the compiled Dart AOT code does:
        //   ldur x1, [x29, #-8]    -- load parsed `active` value from stack
        //   stur w1, [x0, #0x1b]   -- StoreField field_1b = active
        //
        // After patch: x1 always holds the Dart `true` object (x22 + 0x20)
        // instead of the server-provided boolean, so field_1b is always true.
        applyPatch(
            bytes = bytes,
            offset = 0x9b5158,
            expected = byteArrayOf(0xA1.toByte(), 0x83.toByte(), 0x5F, 0xF8.toByte()),
            replacement = byteArrayOf(0xC1.toByte(), 0x82.toByte(), 0x00, 0x91.toByte()),
            label = "activeSubscriptionActive"
        )

        libFile.writeBytes(bytes)
    }
}

private fun applyPatch(
    bytes: ByteArray,
    offset: Long,
    expected: ByteArray,
    replacement: ByteArray,
    label: String
) {
    val index = offset.toInt()
    if (index + expected.size > bytes.size) {
        error("$label patch offset 0x${offset.toString(16)} is out of bounds")
    }
    val actual = bytes.sliceArray(index until index + expected.size)
    if (!actual.contentEquals(expected)) {
        error(
            "$label patch mismatch at 0x${offset.toString(16)}: " +
            "expected ${expected.joinToString("") { "%02x".format(it) }}, " +
            "got ${actual.joinToString("") { "%02x".format(it) }}"
        )
    }
    replacement.forEachIndexed { i, byte -> bytes[index + i] = byte }
}
