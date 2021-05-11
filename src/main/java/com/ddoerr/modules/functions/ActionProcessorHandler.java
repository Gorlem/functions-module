package com.ddoerr.modules.functions;

import java.lang.reflect.Field;
import java.util.List;

import net.eq2online.macros.core.executive.MacroActionProcessor;
import net.eq2online.macros.scripting.api.IMacroAction;

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
}
