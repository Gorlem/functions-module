package com.ddoerr.modules.functions.actions;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.eq2online.macros.scripting.api.IMacroAction;
import net.eq2online.macros.scripting.parser.ScriptCore;

public class FunctionState {
	public static class Argument {
		public static Argument Invalid = new Argument(null, false);
		
		private static Pattern defaultPattern = Pattern.compile("^([&#]?(?:[a-z](?:[a-z0-9_\\-]*)))=(.+)$");
		private static Pattern arrayPattern = Pattern.compile("^([&#]?([a-z]([a-z0-9_\\\\-]*)))\\[\\]$");
		private static Pattern scalarPattern = Pattern.compile("^([&#]?([a-z]([a-z0-9_\\\\-]*)))$");
		
		private final String name;
		private final boolean isArray;
		private final String defaultValue;
		
		public Argument(String name, boolean isArray) {
			this.name = name;
			this.isArray = isArray;
			this.defaultValue = null;
		}
		
		public Argument(String name, boolean isArray, String defaultValue) {
			this.name = name;
			this.isArray = isArray;
			this.defaultValue = defaultValue;
		}

		public String getName() {
			return name;
		}

		public String getDefaultValue() {
			return defaultValue;
		}
		
		public boolean isArray() {
			return isArray;
		}
		
		public boolean isValid() {
			return name != null;
		}
		
		public boolean hasDefaultValue() {
			return defaultValue != null;
		}
		
		public static Argument parse(String input) {
			Matcher scalarMatcher = scalarPattern.matcher(input);
			
			if (scalarMatcher.matches()) {
				return new Argument(scalarMatcher.group(1), false);
			}
			
			Matcher arrayMatcher = arrayPattern.matcher(input);
			
			if (arrayMatcher.matches()) {
				return new Argument(arrayMatcher.group(1), true);
			}
			
			Matcher defaultMatcher = defaultPattern.matcher(input);
			
			if (defaultMatcher.matches()) {
				String[] tokenized = ScriptCore.tokenize(defaultMatcher.group(2), (char)0, '"', '"', '\\', new StringBuilder());
				return new Argument(defaultMatcher.group(1), false, tokenized[0]);
			}
			
			return Invalid;
		}
	}
	
	private final List<IMacroAction> actions;
	private final List<Argument> arguments;
	
	public FunctionState(List<IMacroAction> actions, List<Argument> arguments) {
		this.actions = actions;
		this.arguments = arguments;
	}
	
	public List<IMacroAction> getActions() {
		return actions;
	}
	
	public List<Argument> getArguments() {
		return arguments;
	}
}