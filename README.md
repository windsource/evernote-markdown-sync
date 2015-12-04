# Evernote Markdown Sync

## Overview

I love [Evernote](https://evernote.com) as it syncs my notes between all my devices. I also love Markdown as it is easy to write and also supports syntax highlighting which is not supported by Evernote. Thirdly I love Git for keeping track of changes to my work.

So I searched for a way to write my notes as Markdown files, store them in git and sync that repository to my Evernote account automatically whenever I change a Markdown file and commit it in Git.

## Requirements

1. Git must be installed on the machine
2. You must get a [developer token for your Evernote account](https://dev.evernote.com/doc/articles/dev_tokens.php)
3. Java must be installed on the machine

## Build

On Linux / Mac
```bash
./gradlew clean build
```

on Windows
```bash
gradlew clean build
```

The resulting jar will be in `build/libs/` afterwards.

## Usage

```
usage: java -jar evernote-markdown-sync-<version>.jar -p <PATH> -t <TOKEN>
 -f           force update of notes in Evernote even if note in Git did
              not change
 -p <PATH>    path to markdown documents inside git repository
 -s           use Evernote sandbox
 -t <TOKEN>   Evernote token used to access account
```

When running Evernote Markdown Sync for the first time every note is created in Evernote. Subsequent runs will update only the notes in Evernote that were changed in the Git repository.

## Architecture

Fortunately Evernote provides an API and also SDKs for various languages. This tool uses the Java SDK.
For Markdown conversion [pegdown](https://github.com/sirthias/pegdown) is used. Styles are converted to be inline using [CSSBox](http://cssbox.sourceforge.net).

## Credits

The style sheet was taken from [Markdown Here](https://github.com/adam-p/markdown-here) which is a great tool to turn Markdown to HTML when using web browser input fields.





