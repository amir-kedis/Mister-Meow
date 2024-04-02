# Hola Amigos This file is a suggested workflow for this project only

## Commit msgs:

commit msgs should take the following format

```
<type>: <commit msg>

```

### available types:

- feat: -> for new features and additions
- fix: -> for a commit where you solve a bug or a problem
- build: -> for when you change a thing in the build process or scripts
- docs: -> for Docs (Hamda meme here)
- style: or chore: -> for cleaning and styling code
- refactor: -> for refactoring changes
- test: -> for testing processes and commits
- squashme: -> for commits you know will be squashed in the next rebase

#### examples:

```
feat: allow provided config object to extend other configs
chore: drop support for Node 6
docs: correct spelling of CHANGELOG
feat(lang): add Polish language
fix: prevent racing of requests
```

- For more info on this convention please check: [conventionalcommits](https://www.conventionalcommits.org/en/v1.0.0/)

## Considering branching:

This time I wish we could take a different approach, instead of the usual merge-base workflow.

1. You will make a new branch based on the latest main branch with the feature name (or fix)
2. You will work until you finish with the flow you want but...
3. You will first rebase main -> your-branch (if you wish here you could make to interactive rebase to make sure the commit msgs and number of commits are reasonable)
4. after you solve all the conflicts and make sure everything is OK,
5. You will fast-forward merge your branch into main.

This approach will make sure no merge commits happen in our repo.
