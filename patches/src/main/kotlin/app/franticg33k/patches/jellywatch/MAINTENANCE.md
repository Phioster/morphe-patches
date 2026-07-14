# Updating the JellyWatch patch after an app update

The "Unlock Premium" patch matches the shop's purchase-manager class and its
state-flow helper by their **R8-obfuscated names**. R8 reassigns those names on
almost every app build, so when JellyWatch updates the patch fails with:

```
PatchException: Failed to match the fingerprint: ShopPurchaseManagerIsOwnedFingerprint
```

All the *cleartext* fingerprints (PremiumStatus, BillingRepository, Pairip,
ShopViewModel) keep matching — only the three obfuscated references break. This
is the same fragility franticg33k documents for e.g. the Hamro Patro patch
("obfuscated helper types pinned to vX.Y.Z"); the fix is to re-pin the names,
not to rewrite the matching.

## Names that need re-pinning

| Meaning | Where it lives | Value for 2.0.REV-1710 |
|---|---|---|
| ShopPurchaseManager class | `Fingerprints.kt` + injected smali | `Lyu8;` |
| owned-items flow field **type** | injected smali | `Lnha;` |
| flow compareAndSet setter | injected smali | `Lnha;->p(Object,Object)Z` |

Field names (`a` = SharedPreferences, `b` = owned-items flow) and method
signatures (`a(String)Z` = isOwned, `d(Set)V` = replaceOwned) have been stable
so far — but verify them too.

## How to re-derive the names (~5 min)

1. Pull the installed base.apk from a device:
   ```
   adb shell pm path com.jellywatch.app        # find the base.apk path
   adb pull <that path>/base.apk
   ```
2. Extract the dex files and disassemble with baksmali:
   ```
   unzip -o base.apk 'classes*.dex' -d dex
   java -jar baksmali-2.5.2.jar disassemble dex/classes3.dex -o smali
   ```
   (The purchase manager has been in `classes3.dex`; if not, disassemble the
   others and grep across all of them.)
3. Find the purchase-manager class — it's the one that writes the
   `"owned_items"` SharedPreferences key:
   ```
   grep -rl '"owned_items"' smali        # -> e.g. smali/yu8.smali  (the class)
   ```
4. Open that class and read off:
   - the field of type `Landroid/content/SharedPreferences;`  (field `a`)
   - the owned-items flow field and **its type** (field `b`, type `Lnha;`)
   - the method returning `Z` taking one `String`  (isOwned, `a`)
   - the method returning `V` taking one `Set`  (replaceOwned, `d`)
   - inside `d`, the write-back call `->getValue()` / the compareAndSet setter
     on the flow type (was `Lnha;->p(Object,Object)Z`)
5. Confirm `ShopViewModel.purchaseManager`'s type matches the new class name:
   ```
   grep -n 'purchaseManager' smali/com/jellywatch/app/shop/ShopViewModel.smali
   ```
6. Replace the old values with the new ones in
   `patches/.../jellywatch/premium/Fingerprints.kt` and
   `patches/.../jellywatch/premium/UnlockPremiumPatch.kt`, then rebuild.

## Optional: validate the injected smali before building

Assemble the two smali blocks with the smali assembler (counterpart to
baksmali) to catch typos and register errors without a full Gradle build.
