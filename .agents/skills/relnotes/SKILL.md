---
name: relnotes
description: Create release notes for a specific CLI Assured release version.
Usage: /relnotes <version> (e.g., /relnotes 3.33.2)
  or /relnotes <version> <previous-version> (e.g., /relnotes 3.35.0 3.33.1)
---

# Release Notes Skill

Create release notes for a CLI Assured release.

The version number is passed as the first non-optional argument (e.g., `3.33.2`).

## Steps

### 1. Parse the version

Extract the version number from the first (required) argument. It must be a semantic version like `3.33.2` following the naming scheme `<major>.<minor>.<micro or patch>`.

### 1.1 Determine the previous version

If previous version is specified via the second command argument, use that one.

Otherwise determine the previous version by looking at existing git tags on the branch of the release notes version.
The name of the branch is either `main` for release notes versions ending with `.0` or `<major>.<minor>` for Long Term
Support (LTS) branches).
Only tags matching `<major>.<minor>.<micro>`, where all of `<major>`, `<minor>` and `<micro>` must be numeric,
are relevant for release notes other tags can be ignored.

If the requested release notes version does not end with `.0`, the previous version is the tag immediately before
the requested version in sorted order.

Use:

```bash
git tag --list '<major>.<minor>.*' | sort -V
```

If the requested version ends with `.0`, the previous version is the highest patch of the previous minor,
e.g., for release notes version `3.34.0`, find the latest `3.33.x` tag, or the latest `3.32.x` and so on.

Verify that the requested version tag exists in git:

```bash
git tag --list '<version>'
```

If you cannot find and suitable previous version, abort and inform the user.

### 2. Gather changes

Run these commands to understand what changed:

```bash
git log --oneline <previous-version>...<version>
```

Look at the commit messages for:
- Dependency upgrades, but consider only runtime and provided dependencies; ignore test dependencies
- New features
- Bug fixes
- Deprecations
- Breaking changes
- References to GitHub issues/PRs (patterns like `#1234`, `fixes #1234`, etc.)

### 3. Consult GitHub issues and PRs

For each GitHub issue or PR number referenced in commits, fetch details using:

```bash
gh issue view <number> --repo cli-assured/cli-assured
gh pr view <number> --repo cli-assured/cli-assured
```

Also list PRs merged between the two tags:

```bash
gh pr list --repo cli-assured/cli-assured --state merged --search "merged:$(git log -1 --format=%ci <previous-version> | cut -d' ' -f1)..$(git log -1 --format=%ci <version> | cut -d' ' -f1)" --limit 100
```

### 4. Check existing release notes for style and structure of the document

The format follows these conventions:

- **Title**: `= CLI Assured <version> release notes`
- **Sections** (include only those that apply):
  - `== Important dependency upgrades` — bullet list of upgraded dependencies. Each upgrade should contain:
    - A link to release notes
      - Look into older release notes under `docs/modules/ROOT/pages/release-notes/` to figure out where the given
        project publishes their release notes and try to find the release notes for the version we upgraded to.
      - If needed you can also search on the internet for the specific dependency release
    - A link to changelog in, typically `https://github.com/<org>/<project>/compare/<old-version>+++...+++<new-version>`
    - If there were security vulnerabilities fixed in the given dependency, list them along with links to the CVE database.
  - `== Enhancements` — for new features or enhancements, each as a `===` subsection. Link GitHub issues in the heading like `=== https://github.com/cli-assured/cli-assured/issues/<issue-number>[#issue-number] <issue-title>`
    - Enhancements may also include changes in JavaDoc or Antora documentation in `docs` folder.
  - `== Bugfixes` — bug fixes, each as a `===` subsection with issue links
  - `== Deprecations` — deprecated features
  - `== Breaking changes` — if any
- **Footer**: Always end with:
  ```
  == Full changelog

  https://github.com/cli-assured/cli-assured/compare/<previous-version>+++...+++<version>
  ```

- If there are no user-facing changes, use a minimal format:
  ```
  = CLI Assured <version> release notes

  There are no end user facing changes in this release.

  == Full changelog

  https://github.com/cli-assured/cli-assured/compare/<previous-version>+++...+++<version>
  ```

- When describing behavior changes, use the pattern: "Before CLI Assured <version>, ... Since CLI Assured <version>, ..."
- Link configuration options using xref syntax: `xref:reference/extensions/quarkus-cxf.adoc#quarkus-cxf_quarkus-cxf-...[quarkus.cxf....]`
- Credit contributors with `Special thanks to https://github.com/<user>[@<user>]`

### 5. Write the release notes file

Write the file to `docs/modules/ROOT/pages/release-notes/<version>.adoc`.
Do not ask the user whether the file can be created or updated, just create and/or update the file however you need.

### 6. Update nav.adoc

Edit `docs/modules/ROOT/nav.adoc`. Add a new entry in the release notes section, maintaining version-descending order.
The entry goes after the `ifeval::[{doc-is-main} == true]` line, among the other `** xref:release-notes/...` entries.

Format: `** xref:release-notes/<version>.adoc[<version>]`

Insert it in the correct position to maintain descending version order.

### 7. Update index.adoc

Edit `docs/modules/ROOT/pages/release-notes/index.adoc`. Add a new list item, maintaining version-descending order within the appropriate minor version group.

Format: `| xref:release-notes/<version>.adoc[<version>] | <date in YYYY-MM-DD format, when the release was tagged>`

- Group the entry with other releases of the same minor version (e.g., 3.33.x entries are together)

### 9. Commit the changes

Checkout a new topic branch unless the current active branch'es name ends with `-release-notes`.

Use the following command to checkout the new topic branch if needed:

```bash
git checkout -b "$(date +%y%m%d)-release-notes<version>"
```

Commit the changes using:

```bash
git add -A
git commit -m "<version> release notes"
```
