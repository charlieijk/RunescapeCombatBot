# Self-Hosted Remote Setup

To avoid pushing to GitHub, this repo includes a local bare remote that acts like your own “server”.

## Create (already done here)

```bash
mkdir -p .remotes
git init --bare .remotes/selfhost.git
git remote add selfhost .remotes/selfhost.git
```

## Push branches

```bash
GIT_DIR=.gitdata GIT_WORK_TREE=. git push selfhost master
GIT_DIR=.gitdata GIT_WORK_TREE=. git push selfhost develop
```

You can add feature/release/hotfix branches the same way. This keeps everything offline.

## Clone elsewhere

On another machine, copy `.remotes/selfhost.git` and run `git clone /path/to/selfhost.git RunescapeCombatBot` to avoid GitHub entirely.

Feel free to rename the `.remotes` folder or host it on NAS/USB when you want a portable “server”.
