package app.franticg33k.patches.jellywatch.shared

import app.morphe.patcher.patch.ApkFileType
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

object Constants {
    // Version-locked: the Unlock Premium patch relies on R8-obfuscated names
    // (Lyu8;/Lnha;) that are only valid for this exact app version. Without a
    // target, Morphe would show "Any" and let users patch versions that crash.
    val COMPATIBILITY_JELLYWATCH = Compatibility(
        name = "JellyWatch",
        packageName = "com.jellywatch.app",
        apkFileType = ApkFileType.APK,
        appIconColor = 0x008577,
        targets = listOf(
            AppTarget("2.0.REV-1710", false, null),
        ),
    )
}
