package com.ddoerr.modules.functions;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

import net.eq2online.macros.scripting.Variable;
import net.eq2online.macros.scripting.api.ICounterProvider;
import net.eq2online.macros.scripting.api.IFlagProvider;
import net.eq2online.macros.scripting.api.IStringProvider;
import net.eq2online.macros.scripting.variable.VariableProviderArray;

public class FunctionVariableProvider extends VariableProviderArray {
	
	private Map<String, Boolean> flags = new HashMap<>();
	private Map<String, Integer> counters = new HashMap<>();
	private Map<String, String> strings = new HashMap<>();

	@Override
	public Object getVariable(String variableName) {
		Matcher variableMatcher = Variable.variableNamePattern.matcher(variableName);
		
		if (variableMatcher.matches()) {			
			String type = variableMatcher.group(2);
			String name = variableMatcher.group(3);
			String arrayIndex = variableMatcher.group(6);
			
			int index = arrayIndex == null ? MISSING : Integer.parseInt(arrayIndex);
			
			if (Variable.PREFIX_STRING.equals(type)) {
				return getString(name, index);
			} else if (Variable.PREFIX_INT.equals(type)) {
				return getCounter(name, index);
			} else if (Variable.PREFIX_BOOL.equals(type)) {
				return getFlag(name, index);
			}
		}
		
		return null;
	}

	@Override
	public String getString(String stringName) {
		return strings.getOrDefault(stringName, IStringProvider.EMPTY);
	}

	@Override
	public void setString(String stringName, String value) {
		strings.put(stringName, value);
	}

	@Override
	public void unsetString(String stringName) {
		strings.remove(stringName);
	}

	@Override
	public int getCounter(String counter) {
		return counters.getOrDefault(counter, ICounterProvider.EMPTY);
	}


	@Override
	public void setCounter(String counter, int value) {
		counters.put(counter, value);
	}


	@Override
	public void unsetCounter(String counter) {
		counters.remove(counter);
	}


	@Override
	public void incrementCounter(String counter, int increment) {
		setCounter(counter, getCounter(counter) + increment);
	}

	@Override
	public void decrementCounter(String counter, int decrement) {
		incrementCounter(counter, -1 * decrement);
	}


	@Override
	public boolean getFlag(String flag) {
		return flags.getOrDefault(flag, IFlagProvider.EMPTY);
	}

	@Override
	public void setFlag(String flag, boolean value) {
		flags.put(flag, value);
	}
	

	@Override
	public void setFlag(String flag) {
		setFlag(flag, true);
	}

	@Override
	public void unsetFlag(String flag) {
		flags.remove(flag);
	}

	@Override
	public void updateVariables(boolean clock) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onInit() {
		// TODO Auto-generated method stub
		
	}

}
