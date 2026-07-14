# Phioster's JellyWatch Patches

A personal fork focused on a single app: **JellyWatch** (`com.jellywatch.app`).
Built for use with [Morphe](https://morphe.software).

> This is a **fork** of [**franticg33k/morphe-patches**](https://github.com/franticg33k/morphe-patches).
> All of the original patch design and the bulk of this codebase are
> franticg33k's work — full credit to them. This fork only trims the project
> down to JellyWatch and keeps that one patch working on current app versions.

## Patches

<!-- PATCHES_START EXPANDED -->
<details open>
<summary>📦 JellyWatch&nbsp;&nbsp;•&nbsp;&nbsp;2 patches</summary>
<br>

| 💊&nbsp;Patch | 📜&nbsp;Description |
|----------|----------------|
| Unlock Premium | Unlocks all premium features and shop items in JellyWatch (local `isPremium` / shop-ownership overrides). Verified on `2.0.REV-1710`. |
| Remove License Activity | Removes the PairIP LicenseActivity from AndroidManifest.xml. |

</details>
<!-- PATCHES_END -->

> **Note:** "Admin Premium" / Watch Pass (unlimited user slots) is **not** unlocked
> by these patches — it is verified server-side via JellyWatch's backend license
> API (`/api/v2/license/slots/...`) plus Play Integrity, so it cannot be flipped
> with a local patch.

## What this fork changes (vs. upstream)

- **Trimmed to JellyWatch only** — removed the patches for all other apps
  (atlasphoto, byair, hamropatro, karobar, nepalipatro, prismatica, providelite).
- **Fixed the JellyWatch "Unlock Premium" patch for app version `2.0.REV-1710`.**
  R8 had renamed the shop's purchase-manager class and its state-flow helper, so
  three obfuscated references no longer matched (`Lav8;`→`Lyu8;`,
  `Loha;`→`Lnha;`, setter `o(...)`→`p(...)`). Names re-derived from the actual
  installed APK.
- **Added a maintenance note** describing how to re-pin those obfuscated names
  after future app updates: `patches/.../jellywatch/MAINTENANCE.md`.
- **Added a manual build workflow** (`.github/workflows/build-bundle.yml`) that
  builds the `.mpp` bundle and uploads it as an artifact.

## Usage

Add this repo as a patch source in Morphe:

```
https://github.com/Phioster/morphe-patches
```

Or click: https://morphe.software/add-source?github=Phioster/morphe-patches

Then select JellyWatch and apply "Unlock Premium".

Releases are published by the **Publish release** GitHub Actions workflow
(`workflow_dispatch`), which builds the `.mpp`, attaches it to a GitHub release,
and points `patches-bundle.json` at it.

## Credits

- **Original author:** [franticg33k](https://github.com/franticg33k) —
  [franticg33k/morphe-patches](https://github.com/franticg33k/morphe-patches).
- byAir patches (in upstream) based on
  [early.egg3707/ee-morphe-patches](https://gitlab.com/early.egg3707/ee-morphe-patches).
- Fork maintained by [Phioster](https://github.com/Phioster).

## License

GPLv3 (unchanged from upstream). See [LICENSE](LICENSE) and [NOTICE](NOTICE).
