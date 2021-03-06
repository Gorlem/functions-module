package com.ddoerr.modules.functions.actions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.eq2online.macros.scripting.api.IMacroAction;
import net.eq2online.macros.scripting.parser.ScriptCore;

public class FunctionState {
	public static class Argument {
		public static Argument Invalid = new Argument(null, null);

		private static String variablePattern = "([&#]?(?:[a-z](?:[a-z0-9_\\-]*)))";
		
		private static Pattern scalarPattern = Pattern.compile("^" + variablePattern + "($|,)");
		private static Pattern scalarDefaultPattern = Pattern.compile("^" + variablePattern + "=(.+?)($|,)");

		private static Pattern arrayPattern = Pattern.compile("^(\\.\\.\\.)?" + variablePattern + "\\[\\]($|,)");
		private static Pattern arrayDefaultPattern = Pattern.compile("^(\\.\\.\\.)?" + variablePattern + "\\[\\]=\\[(.+?)\\]?($|,)");
		
		private final String name;
		private final boolean isArray;
		private final Object defaultValue;
		private final boolean isCatchAll;
		
		public Argument(String name, String defaultValue) {
			this.name = name;
			this.isArray = false;
			this.defaultValue = defaultValue;
			this.isCatchAll = false;
		}
		
		public Argument(String name, boolean isCatchAll, String[] defaultValue) {
			this.name = name;
			this.isArray = true;
			this.defaultValue = defaultValue;
			this.isCatchAll = isCatchAll;
		}
		
		

		public String getName() {
			return name;
		}

		public Object getDefaultValue() {
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
		
		public boolean isCatchAll() {
			return isCatchAll;
		}
		
		public static List<Argument> tokenize(String input) {
			int index = input.indexOf(',');
			
			if (index == -1) {
				return Collections.emptyList();
			}
			
			List<Argument> arguments = new ArrayList<Argument>();
			input = input.substring(index + 1);
			
			while (!input.isEmpty()) {
				Matcher scalarMatcher = scalarPattern.matcher(input);
				
				if (scalarMatcher.find()) {
					arguments.add(new Argument(scalarMatcher.group(1), null));
					input = input.substring(scalarMatcher.end());
					continue;
				}
				
				Matcher scalarDefaultMatcher = scalarDefaultPattern.matcher(input);
				
				if (scalarDefaultMatcher.find()) {
					String[] tokenized = ScriptCore.tokenize(scalarDefaultMatcher.group(2), (char)0, '"', '"', '\\', new StringBuilder());
					arguments.add(new Argument(scalarDefaultMatcher.group(1), tokenized[0]));
					input = input.substring(scalarDefaultMatcher.end());
					continue;
				}
				
				Matcher arrayMatcher = arrayPattern.matcher(input);
				
				if (arrayMatcher.find()) {
					arguments.add(new Argument(arrayMatcher.group(2), arrayMatcher.group(1) != null, null));
					input = input.substring(arrayMatcher.end());
					continue;
				}
				
				Matcher arrayDefaultMatcher = arrayDefaultPattern.matcher(input);
				
				if (arrayDefaultMatcher.find()) {
					String[] tokenized = ScriptCore.tokenize(arrayDefaultMatcher.group(3), ',', '"', '"', '\\', new StringBuilder());
					arguments.add(new Argument(arrayDefaultMatcher.group(2), arrayDefaultMatcher.group(1) != null, tokenized));
					input = input.substring(arrayDefaultMatcher.end());
					continue;

				}
				
				arguments.add(Invalid);
				
				index = input.indexOf(',');
				if (index == -1) {
					break;
				}
				
				input = input.substring(index + 1);
			}
			
			return arguments;
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