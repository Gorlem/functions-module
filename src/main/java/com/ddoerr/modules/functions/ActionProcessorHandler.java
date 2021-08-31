package com.ddoerr.modules.functions;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;

import net.eq2online.macros.core.executive.MacroAction;
import net.eq2online.macros.core.executive.MacroActionProcessor;
import net.eq2online.macros.scripting.api.IMacroAction;
import net.eq2online.macros.scripting.api.IScriptAction;
import net.eq2online.macros.scripting.parser.ScriptContext;
import net.eq2online.macros.scripting.parser.UnrecognisedAction;

public class ActionProcessorHandler {
	private final MacroActionProcessor actionProcessor;
	
	public ActionProcessorHandler(MacroActionProcessor actionProcessor) {
		this.actionProcessor = actionProcessor;
	}
	
	public static ActionProcessorHandler from(IMacroAction macroAction) {
		return new ActionProcessorHandler((MacroActionProcessor) macroAction.getActionProcessor());
	}
	
	public int getCurrentPointer() {
		return getFieldValue("pointer");
	}
	
	public List<IMacroAction> getActionsBetween(int start, int end) {
		List<IMacroAction> actions = getFieldValue("actions");
		return actions.subList(start + 1, end);
	}
	
	public void replaceActions(String name) {
		List<IMacroAction> actions = this.<List<IMacroAction>>getFieldValue("actions")
				.stream()
				.map(action -> {
					if (action.getAction() instanceof UnrecognisedAction && action.getAction().getName().equalsIgnoreCase(name)) {
						IScriptAction scriptAction = ScriptContext.MAIN.getAction("call");
						
						String rawParams = name + "," + action.getRawParams();
						String[] params = ArrayUtils.add(action.getParams(), name);
						ArrayUtils.shift(params, 1);
						
						return new MacroAction(actionProcessor, scriptAction,
								rawParams, ((MacroAction)action).getUnparsedParams(), params,
								action.hasOutVar() ? action.getOutVarName() : null);
					}
					
					return action;
				})
				.collect(Collectors.toList());
		
		setFieldValue("actions", actions);
	}
	
	@SuppressWarnings("unchecked")
	private <T> T getFieldValue(String fieldName) {
		try {
			Field actionsField = actionProcessor.getClass().getDeclaredField(fieldName);
			actionsField.setAccessible(true);
			return (T) actionsField.get(actionProcessor);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	private <T> void setFieldValue(String fieldName, T value) {
		try {
			Field actionsField = actionProcessor.getClass().getDeclaredField(fieldName);
			actionsField.setAccessible(true);
			actionsField.set(actionProcessor, value);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
