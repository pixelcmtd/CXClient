# CXClient - Minecraft Hacked Client
## History
|Time                    |Version|original publication and licensing                     |
|------------------------|-------|-------------------------------------------------------|
|December 2017           |1.0    |completely private                                     |
|January-February 2018   |2.0-2.1|public through MediaFire and German YouTube, obfuscated|
|February 2018           |2.2    |planned to be released, was never released             |
|July 2018               |2.3    |no more obfuscation, still no source code              |
|August 2018-January 2019|2.4-3.0|GitHub source under GPLv3                              |
|future                  |3.1+   |GitHub source under BSD 3-clause                       |
## Building
    git clone https://github.com/chrissxyt/cxclient
Open eclipse with /path/to/cxclient/eclipse as the workspace, add a new Java
Project called "Client", export that project as a Runnable Jar File.
## Installing
Grab a build, extract the folder (usually called something like "cxclient_x.x")
into your ~/.minecraft/versions/ or %APPDATA%\\.minecraft\versions\, depending
on your OS. If you want to use your own build: replace the jar file inside the
folder (usually called something like "cxclient_x.x.jar") with your own one.
## Updating
We planned an updater and now it is technically easily possible, but...we won't
do it! There are reasons, you can find them [here](UPDATER.md). For pure
updating you can just replace the old JAR or put the new folder there.
