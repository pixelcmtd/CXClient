# CXClient - Minecraft Hacked Client
## History
| Time                        | Version | original publication and licensing                      |
|-----------------------------|---------|---------------------------------------------------------|
| December 2017               | 1.0     | completely private                                      |
| January-February 2018       | 2.0-2.1 | public through MediaFire and German YouTube, obfuscated |
| February 2018               | 2.2     | never released                                          |
| July 2018                   | 2.3     | no more obfuscation, still no source code               |
| August 2018-January 2019    | 2.4-3.0 | GitHub source under GPLv3                               |
| September 2019-October 2020 | 3.1-3.2 | GitHub source under BSD 3-clause                        |

## Installing
Grab a build, extract the folder (called something like `cxclient_x.y`) into
your Minecraft folder's subdirectory `versions`. Depending on your OS your
Minecraft folder is different:

| OS      | Path                                      |
|---------|-------------------------------------------|
| Linux   | `~/.minecraft`                            |
| macOS   | `~/Library/Application Support/minecraft` |
| Windows | `%APPDATA%\.minecraft`                    |

## Updating
We planned an updater and now it is technically easily possible, but...we won't
do it! There are reasons, you can find them [here](UPDATER.md). For pure
updating you can just put the new folder there and change your profile in the launcher.

## Extending CXClient

There are currently two ways of extending CXClient in development: The iAPI and the eAPI.

The iAPI is for traditional "addons"/"plugins", JARs from the subdirectory
`cxclient_addons` of the Minecraft directory are automatically loaded on startup. In a
scheme that is not stable yet, those addons are then initialized so they can hook
themselves into the core of CXClient.

The eAPI rather is for extending CXClient externally, i.e. from an external program
written in any programming language doing anything. It is even less done than the iAPI,
but progress is quite quick right now. Previous development versions of it used a
directory structure with ridiculous file I/O usage, while the current one is very similar
to what Vanilla Minecraft servers do: It allows external programs to connect to an RCON
server that then executes the given commands.

## Custom Builds and Hacking
For instructions on how to build CXClient yourself or hack on it, you can read the
[according document](HACKING.md).
