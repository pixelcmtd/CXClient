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

## Building
```
git clone https://github.com/chrissxYT/CXClient.git
```

Open Eclipse with `/path/to/CXClient/eclipse` as the workspace, add a new Java
Project called "Client", export that project as `Runnable Jar File`.

## Installing
Grab a build, extract the folder (called something like `cxclient_x.y`) into
your Minecraft folder's subdirectory `versions`. Depending on your OS your
Minecraft folder is different:

| OS      | Path                                      |
|---------|-------------------------------------------|
| Linux   | `~/.minecraft`                            |
| macOS   | `~/Library/Application Support/minecraft` |
| Windows | `%APPDATA%\.minecraft`                    |

If you want to use your own build, replace the JAR file inside the
folder (usually called something like `cxclient_x.y.jar`) with your own one.

## Updating
We planned an updater and now it is technically easily possible, but...we won't
do it! There are reasons, you can find them [here](UPDATER.md). For pure
updating you can just put the new folder there and change your profile in the launcher.
