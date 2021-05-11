package com.ddoerr.modules.functions;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.eq2online.macros.scripting.Variable;
import net.eq2online.macros.scripting.api.ICounterProvider;
import net.eq2online.macros.scripting.api.IFlagProvider;
import net.eq2online.macros.scripting.api.IMutableArrayProvider;
import net.eq2online.macros.scripting.api.IStringProvider;

public class FunctionVariableProvider
		implements IFlagProvider, ICounterProvider, IStringProvider, IMutableArrayProvider {

	private Map<String, Boolean> flags = new HashMap<>();
	private Map<String, Integer> counters = new HashMap<>();
	private Map<String, String> strings = new HashMap<>();
	
	private Map<String, Boolean[]> flagArrays = new HashMap<>();
	private Map<String, Integer[]> counterArrays = new HashMap<>();
	private Map<String, String[]> stringArrays = new HashMap<>();
	
	@Override
	public int indexOf(String arrayName, String value, boolean caseSensitive) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMaxArrayIndex(String arrayName) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean checkArrayExists(String arrayName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object getArrayVariableValue(String variableName, int offset) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateVariables(boolean clock) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getVariable(String variableName) {
		System.out.println(variableName);
		if (variableName == null) {
			return null;
		} else if (variableName.startsWith(Variable.PREFIX_STRING)) {
			return getString(variableName.substring(1));
		} else if (variableName.startsWith(Variable.PREFIX_INT)) {
			return getCounter(variableName.substring(1));
		} else {
			return getFlag(variableName);
		}
	}

	@Override
	public Set<String> getVariables() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onInit() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean push(String arrayName, String value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String pop(String arrayName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean put(String arrayName, String value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void delete(String arrayName, int offset) {
		// TODO Auto-generated method stub

	}

	@Override
	public void clear(String arrayName) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getString(String stringName) {
		return strings.getOrDefault(stringName, IStringProvider.EMPTY);
	}

	@Override
	public String getString(String stringName, int offset) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setString(String stringName, String value) {
		strings.put(stringName, value);
	}

	@Override
	public void setString(String stringName, int offset, String value) {
		if (offset < 0) {
			setString(stringName, value);
		}
	}

	@Override
	public void unsetString(String stringName) {
		strings.remove(stringName);
	}

	@Override
	public void unsetString(String stringName, int offset) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getCounter(String counter) {
		return counters.getOrDefault(counter, ICounterProvider.EMPTY);
	}

	@Override
	public int getCounter(String counter, int offset) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setCounter(String counter, int value) {
		counters.put(counter, value);
	}

	@Override
	public void setCounter(String counter, int offset, int value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void unsetCounter(String counter) {
		counters.remove(counter);
	}

	@Override
	public void unsetCounter(String counter, int offset) {
		// TODO Auto-generated method stub

	}

	@Override
	public void incrementCounter(String counter, int increment) {
		setCounter(counter, getCounter(counter) + increment);
	}

	@Override
	public void incrementCounter(String counter, int offset, int increment) {
		// TODO Auto-generated method stub

	}

	@Override
	public void decrementCounter(String counter, int decrement) {
		incrementCounter(counter, -1 * decrement);
	}

	@Override
	public void decrementCounter(String counter, int offset, int decrement) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean getFlag(String flag) {
		return flags.getOrDefault(flag, IFlagProvider.EMPTY);
	}

	@Override
	public boolean getFlag(String flag, int offset) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setFlag(String flag, boolean value) {
		flags.put(flag, value);
	}

	@Override
	public void setFlag(String flag, int offset, boolean value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setFlag(String flag) {
		setFlag(flag, true);
	}

	@Override
	public void setFlag(String flag, int offset) {
		// TODO Auto-generated method stub

	}

	@Override
	public void unsetFlag(String flag) {
		flags.remove(flag);
	}

	@Override
	public void unsetFlag(String flag, int offset) {
		// TODO Auto-generated method stub

	}

}
