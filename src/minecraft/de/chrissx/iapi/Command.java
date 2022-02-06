package de.chrissx.iapi;

import java.util.Arrays;
import java.util.function.Consumer;

import de.chrissx.util.Util;

// TODO: somehow support checking the number of arguments ahead of calling it
public class Command {

	public final String cmd;
	public final Consumer<String[]> handler;

	public Command(String cmd, Consumer<String[]> handler) {
		this.cmd = cmd;
		this.handler = handler;
	}

	// TODO: support for --help -H -h or something
	public static Command fromSubcommands(String cmd, Command[] subcommands) {
		return new Command(cmd, (args) -> {
			String subcmd = args[1].charAt(0) == '#' ? args[1].substring(1) : args[1];
			for(Command subcommand : subcommands)
				if(subcmd.equalsIgnoreCase(subcommand.cmd)) {
					subcommand.handler.accept(Arrays.copyOfRange(args, 1, args.length));
					return;
				}
			Util.sendError(cmd + ": Unknown subcommand: " + subcmd);
		});
	}
}
