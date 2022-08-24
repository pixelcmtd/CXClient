# Hacking on CXClient

First of all, thank you for your will to hack on Minecraft, we need more people
like you. Secondly, before reading this, please consider writing an iAPI or eAPI
plugin first. While both are still not finished and might be cancelled or broken
randomly, they are the proper way to do small extensions. If you, however,
intend to change a lot about the internals, or just want to maintain a fork for
some other reason, you should read further.

## Building

First of all, you will need to be able to build CXClient. For that you need to
get the code and run [`ant`](https://ant.apache.org/), for example like this:

```sh
git clone https://github.com/pixelcmtd/CXClient.git
cd CXClient
ant
```

## Basic Editing

You are free to use whatever editor you like: `vi`, `emacs`, `idc`.

However, the [Eclipse IDE](https://www.eclipse.org/eclipseide/) is officially
supported. Open it up, select `/path/to/CXClient/eclipse` as the workspace, and
add the two Java Projects `Client` and `iapiTestPlugin`.

Now you're all set up for success! You can do some basic edits now.

<!--TODO: implementation details-->
