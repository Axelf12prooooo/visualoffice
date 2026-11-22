# VisualOffice

Suite bureautique + environnement de développement pour Android (Kotlin, Jetpack Compose, Material 3, MVVM, Room, DataStore).

## État actuel : phases 1 et 2 (première version)

> Ce code **n'a pas encore été compilé** : l'environnement où il a été écrit n'a ni Android SDK ni accès aux dépôts Google/Maven. La première compilation réelle sera celle de GitHub Actions ; il est possible qu'elle révèle des erreurs à corriger.

**Fonctionnel (à vérifier à la première compilation)**
- Thème sombre/clair/système (palette du cahier des charges), persistant (DataStore).
- Navigation : barre basse sur smartphone, barre latérale sur tablette (≥ 600 dp), Paramètres via l'engrenage.
- Accueil : logo, bienvenue, accès rapide aux modules, fichiers récents (Room).
- « Ouvrir un fichier » via le Storage Access Framework (aucune permission de stockage), avec aperçu des fichiers texte/code/Markdown/CSV (128 Ko max).
- Test unitaire sur la détection de type de fichier.

**Pas encore développé** (écrans qui l'indiquent honnêtement) : VisualWriter, VisualSheets, VisualSlides, VisualCode, terminal, projets, explorateur complet. « Nouveau document » et « Nouveau projet » sont désactivés en attendant.

## Structure

```
app/src/main/java/app/visualoffice/
  core/       utilitaires partagés
  data/       Room (db/) et DataStore (settings/)
  domain/     modèles purs (FileKind)
  features/   home, preview, settings (puis writer, sheets, slides, code, projects, explorer)
  ui/         theme, navigation, components
.github/workflows/android-build.yml
```

## Publier sur GitHub

```bash
cd VisualOffice
git init -b main
git add .
git commit -m "VisualOffice : phases 1 et 2"
git remote add origin https://github.com/<votre-compte>/VisualOffice.git
git push -u origin main
```

Dès le push, l'onglet **Actions** lance « Android Build ». Le job `debug` publie l'APK dans **Artifacts** (`VisualOffice-debug-apk`) : téléchargez le zip, extrayez l'APK et installez-le sur votre téléphone (autorisez les sources inconnues). Aucun secret n'est nécessaire.

Le workflow utilise `gradle/actions/setup-gradle` avec Gradle 8.9 : le Gradle Wrapper n'est pas requis. Pour en ajouter un en local : `gradle wrapper --gradle-version 8.9`.

## APK release signé

1. Créer la clé (une seule fois, à conserver précieusement hors du dépôt) :
   ```bash
   keytool -genkeypair -v -keystore visualoffice-release.keystore \
     -alias visualoffice -keyalg RSA -keysize 4096 -validity 10000
   ```
2. Encoder en Base64 :
   - Linux : `base64 -w0 visualoffice-release.keystore > keystore.b64`
   - macOS : `base64 -i visualoffice-release.keystore -o keystore.b64`
   - Windows (PowerShell) : `[Convert]::ToBase64String([IO.File]::ReadAllBytes("visualoffice-release.keystore")) | Set-Content keystore.b64`
3. Dans GitHub : **Settings → Secrets and variables → Actions → New repository secret**, créer :
   `KEYSTORE_BASE64` (contenu de `keystore.b64`), `KEYSTORE_PASSWORD`, `KEY_ALIAS` (`visualoffice` dans l'exemple), `KEY_PASSWORD`.
4. Au prochain push sur `main`, le job `release` produit `VisualOffice-release-apk`. Sans les 4 secrets, il s'ignore sans échouer.

Si vous perdez le keystore, vous ne pourrez plus mettre à jour l'application installée : sauvegardez-le.

## GitHub Release

```bash
git tag v0.1.0
git push origin v0.1.0
```
Si les secrets sont configurés, l'APK signé est joint automatiquement à la Release.

## Feuille de route

3 Explorateur de fichiers · 4 VisualWriter · 5 VisualCode, terminal, projets · 6 VisualSheets · 7 VisualSlides · 8 Paramètres complets (accent, police, langue, stockage) · 9 Tests · 10 Finitions CI.
