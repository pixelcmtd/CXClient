package de.chrissx.iapi;

import java.util.Arrays;
import java.util.function.Consumer;

import de.chrissx.mods.CommandExecutor;
import de.chrissx.util.Util;

// TODO: somehow support checking the number of arguments ahead of calling it
public class Command implements CommandExecutor {

	private final String cmd;
	private final Consumer<String[]> handler;

	public Command(String cmd, Consumer<String[]> handler) {
		this.cmd = cmd;
		this.handler = handler;
	}

	// TODO: support for --help -H -h or something
	public static Command fromSubcommands(String cmd, Command[] subcommands) {
		return new Command(cmd, (args) -> {
			String subcmd = args[0].charAt(0) == '#' ? args[0].substring(1) : args[0];
			for(Command subcommand : subcommands)
				if(subcmd.equalsIgnoreCase(subcommand.cmd)) {
					subcommand.handler.accept(Arrays.copyOfRange(args, 1, args.length));
					return;
				}
			Util.sendError(cmd + ": Unknown subcommand: " + subcmd);
		});
	}

	@Override
	public void processCommand(String[] args) {
		handler.accept(args);
	}

	@Override
	public String argv0() {
		return cmd;
	}
}
