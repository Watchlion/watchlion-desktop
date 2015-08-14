# Concept

Watchlion manage installed applications.

It is usefull if you own several computers (a desktop and a portable for exemple) and you want to maintain the same applications on each.

Watchlion works like that:
- A file, called Reference, list all wanted applications. It is the same for all your computers. For me, this file is in a shared repository on Dropbox, accessible from each of my computers
- Another file, called Local, list the applications installed on a given computer. This file is different for each computer. By default, Watchlion create this file in your HOME directory on each computer.


# Usage

The application is divided into 4 parts (4 buttons on the top):

1. "Installed" list the applications set as installed in Watchlion
1. "Missing" list applications set as not installed (in Local) but wanted (in Reference)
1. "All" list both of them
1. "Explore" list items in wanted directoris (for exemple "Program Files" in Windows or "/Applications" for Mac OS) in order to create your Reference.

Do not forget to clic on "Save" after each modification.


# Install

To launch the App:

1. `git clone https://github.com/Watchlion/watchlion-desktop.git` or "Download ZIP" from project homepage on Github.com
1. `mvn install`
1. Go to `resources/scripts` and launch the right watchlion bash file

You can also launch by yourself:

```
java -jar watchlion-0.0.1-SNAPSHOT-executable.jar /
-reference "\path\to\Dropbox\watchlion\watchlion.reference.json" /
-local "\path\to\HOME\watchlion\watchlion.local.json" /
-report
```
With this informations:

- Local can not exist or be empty... Then Save when application appears to generate it.
- `report argument (optional) triggers console report instead of GUI app.
- Launch app without parameter to have the help / usage.

Both Reference and Local JSON files are backuped as new files near their original, with a timestamp in the backup name.  
