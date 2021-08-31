package com.ddoerr.modules.functions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import net.eq2online.macros.core.executive.MacroAction;
import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IMacroAction;
import net.eq2online.macros.scripting.api.IReturnValue;
import net.eq2online.macros.scripting.api.IReturnValueArray;
import net.eq2online.macros.scripting.api.IScriptActionProvider;
import net.eq2online.macros.scripting.api.IStringProvider;
import net.eq2online.macros.scripting.api.ReturnValue;
import net.eq2online.macros.scripting.api.ReturnValueArray;

public class VariableHandler {	
	private static final Pattern variablePattern = Pattern.compile("^([#&]?)([a-z][a-z0-9_\\-]*)(\\[\\])?$");
	private static final String temporaryVariable = "&functions--variable--internal";
	private static final IReturnValue emptyValue = new ReturnValue(IStringProvider.EMPTY);
	
	private static IScriptActionProvider getProvider(IMacro macro) {
		return macro.getContext().getActionProvider();
	}
	
	private static String getArrayName(String name) {
		if (name.endsWith("[]")) {
			return name.substring(0, name.length() - 2);
		}
		
		return name;
	}
	
	public static boolean isValid(String name) {
		return variablePattern.matcher(name).matches();
	}
	
	public static boolean isScalar(String name) {
		Matcher matcher = variablePattern.matcher(name);
		return matcher.matches() && matcher.group(3) == null;
	}
	
	public static boolean isArray(String name) {
		Matcher matcher = variablePattern.matcher(name);
		return matcher.matches() && matcher.group(3) != null;
	}
	
	public static String getTemporaryScalar() {
		return temporaryVariable;
	}
	
	public static String getTemporaryArray() {
		return temporaryVariable + "[]";
	}
	
	public static IReturnValue getEmpty() {
		return emptyValue;
	}
	
	public static void set(IMacro macro, String name, IReturnValue value) {
		if (isArray(name)) {
			setArray(macro, name, value);
		} else if (isScalar(name)) {
			setScalar(macro, name, value);
		}
	}
	
	public static void setArray(IMacro macro, String name, IReturnValue value) {
		if (value instanceof IReturnValueArray) {
			setArray(macro, name, ((IReturnValueArray)value).getStrings());
		} else {
			setArray(macro, name, Collections.singleton(value.getString()));
		}
	}
	
	public static void setArray(IMacro macro, String name, String[] value) {
		setArray(macro, name, Arrays.asList(value));
	}
	
	public static void setArray(IMacro macro, String name, Iterable<String> value) {
		IScriptActionProvider provider = getProvider(macro);
		String arrayName = getArrayName(name);
		
		provider.clearArray(macro, arrayName);

		for (String entry : value) {
			provider.pushValueToArray(macro, arrayName, entry);
		}
	}
	
	public static void setScalar(IMacro macro, String name, IReturnValue value) {
		IScriptActionProvider provider = getProvider(macro);
		provider.setVariable(macro, name, value);
	}
	
	public static void setScalar(IMacro macro, String name, String value) {
		IScriptActionProvider provider = getProvider(macro);
		provider.setVariable(macro, name, value);
	}
	
	public static IReturnValue get(IMacro macro, String name) {
		if (isArray(name)) {
			return getArray(macro, name);
		} else if (isScalar(name)) {
			return getScalar(macro, name);
		}
		
		return getEmpty();
	}

	public static IReturnValue getScalar(IMacro macro, String name) {
		IScriptActionProvider provider = getProvider(macro);
		
		String value = provider.getVariable(name, macro).toString();
		return new ReturnValue(value);
	}
	
	public static IReturnValueArray getArray(IMacro macro, String name) {
		IScriptActionProvider provider = getProvider(macro);
		String arrayName = getArrayName(name);
		
		int arraySize = provider.getArraySize(macro, arrayName);
		List<String> values = new ArrayList<String>();
		
		for (int i = 0; i < arraySize; i++) {
			values.add(provider.getArrayElement(macro, arrayName, i).toString());
		}
		
		ReturnValueArray arrayValue = new ReturnValueArray(false);
		arrayValue.putStrings(values);
		return arrayValue;
	}
	
	public static void copyArray(IMacro sourceMacro, String sourceName, IMacro targetMacro, String targetName) {
		IScriptActionProvider sourceProvider = getProvider(sourceMacro);
		IScriptActionProvider targetProvider = getProvider(targetMacro);
		
		String sourceArray = getArrayName(sourceName);
		String targetArray = getArrayName(targetName);
		
		int arraySize = sourceProvider.getArraySize(sourceMacro, sourceArray);
		
		for (int i = 0; i < arraySize; i++) {
			String element = sourceProvider.getArrayElement(sourceMacro, sourceArray, i).toString();
			targetProvider.pushValueToArray(targetMacro, targetArray, element);
		}
	}
	
	public static String expand(IMacro macro, String text) {
		return getProvider(macro).expand(macro, text, false);
	}
	
	public static IReturnValue parseParameters(IMacroAction macroAction, IMacro macro) {
		IScriptActionProvider provider = getProvider(macro);
		
		String rawParams = macroAction.getRawParams();
		String[] params = macroAction.getParams();
		String unparsedParams = ((MacroAction)macroAction).getUnparsedParams();

		if (VariableHandler.isValid(rawParams)) {
			return VariableHandler.get(macro, rawParams);
		} else if (params.length > 1) {
			List<String> values = Arrays.stream(params)
				.map((value) -> VariableHandler.expand(macro, value))
				.collect(Collectors.toList());
			
			ReturnValueArray returnValue = new ReturnValueArray(false);
			returnValue.putStrings(values);
			return returnValue;
		} else if (Lists.newArrayList('"', '%').contains(unparsedParams.charAt(0))) {
			return new ReturnValue(VariableHandler.expand(macro, rawParams));
		} else {
			int result = provider.getExpressionEvaluator(macro, VariableHandler.expand(macro, rawParams)).evaluate();
			return new ReturnValue(result);
		}
	}
}